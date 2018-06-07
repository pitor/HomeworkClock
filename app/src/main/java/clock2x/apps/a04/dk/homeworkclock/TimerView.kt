package clock2x.apps.a04.dk.homeworkclock

import android.app.Activity
import android.content.Context
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

    var isRunning = false
    var timer : Timer? = null
    var millis : Long = 0

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

        hoursText.text = "00"
        minutesText.text = "00"
        secondsText.text = "00"
        forceLayout()
    }

    fun Start(context: Activity) {
        timer = Timer()

        val startMillis = millis
        val startTime = System.currentTimeMillis()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                millis = startMillis + System.currentTimeMillis() - startTime;
                context.runOnUiThread {
                   updateTextFields()
                }
            }
        }, 0, 100)

        isRunning = true;
    }

    fun Pause() {
        if(timer != null)
            timer!!.cancel()
        timer = null;
        isRunning = false;
    }

    fun Reset() {
        millis = 0;
    }

    private fun updateTextFields() {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / 60000 ) % 60
        val hours = (millis / (60 * 60 * 1000 ))

        secondsText.text = String.format("%02d", seconds)
        hoursText.text = String.format("%02d", hours)
        minutesText.text = String.format("%02d", minutes)
    }
}