package com.example.myapplication



import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    private lateinit var infoTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var buttonToMainActivity: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        buttonToMainActivity = findViewById(R.id.btnToMainAct)
        buttonToMainActivity.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        infoTextView = findViewById(R.id.infoTextView)
        imageView = findViewById(R.id.imageView)

        // Проверяем тип действия и получаем файл
        if (intent?.action == Intent.ACTION_SEND) {
            handleIncomingFile()
        }
    }

    private fun handleIncomingFile() {
        // Получаем URI переданного файла
        val fileUri: Uri? = intent.getParcelableExtra(Intent.EXTRA_STREAM)

        if (fileUri != null) {
            // Отобразим информацию о файле или URI
            infoTextView.text = "Получен файл: $fileUri"
            // Если это изображение, можно сразу отобразить его
            imageView.setImageURI(fileUri)
        } else {
            Toast.makeText(this, "Файл не получен", Toast.LENGTH_SHORT).show()
        }
    }
}
