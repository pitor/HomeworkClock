package clock2x.apps.a04.dk.homeworkclock

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var timer1 : TimerView
    private lateinit var timer2 : TimerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timer1 = findViewById(R.id.timer1)
        timer2 = findViewById(R.id.timer2)
    }

    override  fun onResume() {
        super.onResume()
        timer1.Start(this)

    }
}
