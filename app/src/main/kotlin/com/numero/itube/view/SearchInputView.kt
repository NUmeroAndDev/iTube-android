package com.numero.itube.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.numero.itube.R
import com.numero.itube.extension.hideKeyboard
import com.numero.itube.extension.showKeyboard
import kotlinx.android.synthetic.main.view_search_input.view.*

class SearchInputView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var listener: OnQueryTextListener? = null

    init {
        View.inflate(context, R.layout.view_search_input, this)

        initEditText()
    }

    fun setOnQueryTextListener(listener: OnQueryTextListener) {
        this.listener = listener
    }

    private fun initEditText() {
        editText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.showKeyboard()
            } else {
                view.hideKeyboard()
            }
        }
        editText.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                val text = editText.text
                val query = text?.toString() ?: ""
                listener?.onQueryTextSubmit(query)
                editText.clearFocus()
            }
            false
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = editText.text
                val query = text?.toString() ?: ""
                listener?.onQueryTextChange(query)
                clearButton.isVisible = query.isEmpty().not()
            }
        })
        clearButton.setOnClickListener {
            editText.requestFocus()
            editText.setText("")
            editText.showKeyboard()
        }
    }

    interface OnQueryTextListener {
        fun onQueryTextSubmit(query: String)

        fun onQueryTextChange(newText: String)
    }
}
