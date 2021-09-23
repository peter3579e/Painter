package com.peter.painter

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.slider.RangeSlider
import com.peter.painter.databinding.ActivityMainBinding
import petrov.kristiyan.colorpicker.ColorPicker
import java.io.IOException
import java.io.OutputStream


class MainActivity : AppCompatActivity() {
    // creating the object of type DrawView
    // in order to get the reference of the View
    private var paint: DrawView? = null

    // creating objects of type button
    private var saveOrRestore: ImageButton? = null
    private var color: ImageButton? = null
    private var eraser: ImageButton? = null
    private var undo: ImageButton? = null
    private var redo: ImageButton? = null
    private var chooseSaveOrRestore: String? = null
    private val REQUEST_CODE = 100

    private lateinit var binding: ActivityMainBinding

    // creating a RangeSlider object, which will
    // help in selecting the width of the Stroke
    private var rangeSlider: RangeSlider? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        // getting the reference of the views from their ids
        paint = binding.drawView
        rangeSlider = binding.rangebar
        undo = binding.btnUndo
        saveOrRestore = binding.btnSave
        color = binding.btnColor
        eraser = binding.btnStroke
        redo = binding.btnRedo

        // creating a OnClickListener for each button,
        // to perform certain actions

        // the undo button will remove the most
        // recent stroke from the canvas
        undo!!.setOnClickListener { paint!!.undo() }
        // the redo button will restore the most
        // recent stroke from the canvas
        redo!!.setOnClickListener { paint!!.redo() }

        // the saveOrRestore will choose the mode to save or restore
        // save mode will save the current
        // canvas which is actually a bitmap
        // in form of PNG, in the storage
        // restore will restore the saved image from the gallery
        saveOrRestore!!.setOnClickListener { selectPath() }
        // the color button will allow the user
        // to select the color of his brush
        color!!.setOnClickListener {
            paint!!.setErase(false)
            val colorPicker = ColorPicker(this@MainActivity)
            colorPicker.setOnFastChooseColorListener(object :
                    ColorPicker.OnFastChooseColorListener {
                override fun setOnFastChooseColorListener(position: Int, color: Int) {
                    // get the integer value of color
                    // selected from the dialog box and
                    // set it as the stroke color
                    paint!!.setColor(color)
                }

                override fun onCancel() {
                    colorPicker.dismissDialog()
                }
                 }) // set the number of color columns
                    // you want  to show in dialog.
                    .setColumns(5) // set a default color selected
                    // in the dialog
                    .setDefaultColorButton(Color.parseColor("#000000"))
                    .show()
        }

        // this button will trigger the eraser mode
        eraser!!.setOnClickListener {
            paint!!.setErase(true)
        }


        // set the range of the RangeSlider
        rangeSlider!!.valueFrom = 0.0f
        rangeSlider!!.valueTo = 100.0f

        // adding a OnChangeListener which will
        // change the stroke width
        // as soon as the user slides the slider
        rangeSlider!!.addOnChangeListener(RangeSlider.OnChangeListener { slider, value, fromUser ->
            paint!!.setStrokeWidth(
                    value.toInt()
            )
        })

        // pass the height and width of the custom view
        // to the init method of the DrawView object
        val vto = paint!!.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                paint!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = paint!!.measuredWidth
                val height = paint!!.measuredHeight
                paint!!.init(height, width)
            }
        })

    }

    private fun selectPath() {
        val items = arrayOf<CharSequence>("Save Image", "Restore Image", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Saving or restoring a Image?")
        builder.setItems(items) { dialog, item ->
            if (items[item] == "Cancel") {

                dialog.dismiss()
            } else {

                chooseSaveOrRestore = items[item].toString()
                callSaveOrRestore()
            }
        }

        builder.show()
    }

    private fun callSaveOrRestore() {
        chooseSaveOrRestore?.let {

            when (it) {

                "Save Image" -> {

                    saveImage()
                }
                "Restore Image" -> {

                    restoreImage()

                }
            }
        }
    }

    private fun restoreImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
//            paint.restoreImage(data?.data)
            var bitmap: Bitmap? = null
            data?.let {
                it.data.let { data ->
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                                contentResolver, data
                        )

                        val opt = BitmapFactory.Options()
                        opt.inScaled = true
                        opt.inPurgeable = true
                        opt.inInputShareable = true
                        var brightBitmap = bitmap
                        brightBitmap = Bitmap.createScaledBitmap(brightBitmap!!, bitmap!!.width, bitmap!!.height, false)
                        var workingBitmap: Bitmap? = null
                        workingBitmap = Bitmap.createBitmap(brightBitmap)
                        val mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true)


                        paint!!.restoreImage(mutableBitmap)

                    } catch (e: IOException) {

                        e.printStackTrace()

                    }
                }
            }
        }
    }


    private fun saveImage() {
        // getting the bitmap from DrawView class
        val bmp = paint!!.save()

        // opening a OutputStream to write into the file
        var imageOutStream: OutputStream? = null
        val cv = ContentValues()

        // name of the file
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png")

        // type of the file
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png")

        // location of the file to be saved
        cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

        // get the Uri of the file which is to be created in the storage
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
        try {
            // open the output stream with the above uri
            imageOutStream = contentResolver.openOutputStream(uri!!)

            // this method writes the files in storage
            bmp!!.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream)

            // close the output stream after use
            imageOutStream!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}