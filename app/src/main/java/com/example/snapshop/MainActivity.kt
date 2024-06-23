package com.example.snapshop

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.snapshop.databinding.ActivityMainBinding
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var imageCapture: ImageCapture
    private val apiKey = "AIzaSyC_zYTrGIQMYnW3zaUYXr8h5O7YJreaQtw"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { openCamera() }
        binding.appBarMain.cameraButton.setOnClickListener { takePicture() }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissions.contains(android.Manifest.permission.CAMERA)) {
            if (grantResults[permissions.indexOf(android.Manifest.permission.CAMERA)] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera required for functionality", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 100)
        } else {
            val previewView = findViewById<View>(R.id.previewView) as PreviewView
            val buttonView = findViewById<View>(R.id.cameraButton)
            previewView.visibility = RelativeLayout.VISIBLE
            buttonView.visibility = RelativeLayout.VISIBLE
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                imageCapture = ImageCapture.Builder().build()
                val cameraProvider = cameraProviderFuture.get()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                val preview = Preview.Builder().build()
                preview.setSurfaceProvider(previewView.surfaceProvider)

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(this))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun takePicture() {
        val imageCapture = imageCapture
        val fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMMdd_HHmmss")) + ".jpg"
        val file = File(externalMediaDirs.first(), fileName)
        val options = ImageCapture.OutputFileOptions.Builder(file).build()
        Toast.makeText(this, file.absolutePath, Toast.LENGTH_LONG).show()

        imageCapture.takePicture(options, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val previewView = findViewById<View>(R.id.previewView) as PreviewView
                val buttonView = findViewById<View>(R.id.cameraButton)
                previewView.visibility = RelativeLayout.GONE
                buttonView.visibility = RelativeLayout.GONE

                val bitmap = BitmapFactory.decodeFile(file.absolutePath)

                callGeminiAPI(bitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(this@MainActivity, "Error saving image: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun callGeminiAPI(bitmap: Bitmap) {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputContent = content {
                    image(bitmap)
                    text("What is this image?")
                }
                val response = generativeModel.generateContent(inputContent)

                // Display the response using a Toast message
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, response.text, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
