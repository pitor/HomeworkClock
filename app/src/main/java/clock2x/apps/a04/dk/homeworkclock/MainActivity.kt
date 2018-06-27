package clock2x.apps.a04.dk.homeworkclock

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AlertDialog
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import kotlinx.android.synthetic.main.activity_main.*
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.support.v4.app.NotificationManagerCompat
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Vibrator
import java.text.MessageFormat
import android.R.attr.data
import android.util.Base64
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {


    private var crCount = 0
    private val personName = "Linus";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crCount++
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        if(savedInstanceState == null) {
            restoreFromSharedPrefs()
        }

        timer1.mainLabel = MessageFormat.format(getString(R.string.is_working), personName)

        timer1.setOnClickListener { v -> run {
            if(timer1.isRunning) {
                timer1.pause();
                rotateArrow(0f);

            }
            else {
                timer1.start(this)
                timer2.pause()
                rotateArrow(-90.0f)
            }
        } }

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        timer2.mainLabel = MessageFormat.format(getString(R.string.is_wasting_time), personName)

        timer2.setOnClickListener { v -> run {
            if(timer2.isRunning) {
                timer2.pause()
                rotateArrow(0f)
            }
            else {
                timer2.start(this)
                timer1.pause()
                rotateArrow(90f)
            }
            vibrator.vibrate(100)
        } }

        resetButton.setOnClickListener {
            v -> run {
            if(!timer1.isRunning && !timer2.isRunning && timer1.millis == 0L && timer2.millis == 0L)
                return@run
            var builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.resetDialogTitle)
            builder.setMessage(R.string.resetDialogMessage)

            builder.setPositiveButton(R.string.resetDialogConfirm) { dialogInterface, i -> run {
                timer1.reset()
                timer2.reset()
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
        timer1.starttUpdaterTimer(this)
        timer2.starttUpdaterTimer(this)
    }

    override fun onPause() {
        super.onPause()
        if(timer1.isRunning || timer2.isRunning) {
            showNotification()
        }
        timer1.stopUIUpdateTimer()
        timer2.stopUIUpdateTimer()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val timer1State = timer1.getState()
        val timer2State = timer2.getState()

        outState?.putBundle("timer1", timer1State)
        outState?.putBundle("timer2", timer2State)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        timer1.restoreFromState(savedInstanceState?.getBundle("timer1"))
        timer2.restoreFromState(savedInstanceState?.getBundle("timer2"))
    }


    override fun onBackPressed() {
        saveStateToSharedPrefs()
        super.onBackPressed()
    }

    private fun saveStateToSharedPrefs() {
        var prefs = getPreferences(Context.MODE_PRIVATE);
        val timer1State = bundleToBase64(timer1.getState())
        val timer2State = bundleToBase64(timer2.getState())

        var editor = prefs.edit()
        editor.putString("timer1", timer1State)
        editor.putString("timer2", timer2State)
        editor.commit()

    }

    private fun restoreFromSharedPrefs() {
        var prefs = getPreferences(Context.MODE_PRIVATE);
        val timer1State = prefs.getString("timer1", null)
        val timer2State = prefs.getString("timer2", null)

        var bundle1 = base64ToBundle(timer1State)
        var bundle2 = base64ToBundle(timer2State)

        timer1.restoreFromState(bundle1)
        timer2.restoreFromState(bundle2)


    }

    private fun bundleToBase64(bundle: Bundle) : String {
        var p = Parcel.obtain()
        p.writeBundle(bundle)
        var bytes = p.marshall();

        val base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        return base64;
    }

    private fun base64ToBundle(base64: String) :Bundle {
        var bytes = Base64.decode(base64, Base64.DEFAULT)
        var p = Parcel.obtain()
        p.unmarshall(bytes, 0, bytes.size)
        p.setDataPosition(0)
        var bundle = p.readBundle()
        var keys = bundle.keySet()
        return bundle
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
                .setSmallIcon(R.drawable.ic_notification)
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
