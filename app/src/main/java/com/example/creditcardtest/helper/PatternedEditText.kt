package com.psi.residentportal.utilities.patterviewhelper

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.TextView

class PatternedEditText : EditText {

    private var patternedViewHelper: PatternedViewHelper? = null

    val rawText: String
        get() = patternedViewHelper!!.rawText

    var specialChar: String?
        get() = patternedViewHelper!!.specialChar
        set(specialChar) {
            patternedViewHelper!!.specialChar = specialChar
        }

    var pattern: String?
        get() = patternedViewHelper!!.pattern
        set(pattern) {
            patternedViewHelper!!.pattern = pattern
        }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        patternedViewHelper = PatternedViewHelper(this)
        patternedViewHelper!!.resolveAttributes(attrs)
    }

    override fun setText(text: CharSequence, type: TextView.BufferType) {
        if (patternedViewHelper != null) {
            patternedViewHelper!!.setText()
        }
        super.setText(text, type)
    }


    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return patternedViewHelper!!.onSaveInstanceState(superState)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as View.BaseSavedState
        super.onRestoreInstanceState(savedState.getSuperState())
        patternedViewHelper!!.onRestoreInstanceState(state)
    }
}
