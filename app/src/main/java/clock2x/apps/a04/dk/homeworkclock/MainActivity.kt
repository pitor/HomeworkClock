package clock2x.apps.a04.dk.homeworkclock

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    private lateinit var timer1 : TimerView
    private lateinit var timer2 : TimerView

    private lateinit var resetButton : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timer1 = findViewById(R.id.timer1)
        timer2 = findViewById(R.id.timer2)

        resetButton = findViewById(R.id.resetButton)

        timer1.setOnClickListener({ v -> run {
            if(timer1.isRunning) {
                timer1.Pause();
            }
            else {
                timer1.Start(this)
                timer2.Pause()
            }
        } })

        timer2.setOnClickListener({ v -> run {
            if(timer2.isRunning) {
                timer2.Pause();
            }
            else {
                timer2.Start(this)
                timer1.Pause()
            }
        } })

        resetButton.setOnClickListener({
            v -> run {
                if(!timer1.isRunning && !timer2.isRunning)
                    return@run
                var builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.resetDialogTitle)
                builder.setMessage(R.string.resetDialogMessage)
                builder.setPositiveButton(R.string.resetDialogConfirm, {dialogInterface, i -> run {
                    timer1.Reset()
                    timer2.Reset()
                    dialogInterface.dismiss()
                }})
                builder.setNegativeButton(R.string.resetDialogCancel, {dialogInterface, i -> dialogInterface.dismiss()})
                builder.create().show()
            }
        })
    }

    override  fun onResume() {
        super.onResume()
    }
}
