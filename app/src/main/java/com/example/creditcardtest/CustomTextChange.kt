package com.example.creditcardtest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import kotlinx.android.synthetic.main.custom_text_change.*
import android.view.KeyEvent.KEYCODE_DEL
import android.view.View
import com.example.creditcardtest.Edittext.CreditCardFormattingTextWatcher


class CustomTextChange : AppCompatActivity() {


    var strsDate = ""
    var typedChar: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_text_change)

        edtExpiry.addTextChangedListener(CreditCardFormattingTextWatcher())


        /*edtExpiry.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                println("==============================================")
                println(" after-->" + s.toString())

                edtExpiry.removeTextChangedListener(this)

                formatDate(typedChar.toString(), s.toString())

                edtExpiry.addTextChangedListener(this)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                var strsDate = edtExpiry.text.toString()

                // afte means index after which i started typing


                println("==============================================")
                println(" Before text change String -->" + strsDate)
                println("char -->" + s.toString())
                println("char -->" + s.toString())
                println("start -->" + "" + start)
                println("after -->" + "" + after)
                println("count -->" + "" + count)
                //println("typed string -->" + ""+strsDate[after])
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                strsDate = edtExpiry.text.toString()


                // start means current position in case of back space


                println("==============================================")
                println("String -->" + strsDate)
                println("char -->" + s.toString())
                println("char -->" + s.toString())
                println("start -->" + "" + start)
                println("before -->" + "" + before)
                println("count -->" + "" + count)



                edtExpiry.setOnKeyListener(object : View.OnKeyListener {
                    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                        //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                        if (keyCode == KeyEvent.KEYCODE_DEL) {
                            typedChar = ""
                        }else{
                            if (start == 0) {
                                println("typed string -->" + "" + strsDate[before])
                                typedChar = strsDate[before].toString()
                            } else if (start < strsDate.length) {
                                println("typed string -->" + "" + strsDate[start])
                                typedChar = strsDate[start].toString()
                            }

                        }
                        return false
                    }
                })


            }
        })*/


    }

    fun formatDate(strCurrentChar: String, strCurrentText: String) {

        if (strCurrentText.trim().length == 1) {
            if (strCurrentChar == "1" || strCurrentChar == "2" || strCurrentChar == "3"
                || strCurrentChar == "4" || strCurrentChar == "5" || strCurrentChar == "6"
                || strCurrentChar == "7" || strCurrentChar == "8" || strCurrentChar == "9"
            ) {
                edtExpiry.setText("0" + strCurrentChar + " /")
                edtExpiry.setSelection(edtExpiry.text.length)
            }
        }


    }


}
