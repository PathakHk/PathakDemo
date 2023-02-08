package com.app.cfm.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.navkar.myapp.R
import com.navkar.myapp.activities.DialogCallBacks
import com.navkar.myapp.databinding.BottomsheetImagePickerBinding

class ImagePickerBottomSheet : BottomSheetDialogFragment() {

    private var mContext: Context? = null
    private var dialogCallBacks: DialogCallBacks? = null
    lateinit var mBinder: BottomsheetImagePickerBinding
    private var isDocumentVisible = true

    fun newInstance(
        mContext: Context,
        isDocumentVisible: Boolean,
        dialogCallBack: DialogCallBacks
    ): ImagePickerBottomSheet {
        val instance = ImagePickerBottomSheet()
        instance.mContext = mContext
        instance.isDocumentVisible = isDocumentVisible
        instance.dialogCallBacks = dialogCallBack
        return instance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinder =
            DataBindingUtil.inflate(inflater, R.layout.bottomsheet_image_picker, container, false)
        mBinder.llImageCamera.setOnClickListener(clickListener)
        mBinder.llImageGallery.setOnClickListener(clickListener)
        mBinder.isDocumentVisible = isDocumentVisible

        return mBinder.root
    }

    private val clickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.llImageCamera -> {
                dialogCallBacks!!.dialogClick(0, 1, "")
                dismiss()
            }
            R.id.llImageGallery -> {
                dialogCallBacks!!.dialogClick(0, 2, "")
                dismiss()
            }
        }
    }
}