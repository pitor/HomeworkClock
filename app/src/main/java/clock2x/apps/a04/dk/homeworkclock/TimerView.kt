package clock2x.apps.a04.dk.homeworkclock

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.RelativeLayout
import java.util.*


class TimerView : RelativeLayout {

    private lateinit var secondsText : TextView
    private lateinit var minutesText : TextView
    private lateinit var hoursText : TextView
    private lateinit var colonLeft: TextView
    private lateinit var colonRight: TextView
    private lateinit var labelHours: TextView
    private lateinit var mainLabelView: TextView


    var isRunning = false
    var timer : Timer? = null
    var millis : Long = 0
    var startMillis : Long = 0;
    var startTimeStamp : Long = 0;

    var mainLabel : String
    get() {
        return mainLabelView.text.toString()
    }
    set(value) {
        mainLabelView.text = value
    }

    constructor(context: Context) : super(context, null) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView( context : Context ) {
        val inflater : LayoutInflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.timerview, this, true)

        hoursText = findViewById(R.id.timerview_hours)
        minutesText = findViewById(R.id.timerview_minutes)
        secondsText = findViewById(R.id.timerview_seconds)
        colonLeft = findViewById(R.id.timerview_colonleft)
        colonRight = findViewById(R.id.timerview_colonright)
        labelHours = findViewById(R.id.timerview_label_hours)
        mainLabelView = findViewById(R.id.timerview_mainlabel)

        millis = 0
        updateTextFields()

        forceLayout()
    }

    fun start(context: Activity) {


        startMillis = millis
        startTimeStamp = System.currentTimeMillis()
        isRunning = true;

        starttUpdaterTimer(context)
    }

    fun starttUpdaterTimer(context: Activity) {
        updateTextFields()
        if(!isRunning)
            return

        timer = Timer()

        timer!!.schedule(object : TimerTask() {
            override fun run() {
                millis = startMillis + System.currentTimeMillis() - startTimeStamp;
                context.runOnUiThread {
                    updateTextFields()
                }
            }
        }, 0, 100)
    }

    fun stopUIUpdateTimer() {
        if(timer != null)
            timer!!.cancel()
        updateTextFields()
    }

    fun pause() {
        stopUIUpdateTimer()
        timer = null;
        isRunning = false;
        updateTextFields()
    }

    fun reset() {
        if(isRunning)
            pause();
        millis = 0;
        updateTextFields()
    }

    private fun updateTextFields() {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / 60000 ) % 60
        val hours = (millis / (60 * 60 * 1000 ))

        if(millis >= 3600000) {
            hoursText.text = String.format("%2d", hours)
            minutesText.text = String.format("%2d", minutes)
            if(hoursText.visibility != View.VISIBLE) {
                hoursText.visibility = View.VISIBLE
                colonLeft.visibility = View.VISIBLE
                labelHours.visibility = View.VISIBLE
                requestLayout()
            }
        }
        else {
            if(hoursText.visibility != View.GONE) {
                hoursText.visibility = View.GONE
                colonLeft.visibility = View.GONE
                labelHours.visibility = View.GONE
                requestLayout()
            }
            minutesText.text = String.format("%2d", minutes)
        }

        secondsText.text = String.format("%02d", seconds)


    }

    val millisKey = "millis"
    val startmillisKey = "startMillis"
    val startTimeStampKey = "startTimeStamp"
    val isRunningKey = "isRunning"


    fun getState(): Bundle {
        var b =  Bundle();
        with(b) {
            putLong(millisKey, millis)
            putLong(startmillisKey, startMillis)
            putLong(startTimeStampKey, startTimeStamp)
            putBoolean(isRunningKey, isRunning)
        }
        return b
    }

    fun restoreFromState(bundle: Bundle?) {
        if (bundle != null) {
            millis = bundle.getLong(millisKey)
            startMillis = bundle.getLong(startmillisKey)
            startTimeStamp = bundle.getLong(startTimeStampKey)
            isRunning = bundle.getBoolean(isRunningKey)
        }
    }
}