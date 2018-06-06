package clock2x.apps.a04.dk.homeworkclock

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.RelativeLayout
import java.util.*


class TimerView : RelativeLayout {

    private lateinit var seconds : TextView
    private lateinit var minutes : TextView
    private lateinit var hours : TextView

    constructor(context: Context) : this(context, null) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView( context : Context ) {
        val inflater : LayoutInflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.timerview, this, true)

        hours = findViewById(R.id.timerview_hours)
        minutes = findViewById(R.id.timerview_minutes)
        seconds = findViewById(R.id.timerview_seconds)

        hours.text = "00"
        minutes.text = "00"
        seconds.text = "00"
    }

    fun Start(context: Activity) {
        var counter = 0;
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                counter = (counter + 1) % 100
                context.runOnUiThread {
                    seconds.text = counter.toString();
                    hours.text = counter.toString();
                    minutes.text = counter.toString();
                }
            }
        }, 0, 999)

    }

    public fun Stop() {

    }
}