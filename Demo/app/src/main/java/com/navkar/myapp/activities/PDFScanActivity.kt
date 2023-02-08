package com.navkar.myapp.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.cfm.dialog.ImagePickerBottomSheet
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.navkar.myapp.R
import com.navkar.myapp.databinding.ActivityPdfScanBinding
import java.io.File


class PDFScanActivity : AppCompatActivity() {

    lateinit var mBinder: ActivityPdfScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_pdf_scan)

        mBinder.text.setOnClickListener {
            openSelectMediaDialog()
        }

    }

    override fun onResume() {
        super.onResume()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val imgListString: ArrayList<String> = ArrayList()
            val imgListDriver: ArrayList<Image> = ImagePicker.getImages(data) as ArrayList<Image>
            for (image in imgListDriver) {
                imgListString.add(image.path)
            }
            processInvoice(imgListString[0])
            //imagePickerListener!!.onPickImage(imgListString)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun processInvoice(path: String) {
        try {
            var contactPerson: String = ""

            if (path != null) {
                val bm = getBitmap(path)
                val image = FirebaseVisionImage.fromBitmap(bm!!)
                val detector = FirebaseVision.getInstance().visionTextDetector

                detector.detectInImage(image).addOnSuccessListener { firebaseVisionText ->
                    val dataMap = InvoiceProcessing().processText(firebaseVisionText, applicationContext)

                    //------output-------
                    try {
                        if (dataMap != null) {

                            if (dataMap["name"] != null)
                                contactPerson = dataMap["name"]!!
                            if (dataMap["mobile"] != null)
                                contactPerson = contactPerson + "\n" + dataMap["mobile"]!!

                            if (dataMap["name1"] != null)
                                contactPerson = contactPerson + "\n\n" + dataMap["name1"]!!
                            if (dataMap["mobile1"] != null)
                                contactPerson = contactPerson + "\n" + dataMap["mobile1"]!!

                            mBinder.tvContact.text = contactPerson

                            //------------

                            if (dataMap["gst"] != null)
                                mBinder.tvGST.text = dataMap["gst"].toString().trim()

                            if (dataMap["email"] != null)
                                mBinder.tvEmail.text = dataMap["email"].toString().trim()

                            if (dataMap["address"] != null)
                                mBinder.tvAddress.text = dataMap["address"].toString().trim()

                            if (dataMap["hsn"] != null)
                                mBinder.tvHSN.text = dataMap["hsn"].toString().trim()

                            if (dataMap["product"] != null)
                                mBinder.tvProduct.text = dataMap["product"].toString().trim()

                            if (dataMap["industry"] != null)
                                mBinder.tvIndustry.text = dataMap["industry"].toString().trim()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.addOnFailureListener { }
            } else {
                Toast.makeText(this, "Please Take a Picture first", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getBitmap(path: String?): Bitmap? {
        return try {
            val f = File(path)
            BitmapFactory.decodeFile(f.absolutePath)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }

    fun openSelectMediaDialog() {
        val bottomSheetDialog = ImagePickerBottomSheet().newInstance(this, true,
            object : DialogCallBacks {
                override fun onDismiss(bundle: Bundle) {
                }

                override fun dialogClick(position: Int, id: Int, remark: String?) {
                    when (id) {
                        1 ->
                            ImagePicker.cameraOnly().start(this@PDFScanActivity)
                        2 -> {
                            ImagePicker.create(this@PDFScanActivity)
                                .folderMode(true) // folder mode (false by default)
                                .toolbarFolderTitle("Gallery") // folder selection title
                                .toolbarImageTitle("Tap to select") // image selection title
                                .toolbarArrowColor(Color.parseColor("#ffffff")) // Toolbar 'up' arrow color
                                .includeVideo(false) // Show video on image picker
                                .limit(1) // max images can be selected (99 by default)
                                .showCamera(false) // show camera or not (true by default)
                                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                                .enableLog(false) // disabling log
                                .start() // start image picker activity with request code
                        }

                    }
                }
            })
        bottomSheetDialog.show(supportFragmentManager, "selectMediaDialog")
    }

}