package com.example.creditcardtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.percentage.*
import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.view.MotionEvent
import kotlinx.android.synthetic.main.specific.*


class specific : AppCompatActivity() {

    companion object {
        lateinit var editText: EditText
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.specific)
        //setAdapterToSpecific()

        btnResetNew.setOnClickListener {
            setAdapterToSpecific()
        }
    }


    private fun setAdapterToSpecific() {

        val layoutManagerSpecificAmount = LinearLayoutManager(this)
        layoutManagerSpecificAmount.orientation = LinearLayoutManager.VERTICAL
        rvSpecific.layoutManager = layoutManagerSpecificAmount
        rvSpecific.itemAnimator = DefaultItemAnimator()

        var count = ""
        var amount = "300.50"

        count = edt1People.text.toString()
        amount = edt1Amount.text.toString()

        var mArrRoomMates: ArrayList<RoommateModel> = ArrayList()
        mArrRoomMates.clear()

        if (TextUtils.isEmpty(count)) {

            var rec1 = RoommateModel()
            rec1.priority = 0
            var rec2 = RoommateModel()
            rec2.priority = 1
            var rec3 = RoommateModel()
            rec3.priority = 2


            mArrRoomMates.add(rec1)
            mArrRoomMates.add(rec2)
            mArrRoomMates.add(rec3)
        } else {
            for (i in 1..count.toInt()) {
                var rec = RoommateModel()
                mArrRoomMates.add(rec)

            }

        }

        if (TextUtils.isEmpty(amount))
            amount = "300.50"


        rvSpecific.adapter =
            SplitAutoPaymentSpecificAmountAdapter(this, mArrRoomMates, rvSpecific, amount.toDouble(), null)

    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val v = currentFocus

        if (v != null &&
            (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
            v is EditText &&
            !v.javaClass.name.startsWith("android.webkit.")
        ) {
            val scrcoords = IntArray(2)
            v.getLocationOnScreen(scrcoords)
            val x = ev.rawX + v.left - scrcoords[0]
            val y = ev.rawY + v.top - scrcoords[1]

            if (x < v.left || x > v.right || y < v.top || y > v.bottom)
                hideKeyboard(this)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun hideKeyboard(activity: Activity?) {

        if (editText != null) {

            if (editText == edt1People || editText == edt1Amount) {

            } else {
                editText.clearFocus()
            }

        }


        if (activity != null && activity.window != null && activity.window.decorView != null) {
            val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }
    }

}