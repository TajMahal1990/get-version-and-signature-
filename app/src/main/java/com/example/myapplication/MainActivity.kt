package com.example.myapplication


import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var infoTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация TextView
        infoTextView = findViewById(R.id.infoTextView)

        // Пакет, который нужно проверить
        val packageToCheck = "ru.otpbank.mobile"

        // Проверка наличия приложения
        if (isAppInstalled(packageToCheck)) {
            val versionAndSignature = getAppInfo(packageToCheck)
            if (versionAndSignature != null) {
                val (version, signature) = versionAndSignature
                // Выводим информацию в TextView
                val resultText = "Версия: $version\nПодпись: $signature"
                infoTextView.text = resultText
            }
        } else {
            val noAppMessage = "Приложение с пакетом $packageToCheck не найдено"
            infoTextView.text = noAppMessage
        }
    }

    // Метод для проверки наличия приложения на устройстве
    private fun isAppInstalled(packageName: String): Boolean {
        val packageManager: PackageManager = packageManager
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    // Метод для получения версии и подписи приложения
    private fun getAppInfo(packageName: String): Pair<String, String>? {
        return try {
            // Получаем информацию о пакете
            val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)

            // Извлекаем версию приложения
            val version = packageInfo.versionName ?: "Неизвестно"

            // Извлекаем подпись
            val signature = packageInfo.signingInfo?.apkContentsSigners?.firstOrNull()?.let {
                val digest = MessageDigest.getInstance("SHA-256")
                digest.update(it.toByteArray())
                bytesToHex(digest.digest())
            } ?: "Нет подписи"

            // Возвращаем версию и подпись
            Pair(version, signature)
        } catch (e: Exception) {
            Log.e("AppInfo", "Не удалось получить информацию о приложении $packageName", e)
            null
        }
    }

    // Метод для преобразования байтов в строку HEX
    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v.ushr(4)]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}
