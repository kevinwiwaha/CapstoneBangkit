package com.example.imagerecog

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.imagerecog.ml.MobilenetV110224Quant
import com.example.imagerecog.ml.MobilenetV210224Quant
import com.example.imagerecog.ml.Model
import com.example.imagerecog.model.Post
import com.example.imagerecog.repository.Repository
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    //declare the variables
    lateinit var bitmap: Bitmap
    lateinit var imgview: ImageView
    lateinit var text_view : TextView
    lateinit var btnMainActivity2 : Button
    lateinit var btnMainActivity3 : Button
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


            //bikin button camera
    val button_camera = findViewById<Button>(R.id.button4)
    button_camera.setOnClickListener {
        var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(i, 101)
    }

        /*
        fungsi camera permission, jika user ngasi ijin kamera
         */

        button_camera.isEnabled = false
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 111)
        }
        else
            button_camera.isEnabled = true



//        fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if(requestCode === 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//        {
//            button_camera.isEnabled = true
//        }
//    }





        // bikin button buat connect ke next activity
//        val btnBelajarOgranik = findViewById<Button>(R.id.button3)
//        btnBelajarOgranik.setOnClickListener {
//            val next_intent = Intent(this, MainActivity2::class.java)
//
//            startActivity(next_intent)
//        }


        //inisialisasi variabelnya imgview ke findViewByid terus kita search
        // resources.id.imageview
        imgview = findViewById(R.id.imageView)
        btnMainActivity2 = findViewById(R.id.btnMainActivity2)
        btnMainActivity3 = findViewById(R.id.btnMainActivity3)
        btnMainActivity2.setVisibility(View.GONE)
        btnMainActivity3.setVisibility(View.GONE)
            val labels = application.assets.open("label.txt").bufferedReader().use { it.readText() }.split("\n")

        //initialize textview
        text_view = findViewById(R.id.textView)
        btnMainActivity2.setOnClickListener {
            val next_intent = Intent(this, MainActivity2::class.java)
            startActivity(next_intent)
        }

        btnMainActivity3.setOnClickListener {
            val next_intent = Intent(this, MainActivity3::class.java)
            startActivity(next_intent)
        }

        // bikin button selectfiles :
        var selectfiles: Button = findViewById(R.id.button)
        // Set OnClickListener
        selectfiles.setOnClickListener(View.OnClickListener {

            //buat fungsi dari si buttonnya
            var intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 100)

        })
        //bikin button predict & code for the functionality for the predict button
        var predict:Button = findViewById(R.id.button2)

        predict.setOnClickListener(View.OnClickListener {
            var resized = Bitmap.createScaledBitmap(bitmap, 200, 200, true)
//            val model = MobilenetV110224Quant.newInstance(this)
            val model = Model.newInstance(this)
//            val model = MobilenetV210224Quant.newInstance(this)
            var tbuffer = TensorImage.fromBitmap(resized)
            var byteBuffer = tbuffer.buffer

//// Creates inputs for reference. [GA DIPAKE]
//            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224*2, 224*2, 3), DataType.UINT8)
//            inputFeature0.loadBuffer(byteBuffer)
//            Log.d("Tensor",inputFeature0.buffer.toString())
//// Runs model inference and gets result.
//            val outputs = model.process(inputFeature0)
//            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//
//            println(outputFeature0.floatArray[0])
////            var max = getMax(outputFeature0.floatArray)
////            Log.d("MAX",max.toString())
//            if(outputFeature0.floatArray[0] > 0.5){
//                var result = "Anorganik"
//                text_view.setText(result)
//                val resultData = Intent(this@MainActivity,MainActivity2::class.java)
//                resultData.putExtra("WasteType",result)
//                startActivity(resultData)
//
//            }else if(outputFeature0.floatArray[0] < 0.5){
//                var result = "Organik"
//                text_view.setText(result)
//
//            }
//
//// Releases model resources if no longer used.
//            model.close()

//            RESIZED IMAGE ENCODE BASE64
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            val encoded: String = Base64.encodeToString(byteArray,Base64.DEFAULT)

//            Datetime
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.BASIC_ISO_DATE
            val formatted = currentDateTime.format(formatter)

//            HTTP REQUEST
            val repository = Repository()
            val viewModelFactory = MainViewModelFactory(repository)
            viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
            val myPost = Post(2,5,formatted,encoded)
            viewModel.pushPost(2,5,formatted,encoded)
            viewModel.myResponse.observe(this, Observer { response ->
                Log.i("Pred",response.body)
                if(response.body.toFloat() > 0.5){
                var result = "Anorganik"
                text_view.setText(result)
                btnMainActivity3.setVisibility(View.VISIBLE)
                btnMainActivity2.setVisibility(View.INVISIBLE)
                val resultData = Intent(this@MainActivity,MainActivity3::class.java)
                    resultData.putExtra("WasteType",result)


                }else if(response.body.toFloat() < 0.5){
                    var result = "Organik"
                    text_view.setText(result)
                    btnMainActivity2.setVisibility(View.VISIBLE)
                    btnMainActivity3.setVisibility(View.INVISIBLE)
                    val resultData = Intent(this@MainActivity,MainActivity2::class.java)
                    resultData.putExtra("WasteType",result)

                }

            })

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

//    //bikin button camera
//    val button_camera = findViewById<Button>(R.id.button4)
//    button_camera.setOnClickListener {
//        var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//        startActivityForResult(i, 101)
//    }


    var button_camera: Button? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode === 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            button_camera?.isEnabled = true
        }
    }


    // we will see, once the user select the images, change the image view, to the selected
    //  images
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        var uri: Uri?= data?.data

        if (requestCode == 101 && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
//            var uri: Uri?= data?.data
            imgview.setImageBitmap(imageBitmap)
            bitmap = imageBitmap
        } else if(requestCode == 100) {
            imgview.setImageURI(data?.data)
            var uri: Uri?= data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        }



//        if(requestCode == 101 )
//        {
//            imgview.setImageURI(data?.data)
//            var uri: Uri?= data?.data
////            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//
//
//        }else if(requestCode == 100){
//            imgview.setImageURI(data?.data)
//            var uri: Uri?= data?.data
//            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//
//
//        }

        /* we will store this image to the bitmap, so that we can
        predict.
        jd kita pake mediastore dan getting the bitmap from the Uri
        which is stored in this data variable
        */


    }
}