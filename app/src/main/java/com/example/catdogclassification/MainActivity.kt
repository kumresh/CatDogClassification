package com.example.catdogclassification

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import org.pytorch.*
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var predictBtn: MaterialButton
    private lateinit var outputText: TextView
    private lateinit var imageView: ImageView
    private lateinit var myBitmap: Bitmap
    private lateinit var myModule: Module
    private lateinit var progressBar:ProgressBar

    private val SELECTED_PHOTO = 11
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        predictBtn = findViewById(R.id.predict)
        outputText = findViewById(R.id.output)
        imageView = findViewById(R.id.image)
        progressBar = findViewById(R.id.progress_bar)

        outputText.isVisible = false
        progressBar.isVisible = false

        try {
            myBitmap = BitmapFactory.decodeStream(assets.open("image.jpeg"))
            myModule = LiteModuleLoader.load(assetFilePath(this, "model.ptl"))
        } catch (e: IOException) {
            Log.e("PytorchHelloWorld", "Error reading assets", e)
            finish()
        }
        imageView.setImageBitmap(myBitmap)

        imageView.setOnClickListener {
            selectImage()
        }

        predictBtn.setOnClickListener {
            outputText.isVisible = false
            progressBar.isVisible = true
            predictImage()
        }


    }

    private fun predictImage() {
        val dogCatClass = ArrayList<String>()
        dogCatClass.add("Cat")
        dogCatClass.add("Dog")
        val thread = Thread { // preparing input Tensor
            val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
                myBitmap,
                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
                TensorImageUtils.TORCHVISION_NORM_STD_RGB,
                MemoryFormat.CHANNELS_LAST
            )

            // running the model
            val outputTensor: Tensor = myModule.forward(IValue.from(inputTensor)).toTensor()
            val scores = outputTensor.dataAsFloatArray
            var maxScore = -Float.MAX_VALUE
            var maxScoreIdx = -1
            for (i in scores.indices) {
                if (scores[i] > maxScore) {
                    maxScore = scores[i]
                    maxScoreIdx = i
                }
            }
            val className: String = dogCatClass[maxScoreIdx]
            runOnUiThread {
                outputText.text = "$className"
                outputText.isVisible = true
                progressBar.isVisible = false
            }
        }

        thread.start()

    }

    private fun selectImage() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery,SELECTED_PHOTO)
    }

    @Throws(IOException::class)
    fun assetFilePath(context: Context, assetName: String?): String? {
        val file = File(context.filesDir, assetName)
        if (file.exists() && file.length() > 0) {
            return file.absolutePath
        }
        context.assets.open(assetName!!).use { `is` ->
            FileOutputStream(file).use { os ->
                val buffer = ByteArray(1020 * 1024)
                var read: Int
                while (`is`.read(buffer).also { read = it } != -1) {
                    os.write(buffer, 0, read)
                }
                os.flush()
            }
            return file.absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECTED_PHOTO){
            if (resultCode == RESULT_OK ){
                val uri = data?.data
                imageView.setImageURI(uri)
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    imageView.setImageBitmap(myBitmap)
                } catch (e: IOException) {
                    e.stackTrace
                }
            }
        }
    }
}