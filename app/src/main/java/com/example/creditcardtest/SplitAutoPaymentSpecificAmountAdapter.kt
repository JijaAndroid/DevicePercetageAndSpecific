package com.example.creditcardtest

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView


/**
 * Created by Ravi Suryawanshi on 22,May,2019
RP APP 4.0 - Xento,Pune.
 */

class SplitAutoPaymentSpecificAmountAdapter(
    var cContext: Context,
    var mArrRoomMate: ArrayList<RoommateModel>,
    var mRecyclerView: RecyclerView?,
    var estimatedAmount: Double,
    nothing1: Nothing?
) :
    RecyclerView.Adapter<SplitAutoPaymentSpecificAmountAdapter.ViewHolder>() {

    var estimatedCharges: Double? = 100.00
    var totalAmount: Double? = 00.00
    var eachRoomMateShare: String? = null
    var remainingShare: Double? = null
    var mView: View? = null

    init {
        estimatedCharges = estimatedAmount
        calculateInitialShare()
    }

    private fun calculateInitialShare() {

        totalAmount = estimatedCharges

        if (mArrRoomMate == null)
            return

        eachRoomMateShare =
            totalAmount?.div(mArrRoomMate.size)?.let { getFormattedStringUpToTwoDecimalAndAppendDollarSign(it) }

        for (record in mArrRoomMate) {
            record.specificShare = eachRoomMateShare
        }
    }

    private fun reCalculateShare(
        incrementDecrementValue: Int?,
        recordRoomMate: RoommateModel,
        isCustomAmountEntered: Boolean,
        dblCustomAmount: Double?
    ) {
        if (isCustomAmountEntered) {
            if (dblCustomAmount != null) {
                if (dblCustomAmount > this!!.totalAmount!!) {
                    calculateRemainingShare(recordRoomMate, totalAmount, null)
                    return
                } else {
                    calculateRemainingShare(recordRoomMate, dblCustomAmount, null)
                    return
                }
            }
        } else {
            calculateRemainingShare(recordRoomMate, null, incrementDecrementValue)
        }
        mRecyclerView?.post(Runnable { notifyDataSetChanged() })

    }


    fun calculateRemainingShare(
        recordRoomMate: RoommateModel?,
        dblCustomAmount: Double?,
        incrementDecrementValue: Int?
    ) {

        if (getRemainingUnlockRoomMateSize() + 1 == 1) {
            recordRoomMate?.specificShare = recordRoomMate?.specificShare
            mRecyclerView?.post(Runnable { notifyDataSetChanged() })
            return
        }


        // in case of custom amount
        if (dblCustomAmount != null) {

            recordRoomMate?.specificShare = getFormattedStringUpToTwoDecimalAndAppendDollarSign(dblCustomAmount)
            var dblSelectedRoomMateShare = getFormattedDoubleValue(recordRoomMate?.specificShare!!)
            remainingShare = totalAmount?.minus(dblSelectedRoomMateShare)

            var remainingEqualShare = remainingShare?.div(getRemainingUnlockRoomMateSize())

            for (record in mArrRoomMate) {
                if (record != recordRoomMate) {
                    if (!record.isLock) {
                        record.specificShare =
                            remainingEqualShare?.let { getFormattedStringUpToTwoDecimalAndAppendDollarSign(it) }
                    }

                }
            }

            mRecyclerView?.post(Runnable { notifyDataSetChanged() })

        } else {                                                                                                             // in case of increment and decrement amount
            var dblSelectedRoomMateShare = recordRoomMate?.specificShare?.let { getFormattedDoubleValue(it) }

            recordRoomMate?.specificShare =
                incrementDecrementValue?.let {
                    dblSelectedRoomMateShare?.plus(it)?.let { share ->
                        getFormattedStringUpToTwoDecimalAndAppendDollarSign(
                            share
                        )
                    }
                }

            remainingShare =
                recordRoomMate?.specificShare?.let { getFormattedDoubleValue(it)?.let { totalAmount?.minus(it) } }


            var remainingEqualShare = remainingShare?.div(getRemainingUnlockRoomMateSize())

            for (roommateModel in mArrRoomMate) {
                if (roommateModel != recordRoomMate) {
                    if (!roommateModel.isLock) {
                        roommateModel.specificShare =
                            remainingEqualShare?.let { getFormattedStringUpToTwoDecimalAndAppendDollarSign(it) }
                    }
                }
            }

            if (recordRoomMate?.specificShare?.let { getFormattedDoubleValue(it) }!! > this!!.totalAmount!!) {
                recordRoomMate?.specificShare =
                    totalAmount?.let { getFormattedStringUpToTwoDecimalAndAppendDollarSign(it) }
                calculateRemainingShare(recordRoomMate, totalAmount, null)
            } else if (recordRoomMate?.specificShare?.let { getFormattedDoubleValue(it) }!! < 0) {
                recordRoomMate?.specificShare = getFormattedStringUpToTwoDecimalAndAppendDollarSign(0.00)
            }

            mRecyclerView?.post(Runnable { notifyDataSetChanged() })

        }

    }

    /* get count of unlock room mates to distribute remaining amount between them*/
    private fun getRemainingUnlockRoomMateSize(): Int {
        var count = 0;
        for (roommateModel in mArrRoomMate) {
            if (!roommateModel.isLock) {
                count += 1
            }
        }
        if (count == 0) {
            return mArrRoomMate.size.minus(1)
        } else {
            return count - 1
        }
    }


    private fun getFormattedDoubleValue(strInput: String): Double {
        return strInput.replace("$", "").toDouble()
    }

    private fun getFormattedStringUpToTwoDecimalAndAppendDollarSign(strInput: Double): String {
        return "$" + String.format("%.2f", strInput)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mView = LayoutInflater.from(cContext).inflate(R.layout.item_split, parent, false)
        return ViewHolder(cContext, mView)
    }

    override fun getItemCount(): Int {
        return mArrRoomMate.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.edtSplitSpecificAmount?.setText(mArrRoomMate[position].specificShare.toString())

        holder.imgPlus?.setOnClickListener {
            onPlusClicked(mArrRoomMate[position])
        }
        holder?.imgMinus?.setOnClickListener {
            onMinusClicked(mArrRoomMate[position])
        }
        holder?.imgLockUnLock?.setOnClickListener {
            onLockUnlockClick(mArrRoomMate[position], holder?.imgLockUnLock, holder.edtSplitSpecificAmount)
        }

        holder?.edtSplitSpecificAmount?.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                // hideKeyboard(holder?.edtSplitSpecificAmount!!)
                var dblCustomAmount: Double = holder.edtSplitSpecificAmount?.text.toString().toDouble()
                reCalculateShare(null, mArrRoomMate[position], true, dblCustomAmount)
            } else {
                specific.editText = holder?.edtSplitSpecificAmount!!
                var strCurrentText: String = holder?.edtSplitSpecificAmount?.text.toString()
                holder?.edtSplitSpecificAmount?.setText(strCurrentText.replace("$", ""))
            }
        }
    }

    class ViewHolder(private var mContext: Context, mView: View?) : RecyclerView.ViewHolder(mView!!) {

        var imgLockUnLock: ImageView? = null
        var imgMinus: ImageView? = null
        var imgPlus: ImageView? = null
        var edtSplitSpecificAmount: EditText? = null
        var txtRoomMateName: TextView? = null
        var txtRoomMateAmount: TextView? = null

        init {
            imgLockUnLock = mView?.findViewById(R.id.imgLockUnLock)
            imgMinus = mView?.findViewById(R.id.imgMinus)
            imgPlus = mView?.findViewById(R.id.imgPlus)
            edtSplitSpecificAmount = mView?.findViewById(R.id.edtSplitSpecificAmount)
            txtRoomMateName = mView?.findViewById(R.id.txtRoomMateName)
            //txtRoomMateAmount = mView?.findViewById(R.id.txtRoomMateAmount)
        }

    }

    private fun onPlusClicked(recordRoomMate: RoommateModel) {
        reCalculateShare(1, recordRoomMate, false, null)
    }

    private fun onMinusClicked(recordRoomMate: RoommateModel) {
        reCalculateShare(-1, recordRoomMate, false, null)
    }

    private fun onLockUnlockClick(
        recordRoomMate: RoommateModel,
        imgLockUnLock: ImageView?,
        edtSplitSpecificAmount: EditText?
    ) {

        if (recordRoomMate.isLock) {
            edtSplitSpecificAmount?.isFocusable = true
            edtSplitSpecificAmount?.isEnabled = true
            edtSplitSpecificAmount?.isFocusableInTouchMode = true
            recordRoomMate.isLock = false
            imgLockUnLock?.setImageDrawable(cContext.resources.getDrawable(R.drawable.unlock))
        } else {
            edtSplitSpecificAmount?.isFocusable = false
            edtSplitSpecificAmount?.isEnabled = false
            edtSplitSpecificAmount?.isFocusableInTouchMode = false
            recordRoomMate.isLock = true
            imgLockUnLock?.setImageDrawable(cContext.resources.getDrawable(R.drawable.lock))

        }

        reCalulateTotalForDistribution()

    }


    /* If room mates are lock then get count of unlock room mates do summation of their share and treat this as a new total amount*/

    fun reCalulateTotalForDistribution() {

        var count = 0
        var summation = 0.00

        for (roommateModel in mArrRoomMate) {
            if (!roommateModel.isLock) {
                count += 1
                summation = summation.plus(getFormattedDoubleValue(roommateModel.specificShare!!))
            }
        }

        if (summation > 0) {
            totalAmount = summation  //estimatedCharges?.minus(summation)
        } else {
            totalAmount = estimatedCharges
        }
    }

    private fun generatePercentage() {

    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = cContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
