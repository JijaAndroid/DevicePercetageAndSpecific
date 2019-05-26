package com.example.creditcardtest

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView


/**
 * Created by Ravi Suryawanshi on 22,May,2019
RP APP 4.0 - Xento,Pune.
 */

class SplitAutoPaymentPercentageAdapter(
    var cContext: Context,
    var mArrRoomMate: ArrayList<RoommateModel>,
    var mRecyclerView: RecyclerView?,
    var estimatedAmount: Double,
    nothing1: Nothing?
) :
    RecyclerView.Adapter<SplitAutoPaymentPercentageAdapter.ViewHolder>() {

    var mTotalPercetageCopy: Int? = 100
    var mEstimatedChargesCopy: Double? = 1350.00
    var estimatedCharges: Double? = 1350.00
    var totalPercetage: Int? = 100
    var eachRoomMatePercentage: Int? = null
    var remainingPercentageOfRestRoomMates: Int? = null
    var mView: View? = null

    init {
        estimatedCharges = estimatedAmount
        mEstimatedChargesCopy = estimatedAmount
        calculateInitialShare()
    }

    private fun calculateInitialShare() {

        if (mArrRoomMate == null)
            return

        eachRoomMatePercentage = totalPercetage?.div(mArrRoomMate.size)                                  //100/3  = 33

        var reminder = totalPercetage?.minus(eachRoomMatePercentage?.times(mArrRoomMate.size)!!)        //100-(33*3) = 1

        for ((index, record) in mArrRoomMate.withIndex()) {
            if (index == 0) {
                record.percentageShare = eachRoomMatePercentage?.plus(reminder!!)
            } else {
                record.percentageShare = eachRoomMatePercentage
            }
        }
        calculateAndDistributeAmountAmongRoomMate()
    }

    private fun assignPercetageAccordingToPriority(
        remainingPercentageToDistribute: Int?,                                                      // 32
        recordRoomMate: RoommateModel?,
        remainingPercentage: Int?
    ) {

        var reminder =
            remainingPercentage?.minus(remainingPercentageToDistribute?.times(getRemainingUnlockRoomMateSize())!!)  //65-(32*2)

        var isReminderAssigned = false

        for ((index, record) in mArrRoomMate.withIndex()) {
            if (record != recordRoomMate) {
                if (!record?.isLock!!) {
                    if (!isReminderAssigned) {
                        record.percentageShare = remainingPercentageToDistribute?.plus(reminder!!)
                        isReminderAssigned = true
                    } else {
                        record.percentageShare = remainingPercentageToDistribute
                    }
                }
            }
        }
    }


    /* As we have calculated percentage of each room mate so now assign amount to each room mate according to percentage
    * only for those room mate those are un locked*/
    fun calculateAndDistributeAmountAmongRoomMate() {
        for ((index, record) in mArrRoomMate.withIndex()) {
            if (!record.isLock) {
                var eachRoomMateAmount: Double =
                    (record.percentageShare?.toDouble()?.div(this!!.totalPercetage!!))?.times(this!!.estimatedCharges!!)!!

                record.strAmount = String.format("%.2f", eachRoomMateAmount)
            }

        }
    }


    private fun reCalculateShare(
        incrementDecrementValue: Int?,
        recordRoomMate: RoommateModel,
        isCustomAmountEntered: Boolean,
        intCustomPercentage: Int?
    ) {
        if (isCustomAmountEntered) {
            if (intCustomPercentage != null) {
                if (intCustomPercentage > this!!.totalPercetage!!) {
                    calculateRemainingShare(recordRoomMate, totalPercetage, null)
                    return
                } else if (intCustomPercentage < 0) {
                    calculateRemainingShare(recordRoomMate, 0, null)
                    return
                } else {
                    calculateRemainingShare(recordRoomMate, intCustomPercentage, null)
                    return
                }
            }
        } else {
            calculateRemainingShare(recordRoomMate, null, incrementDecrementValue)
        }

        calculateAndDistributeAmountAmongRoomMate()
        mRecyclerView?.post(Runnable { notifyDataSetChanged() })

    }


    fun calculateRemainingShare(
        recordRoomMate: RoommateModel?,
        intCustomPercentage: Int?,
        incrementDecrementValue: Int?
    ) {

        if (getRemainingUnlockRoomMateSize() + 1 == 1) {
            recordRoomMate?.percentageShare = recordRoomMate?.percentageShare
            mRecyclerView?.post(Runnable { notifyDataSetChanged() })
            return
        }

        // in case of custom percentage
        if (intCustomPercentage != null) {

            // in case of increment and decrement amount

            recordRoomMate?.percentageShare = intCustomPercentage                                  // custom - 50

            remainingPercentageOfRestRoomMates =
                totalPercetage?.minus(recordRoomMate?.percentageShare!!)                         //100-50 = 50

            var remainingEquallyDistributedPercentage =
                remainingPercentageOfRestRoomMates?.div(getRemainingUnlockRoomMateSize())                        //50/2 = 25

            assignPercetageAccordingToPriority(
                remainingEquallyDistributedPercentage,
                recordRoomMate,
                remainingPercentageOfRestRoomMates
            )                                                                                   //25,record,50

            calculateAndDistributeAmountAmongRoomMate()
            mRecyclerView?.post(Runnable { notifyDataSetChanged() })

        } else {
// in case of increment and decrement amount
            var intSelectedRoomMatePercentage = recordRoomMate?.percentageShare                 //34

            recordRoomMate?.percentageShare =
                intSelectedRoomMatePercentage?.plus(incrementDecrementValue!!)                   //34+1 = 35

            remainingPercentageOfRestRoomMates =
                totalPercetage?.minus(recordRoomMate?.percentageShare!!)                         //100-35 = 65

            var remainingDistributedPercentage =
                remainingPercentageOfRestRoomMates?.div(getRemainingUnlockRoomMateSize())                        //65/2 = 32

            assignPercetageAccordingToPriority(
                remainingDistributedPercentage,
                recordRoomMate,
                remainingPercentageOfRestRoomMates
            )    //32  record

            if (recordRoomMate?.percentageShare!! > this!!.totalPercetage!!) {
                recordRoomMate.percentageShare = totalPercetage
                calculateRemainingShare(recordRoomMate, recordRoomMate.percentageShare, null)
            } else if (recordRoomMate?.percentageShare!! < 0) {
                recordRoomMate.percentageShare = 0
                calculateRemainingShare(recordRoomMate, recordRoomMate.percentageShare, null)
            }

            calculateAndDistributeAmountAmongRoomMate()
            mRecyclerView?.post(Runnable { notifyDataSetChanged() })

        }

    }

    /* get count of unlock room mates to distribute remaining percentage between them*/
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

    /* get count of unlock room mates to */
    private fun getUnLockRoomMateCount(): Int {
        var count = 0;
        for (roommateModel in mArrRoomMate) {
            if (!roommateModel.isLock) {
                count += 1
            }
        }
        return count
    }


    private fun getFormattedDoubleValue(strInput: String): Double {
        return strInput.replace("$", "").toDouble()
    }

    private fun getFormattedStringUpToTwoDecimalAndAppendDollarSign(strInput: Double): String {
        return "$" + String.format("%.2f", strInput)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mView = LayoutInflater.from(cContext).inflate(R.layout.item_percentage, parent, false)
        return ViewHolder(cContext, mView)
    }

    override fun getItemCount(): Int {
        return mArrRoomMate.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.edtSplitPercentage?.setText(mArrRoomMate[position].percentageShare.toString())
        holder.txtRoomMateAmount?.text = mArrRoomMate[position].strAmount.toString()

        holder.imgPlus?.setOnClickListener {
            onPlusClicked(mArrRoomMate[position])
        }
        holder?.imgMinus?.setOnClickListener {
            onMinusClicked(mArrRoomMate[position])
        }
        holder?.imgLockUnLock?.setOnClickListener {
            onLockUnlockClick(mArrRoomMate[position], holder?.imgLockUnLock, holder.edtSplitPercentage)
        }

        holder?.edtSplitPercentage?.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                var intCustomPercentage = 0

                var strEnteredPercentage =
                    holder.edtSplitPercentage?.text.toString()

                if (!TextUtils.isEmpty(strEnteredPercentage)) {
                    intCustomPercentage = strEnteredPercentage.toInt()
                }

                reCalculateShare(null, mArrRoomMate[position], true, intCustomPercentage)
            } else {
                specific.editText = holder?.edtSplitPercentage!!
                var strCurrentText: String = holder?.edtSplitPercentage?.text.toString()
                holder?.edtSplitPercentage?.setText(strCurrentText.replace("%", ""))
            }
        }
    }

    class ViewHolder(private var mContext: Context, mView: View?) : RecyclerView.ViewHolder(mView!!) {

        var imgLockUnLock: ImageView? = null
        var imgMinus: ImageView? = null
        var imgPlus: ImageView? = null
        var edtSplitPercentage: EditText? = null
        var txtRoomMateName: TextView? = null
        var txtRoomMateAmount: TextView? = null


        init {
            imgLockUnLock = mView?.findViewById(R.id.imgLockUnLock)
            imgMinus = mView?.findViewById(R.id.imgMinus)
            imgPlus = mView?.findViewById(R.id.imgPlus)
            edtSplitPercentage = mView?.findViewById(R.id.edtSplitPercentage)
            txtRoomMateName = mView?.findViewById(R.id.txtRoomMateName)
            txtRoomMateAmount = mView?.findViewById(R.id.txtRoomMateAmount)
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

        reCalulatePercentageForDistribution()

    }


    /* If room mates are lock then get count of unlock room mates do summation of their percentage and treat this as a new total amount and percentage*/

    fun reCalulatePercentageForDistribution() {

        var summationOfPercentage = 0

        for (roommateModel in mArrRoomMate) {
            if (!roommateModel.isLock) {
                summationOfPercentage =
                    summationOfPercentage.plus(roommateModel.percentageShare!!)  //  consider 2 are unlocked - summation  =  33+33 = 66
            }
        }

        /* If unlock count is same as array list size then total percetage shoiuld be 100 and total estimatates changes should be origional amouunt
        * if not then summation of percetage should be now total percetage and estimated charges should be calculated based on new  new summation percetage on origional estimated
        * amount
        *
        * Room mate 1 = 34 - Locked
        * Room mate 2  = 33 - UnLocked
        * Room mate 3  = 33 - UnLocked
        * Summation = 33+33 = 66%
        * * */

        if (getUnLockRoomMateCount() == mArrRoomMate.size) {
            totalPercetage = mTotalPercetageCopy
            estimatedCharges = mEstimatedChargesCopy
        } else {
            totalPercetage = summationOfPercentage
            /* We have to convert summationPercentage to double because if we dont it will always return o after division by 100
            as it is int but we need double value to calculate new estimated charges (exp (300.00) * (60/100 = 0.6) )*/
            estimatedCharges = mEstimatedChargesCopy?.times(summationOfPercentage.toDouble().div(100))
        }

    }


}
