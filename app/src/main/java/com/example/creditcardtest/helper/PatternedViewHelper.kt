package com.psi.residentportal.utilities.patterviewhelper

import android.content.res.TypedArray
import android.os.Parcelable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import com.example.creditcardtest.R
import com.example.creditcardtest.helper.PatternUtils
import com.example.creditcardtest.helper.PetSavedState

class PatternedViewHelper(private val editText: EditText) {

    var specialChar: String? = null
    var pattern: String? = null

    var rawText = ""
        private set

    fun resolveAttributes(attrs: AttributeSet?) {
        if (attrs == null) return
        val a = editText.context.obtainStyledAttributes(attrs, R.styleable.PatternedEditText)
        pattern = a.getString(R.styleable.PatternedEditText_pattern)
        specialChar = a.getString(R.styleable.PatternedEditText_specialChar)
        if (specialChar == null) {
            specialChar = "#"
        }
        val showHint = a.getBoolean(R.styleable.PatternedEditText_showPatternAsHint, false)
        a.recycle()

        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(pattern!!.length))

        if (showHint) {
            editText.hint = pattern
        }

        val textWatcher = object : TextWatcher {
            private var mForcing = false
            private var sb: StringBuilder? = null

            private var isDeleting = false

            private var differenceCount = 0
            var toBeSetCursorPosition = 0
            var mBeforeTextLength = 0

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                mBeforeTextLength = s.length
                if (!mForcing) {
                    sb = StringBuilder()
                    differenceCount = PatternUtils.getDifferenceCount(
                        s.toString().substring(0, editText.selectionStart),
                        pattern,
                        specialChar!![0]
                    )
                    sb!!.append(rawText)
                    if (after == 0) {
                        isDeleting = true
                        try {
                            sb!!.delete(
                                editText.selectionEnd - count - differenceCount,
                                editText.selectionEnd - differenceCount
                            )
                        } catch (e: IndexOutOfBoundsException) {
                            //Do nothing. User tried to delete unremovable char(s) of pattern.
                        }

                    } else {
                        isDeleting = false
                    }
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!mForcing) {
                    if (!isDeleting) {
                        try {
                            var from = editText.selectionEnd - count - differenceCount
                            if (from < 0) {
                                from = 0
                            }
                            sb!!.insert(from, s.subSequence(start, start + count))
                        } catch (e: StringIndexOutOfBoundsException) {
                            Log.e("PatternedEditText: ", e.toString())
                            //getSelectionEnd() returns 0 after screen rotation.
                            //Added to handle filling EditText after rotation.
                            //onRestoreInstanceState() is responsible for setting text.
                        }

                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (!mForcing) {
                    mForcing = true
                    rawText = sb!!.toString()
                    val convertedText: String
                    if (PatternUtils.isTextAppliesPattern(rawText, pattern!!, specialChar!![0])) {
                        convertedText = rawText
                        rawText = PatternUtils.convertPatternedTextToText(rawText, pattern, specialChar!![0])
                    } else {
                        convertedText = PatternUtils.convertTextToPatternedText(rawText, pattern!!, specialChar!![0])
                    }
                    toBeSetCursorPosition = editText.selectionStart + convertedText.length - s.length
                    if (mBeforeTextLength == 0) {
                        toBeSetCursorPosition = convertedText.length
                    }
                    s.clear()
                    s.append(convertedText)
                    try {
                        if (isDeleting) {
                            if (toBeSetCursorPosition < convertedText.length) {
                                ++toBeSetCursorPosition
                            }
                        } else if (toBeSetCursorPosition != convertedText.length) {
                            --toBeSetCursorPosition
                        }
                        editText.setSelection(toBeSetCursorPosition)
                    } catch (e: IndexOutOfBoundsException) {
                        Log.e("PatternedEditText: ", e.toString())
                    }

                    mForcing = false
                }
            }
        }
        editText.addTextChangedListener(textWatcher)
    }

    fun onSaveInstanceState(superState: Parcelable): Parcelable {
        return PetSavedState(superState, rawText)
    }

    fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as PetSavedState
        rawText = savedState.realText
        editText.setText(rawText)
    }

    fun setText() {
        this.rawText = ""
    }

}
