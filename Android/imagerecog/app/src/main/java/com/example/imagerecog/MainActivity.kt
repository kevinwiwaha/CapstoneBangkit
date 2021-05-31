package com.example.imagerecog

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.imagerecog.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


class MainActivity : AppCompatActivity() {
    //declare the variables
    lateinit var bitmap: Bitmap
    lateinit var imgview: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        // bikin button buat connect ke next activity
        val button_belajar = findViewById<Button>(R.id.button3)
        button_belajar.setOnClickListener {
            val next_intent = Intent(this, MainActivity2::class.java)

            startActivity(next_intent)
        }
        //inisialisasi variabelnya imgview ke findViewByid terus kita search
        // resources.id.imageview
        imgview = findViewById(R.id.imageView)

        //initialize textview
        var tv:TextView = findViewById(R.id.textView)



        // Get button using its id :
        var selectfiles: Button = findViewById(R.id.button)
        // Set OnClickListener
        selectfiles.setOnClickListener(View.OnClickListener {

            //buat fungsi dari si buttonnya
            var intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, 100)

        })
        //code for the functionality for the predict button
        var predict:Button = findViewById(R.id.button2)

        predict.setOnClickListener(View.OnClickListener {
            // pertama, resize the bitmap
            var resized: Bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val model = Model.newInstance(this)

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)

            //create bytebuffer from these resize images

            var tbuffer = TensorImage.fromBitmap(resized)
            var byteBuffer = tbuffer.buffer
            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            //output feature0 is our output prediction
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            /* to convert output feature to string,
            first, convert to float array and select one index
             */

            tv.setText(outputFeature0.floatArray[10].toString())

            // Releases model resources if no longer used.
            model.close()
        })
    }
    // we will see, once the user select the images, change the image view, to the selected
    //  images
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        imgview.setImageURI(data?.data)
        var uri: Uri?= data?.data

        /* we will store this image to the bitmap, so that we can
        predict.
        jd kita pake mediastore dan getting the bitmap from the Uri
        which is stored in this data variable
        */
        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
    }
}