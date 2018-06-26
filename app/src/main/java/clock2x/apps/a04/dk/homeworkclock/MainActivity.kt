package clock2x.apps.a04.dk.homeworkclock

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import kotlinx.android.synthetic.main.activity_main.*
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.support.v4.app.NotificationManagerCompat
import android.app.PendingIntent
import android.content.Intent







class MainActivity : AppCompatActivity() {


    private var crCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crCount++
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        timer1.setOnClickListener { v -> run {
            if(timer1.isRunning) {
                timer1.Pause();
                rotateArrow(0f);

            }
            else {
                timer1.Start(this)
                timer2.Pause()
                rotateArrow(-90.0f)
            }
        } }

        timer2.setOnClickListener { v -> run {
            if(timer2.isRunning) {
                timer2.Pause();
                rotateArrow(0f)
            }
            else {
                timer2.Start(this)
                timer1.Pause()
                rotateArrow(90f)
            }
        } }

        resetButton.setOnClickListener {
            v -> run {
            if(!timer1.isRunning && !timer2.isRunning)
                return@run
            var builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.resetDialogTitle)
            builder.setMessage(R.string.resetDialogMessage)

            builder.setPositiveButton(R.string.resetDialogConfirm) { dialogInterface, i -> run {
                timer1.Reset()
                timer2.Reset()
                dialogInterface.dismiss()
            }}
            builder.setNegativeButton(R.string.resetDialogCancel, {dialogInterface, i -> dialogInterface.dismiss()})
            builder.create().show()
        }
        }
    }

    override fun onResume() {
        super.onResume()
        clearNotification()
    }

    override fun onPause() {
        super.onPause()
        if(timer1.isRunning || timer2.isRunning) {
            showNotification()
        }
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

        arrow.startAnimation(anim)
    }

    val CHANNEL_ID = "homeworkclockchannel"

    private fun showNotification() {

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val title = getString(R.string.notification_title);
        val message = getString(R.string.notification_message);

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.abc_ic_arrow_drop_right_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val noget = builder.build()

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(111, noget)
    }

    private fun clearNotification() {
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.cancel(111)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
    }
}
