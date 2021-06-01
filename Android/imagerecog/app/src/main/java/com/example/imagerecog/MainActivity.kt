package com.example.imagerecog

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.imagerecog.ml.MobilenetV110224Quant
import com.example.imagerecog.ml.MobilenetV210224Quant
import com.example.imagerecog.ml.Model
import com.example.imagerecog.repository.Repository
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer


class MainActivity : AppCompatActivity() {
    //declare the variables
    lateinit var bitmap: Bitmap
    lateinit var imgview: ImageView
    lateinit var text_view : TextView
    private lateinit var viewModel: MainViewModel

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
//            val labels = application.assets.open("labels.txt").bufferedReader().use { it.readText() }.split("\n")

        //initialize textview
        var tv:TextView = findViewById(R.id.textView)
        text_view = findViewById(R.id.textView)


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
            var resized = Bitmap.createScaledBitmap(bitmap, 224*2, 224*2, true)
//            val model = MobilenetV110224Quant.newInstance(this)
            val model = Model.newInstance(this)
//            val model = MobilenetV210224Quant.newInstance(this)
            var tbuffer = TensorImage.fromBitmap(resized)
            var byteBuffer = tbuffer.buffer

// Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224*2, 224*2, 3), DataType.UINT8)
            inputFeature0.loadBuffer(byteBuffer)
            Log.d("Tensor",inputFeature0.buffer.toString())
// Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            println(outputFeature0.floatArray[0])
//            var max = getMax(outputFeature0.floatArray)
//            Log.d("MAX",max.toString())
            if(outputFeature0.floatArray[0] > 0.5){
                var result = "Anorganik"
                text_view.setText(result)

            }else if(outputFeature0.floatArray[0] < 0.5){
                var result = "Organik"
                text_view.setText(result)

            }

// Releases model resources if no longer used.
            model.close()
        })
    }
    fun getMax(arr:FloatArray) : Int{
        var ind = 0;
        var min = 0.0f;

        for(i in 0..1000)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                ind = i;
            }
        }
        return ind
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