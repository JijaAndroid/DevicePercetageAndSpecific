package com.example.creditcardtest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText
import android.view.View


class EdittextSeparate : AppCompatActivity() {


    lateinit var edtSlash: EditText
    lateinit var edtMonth: EditText
    lateinit var edtYear: EditText
    lateinit var strBeforeTextOfMonth: String
    lateinit var strBeforeTextOfYear: String
    var currentPositionOfMonthEditText: Int = -1
    var currentPositionOfYearEditText: Int = -1
    var listenerSlash: TextWatcher? = null
    lateinit var listenerMonth: TextWatcher
    lateinit var listenerYear: TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        edtSlash = findViewById(R.id.edtSlash)
        edtMonth = findViewById(R.id.edtMonth)
        edtYear = findViewById(R.id.edtYear)



        edtMonth.addTextChangedListener(WatcherMonth())
        edtSlash.addTextChangedListener(watcherSlash())
        edtYear.addTextChangedListener(watcherYear())


        /* edtYear.isEnabled = false
         edtSlash.isEnabled = false


         edtSlash.setOnTouchListener(object : View.OnTouchListener {
             override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                 if (event?.action == MotionEvent.ACTION_DOWN) {
                     edtSlash.clearFocus()
                     edtMonth.requestFocus()
                     removeListenerAndSettext()
                 }

                 return false

             }

         })

         edtYear.setOnTouchListener(object : View.OnTouchListener {
             override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                 if (event?.action == MotionEvent.ACTION_DOWN) {
                     edtYear.clearFocus()
                     edtMonth.requestFocus()
                     removeListenerAndSettext()
                 }

                 return false

             }

         })

         edtSlash.setOnClickListener {
             edtSlash.clearFocus()
             edtMonth.requestFocus()
             removeListenerAndSettext()

         }

         edtYear.setOnClickListener {
             edtYear.clearFocus()
             edtMonth.requestFocus()
             removeListenerAndSettext()

         }*/


        edtSlash.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    processAndMoveBackToMonth("/")
                }
                return false
            }
        })


        edtYear.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (TextUtils.isEmpty(edtYear.text.toString()) || currentPositionOfYearEditText == 0 /*|| currentPositionOfYearEditText == 1*/) {
                        processAndMoveBackToMonth("/")
                    }

                }
                return false
            }
        })


        edtYear.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->

            if (hasFocus) {
                if (TextUtils.isEmpty(edtMonth.text.trim())) {
                    edtYear.clearFocus()
                    edtMonth.requestFocus()
                    removeListenerAndSettext()

                }

            }

        }

        edtSlash.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->

            if (hasFocus) {
                if (TextUtils.isEmpty(edtMonth.text.trim())) {
                    edtYear.clearFocus()
                    edtMonth.requestFocus()
                    removeListenerAndSettext()
                }

            }

        }


    }

    private fun removeListenerAndSettext() {
        edtSlash.removeTextChangedListener(watcherSlash())
        edtSlash.setText("")
        edtSlash.addTextChangedListener(watcherSlash())
    }

    private fun processAndMoveBackToMonthOrSlash(strYear: String) {

        if (strYear.length == 3 && currentPositionOfYearEditText == 0) {
            edtYear.setText(edtYear.text.toString().substring(0, 2))
            edtYear.setSelection(1)
        } else if (strYear.length == 3 && currentPositionOfYearEditText == 1) {
            edtYear.setText(edtYear.text.toString().substring(0, 2))
            edtYear.setSelection(edtYear.text.toString().trim().length)
        } else if (strYear.length == 3) {
            if (!TextUtils.isEmpty(strBeforeTextOfYear)) {
                edtYear.setText(strBeforeTextOfYear)
                edtYear.setSelection(edtYear.text.toString().trim().length)
            }
        }



        if (TextUtils.isEmpty(strYear)) {
            edtYear.clearFocus()
            edtSlash.removeTextChangedListener(watcherSlash())
            edtSlash.setText("")
            edtSlash.addTextChangedListener(watcherSlash())
            edtMonth.requestFocus()
            edtMonth.setSelection(edtMonth.text.trim().length)
        }

    }

    private fun processAndMoveBackToMonth(strSlash: String) {

        if (TextUtils.isEmpty(edtMonth.text.toString()) && TextUtils.isEmpty(edtYear.text.toString())) {
            edtSlash.setText("")
        } else if (!TextUtils.isEmpty(edtMonth.text.toString())) {
            edtSlash.setText("/")
            edtSlash.clearFocus()
            edtMonth.requestFocus()
            edtMonth.setSelection(edtMonth.text.trim().length)
        } else if (TextUtils.isEmpty(edtYear.text.toString())) {
            edtSlash.setText("")
        }


    }

    private fun processAndMoveToSplashNextEdittext(strMonth: String) {
        if (strMonth.length == 1) {
            if (strMonth.toInt() in 2..9) {
                edtMonth.setText("0" + strMonth)
                edtMonth.clearFocus()
                edtSlash.setText("/")
                edtYear.requestFocus()
            }
        } else if (strMonth.length == 2) {
            edtMonth.clearFocus()
            edtSlash.setText("/")
            edtYear.requestFocus()
        } else if (strMonth.length == 2 && strMonth.trim() == "00") {
            edtMonth.setText("0")
        } else if (strMonth.length == 3 && currentPositionOfMonthEditText == 0) {
            edtMonth.setText(strBeforeTextOfMonth)
        } else if (strMonth.length == 3) {
            edtMonth.setText(strBeforeTextOfMonth)
            edtSlash.setText("/")
            edtMonth.clearFocus()
            edtYear.requestFocus()
            edtYear.setSelection(0)
        }


    }

    public fun removeSplashIfTextNotPreset() {

        if (TextUtils.isEmpty(edtMonth.text.trim()) && TextUtils.isEmpty(edtYear.text.trim())) {
            edtSlash.setText("/")
        }


    }

    inner class WatcherMonth : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            edtMonth.removeTextChangedListener(this)
            var strMonth = s.toString()
            processAndMoveToSplashNextEdittext(strMonth)
            edtMonth.addTextChangedListener(this)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            strBeforeTextOfMonth = s.toString()
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            currentPositionOfMonthEditText = start
        }

    }

    inner class watcherSlash : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            edtSlash.removeTextChangedListener(this)
            processAndMoveBackToMonth("/")
            edtSlash.addTextChangedListener(this)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


        }


    }

    inner class watcherYear : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            var strYear = s.toString()
            processAndMoveBackToMonthOrSlash(strYear)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            strBeforeTextOfYear = s.toString()
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            currentPositionOfYearEditText = start
        }


    }


}
