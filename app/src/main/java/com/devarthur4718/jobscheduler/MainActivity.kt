package com.devarthur4718.jobscheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mScheduler: JobScheduler? = null

    companion object {
        const val JOB_ID = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViews()
    }

    private fun setViews() {
        btnScheduleJob.setOnClickListener {

            var seekBarSet = seekbar.progress > 0

            var selectedNetworkOptions = when (rbNetworkOptions.checkedRadioButtonId) {
                R.id.rbNoNetwork -> JobInfo.NETWORK_TYPE_NONE
                R.id.rbAnyNetwork -> JobInfo.NETWORK_TYPE_ANY
                R.id.rbwifiNetwork -> JobInfo.NETWORK_TYPE_UNMETERED
                else -> JobInfo.NETWORK_TYPE_NONE
            }
            mScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val serviceName = ComponentName(
                packageName,
                NotificationJobService::class.java.name
            )

            var constraintSet = selectedNetworkOptions != JobInfo.NETWORK_TYPE_NONE
                    || mDeviceChargingSwitch.isChecked || mDeviceIdleSwitch.isChecked

            if(constraintSet){
                val myJobInfo = JobInfo.Builder(JOB_ID, serviceName).apply {
                    setRequiredNetworkType(selectedNetworkOptions)
                    setRequiresDeviceIdle(mDeviceIdleSwitch.isChecked)
                    setRequiresCharging(mDeviceChargingSwitch.isChecked)
                    if(seekBarSet) setOverrideDeadline(seekbar.progress * 1000L)
                }.build()


                mScheduler?.schedule(myJobInfo)
                it.context.showToast("Job Scheduled, job will run when \n the constraints are met.")

            }else{
                it.context.showToast("Please set at least one Constraint")
            }



        }

        btnCancel.setOnClickListener {
            mScheduler?.let {
                it.cancelAll()
                mScheduler = null
                applicationContext.showToast("Jobs cancelled")
            }
        }

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                seekBarProgress.text = "Override Deadline: ${if(progress > 0) progress else "Not Set"}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })


    }


}
