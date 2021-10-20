package net.basicmodel.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputLayout
import net.basicmodel.R

/**
 * Copyright (C) 2021,2021/10/20, a Tencent company. All rights reserved.
 *
 * User : v_xhangxie
 *
 * Desc :
 */
class InputEditText : LinearLayout {
    var view: EditText? = null
    var input: TextInputLayout? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context): View {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_editext, this, true)
        view = v.findViewById(R.id.editText)
        input = v.findViewById(R.id.inputLayout)
        return v
    }

    fun getEditTextView():EditText{
        return view!!
    }

    fun getEditTextContent(): String {
        return view!!.text.toString()
    }

    fun setHint(s: String) {
        input!!.hint = s
    }
}