package net.dankito.banking.ui.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.view_form_edit_text.view.*
import net.dankito.banking.ui.android.R
import net.dankito.banking.ui.android.extensions.textString


open class FormEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {


    init {
        setupUi(context, attrs)
    }

    private fun setupUi(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = inflater.inflate(R.layout.view_form_edit_text, this, true)

        rootView.apply {

            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.FormEditText,
                0, 0).apply {

                try {
                    textInputLayout.hint = getString(R.styleable.FormEditText_android_hint)

                    textInputEditText.setText(getString(R.styleable.FormEditText_android_text))
                    textInputEditText.inputType = getInt(R.styleable.FormEditText_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL)
                    textInputEditText.setSelectAllOnFocus(getBoolean(R.styleable.FormEditText_android_selectAllOnFocus, false))
                } finally {
                    recycle()
                }
            }
        }
    }


    open var text: String
        get() = textInputEditText.textString
        set(value) = textInputEditText.setText(value)

    open val actualEditText: EditText
        get() = textInputEditText

}