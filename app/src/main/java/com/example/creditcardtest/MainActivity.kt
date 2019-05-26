package com.example.creditcardtest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.creditcardtest.Edittext.CardType
import com.example.creditcardtest.Edittext.CreditCardNumberMaskWatcher
import com.example.creditcardtest.Edittext.CreditCardTextChangeListener
import kotlinx.android.synthetic.main.activity_main.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.R.attr.start
import android.content.Intent
import android.text.TextUtils
import com.example.creditcardtest.helper.TwoDigitsCardTextWatcher


class MainActivity : AppCompatActivity(), CreditCardTextChangeListener {

    override fun onCardTypeFound(cardType: CardType) {

        textView.text = getCardTypeImageId(cardType)

    }

    override fun onCardNumberEntered(cardNumber: String?, completed: Boolean) {
    }

    override fun onCardDateEntered(dateValid: Boolean, month: Int, year: Int) {
    }

    override fun onCardCVVEntered(cvv: String?, completed: Boolean) {
    }

    override fun onNext(appendChar: Char?) {
    }

    override fun onPrevious() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btnSpecific.setOnClickListener {
            startActivity(Intent(this, specific::class.java))
        }
        btnPercentage.setOnClickListener {
            startActivity(Intent(this, percentage::class.java))
        }


        //edtCardNumber.addTextChangedListener(CreditCardNumberMaskWatcher(this))

        // mExpiryDate.addTextChangedListener(TwoDigitsCardTextWatcher(mExpiryDate))

        /*mExpiryDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty() && (s.length % 3) == 0) {
                    val c = s[s.length-1]
                    if (c == '/') {
                        s.delete(s.length-1, s.length)
                    }
                }
                if (s.isNotEmpty() && (s.length % 3) == 0) {
                    val c = s[s.length-1]
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), "/").size <= 2) {
                        s.insert(s.length-1, "/")
                    }
                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, added: Int) {

            }
        })*/
/*
 mExpiryDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, added: Int) {
                var current = p0.toString()
                if (current.length == 2 && start == 1) {
                    mExpiryDate.setText(current + "/")
                    mExpiryDate.setSelection(current.length + 1)
                } else if (current.length == 2 && before == 1) {
                    current = current.substring(0, 1)
                    mExpiryDate.setText(current)
                    mExpiryDate.setSelection(current.length)
                }
            }
        })
*/

        /*mExpiryDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {
                if (start == 1 && start+added == 2 && p0?.contains('/') == false) {
                    mExpiryDate.setText(p0.toString() + "/")
                } else if (start == 3 && start-removed == 2 && p0?.contains('/') == true) {
                    mExpiryDate.setText(p0.toString().replace("/", ""))
                }
            }
        })*/


        /*mExpiryDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                var lastInput : String ="";
                var input = s.toString()

                var formatter : SimpleDateFormat = SimpleDateFormat("MM/yy", Locale.GERMANY);
                    var expiryDateDate : Calendar = Calendar.getInstance()
                    try {
                        expiryDateDate.setTime(formatter.parse(input));
                    } catch ( e : ParseException) {
                        if (s?.length == 2 && !lastInput.endsWith("/")) {
                            var month : Int = Integer.parseInt(input);
                            if (month <= 12) {
                                mExpiryDate.setText(mExpiryDate.getText().toString() + "/");
                            }
                        }else if (s?.length == 2 && lastInput.endsWith("/")) {
                            var month : Int = Integer.parseInt(input);
                            if (month <= 12) {
                                mExpiryDate.setText(mExpiryDate.getText().toString().substring(0,1))
                            }
                        }
                        lastInput = mExpiryDate.getText().toString();
                        //because not valid so code exits here
                        return;
                    }
                    // expiryDateDate has a valid date from the user
                    // Do something with expiryDateDate here
                }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })*/

    }

    fun getCardTypeImageId(cardType: CardType): String {
        return when (cardType) {
            CardType.UNKNOWN ->
                "UNKNOWN"

            CardType.DINERS_CLUB ->
                "DINERS_CLUB"

            CardType.DISCOVER ->
                "DISCOVER"

            CardType.AMERICAN_EXPRESS ->
                "AMERICAN_EXPRESS"

            CardType.MASTERCARD ->
                "MASTERCARD"

            CardType.VISA ->
                "VISA"

            CardType.JCB ->
                "JCB"

            CardType.MAESTRO ->
                "MAESTRO"
        }
    }

}
