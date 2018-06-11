package clock2x.apps.a04.dk.homeworkclock

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageButton
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    private lateinit var timer1 : TimerView
    private lateinit var timer2 : TimerView
    private lateinit var arrowView: ImageView

    private lateinit var resetButton : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timer1 = findViewById(R.id.timer1)
        timer2 = findViewById(R.id.timer2)
        arrowView = findViewById(R.id.arrow)

        resetButton = findViewById(R.id.resetButton)

        timer1.setOnClickListener({ v -> run {
            if(timer1.isRunning) {
                timer1.Pause();
                rotateArrow(0f);

            }
            else {
                timer1.Start(this)
                timer2.Pause()
                rotateArrow(-90.0f)
            }
        } })

        timer2.setOnClickListener({ v -> run {
            if(timer2.isRunning) {
                timer2.Pause();
                rotateArrow(0f)
            }
            else {
                timer2.Start(this)
                timer1.Pause()
                rotateArrow(90f)
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

    override fun onResume() {
        super.onResume()
    }

    private var prevDeg : Float = 0f;
    private fun rotateArrow(deg: Float) {
        val anim =  RotateAnimation( prevDeg, deg,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = 300
        anim.fillAfter = true;

        prevDeg = deg

        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(a: Animation?) {

            }

            override fun onAnimationRepeat(a: Animation?) {

            }

            override fun onAnimationEnd(a: Animation?) {
                a!!.fillAfter = true
            }
        })

        arrowView.startAnimation(anim)

    }
}
