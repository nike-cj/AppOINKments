package it.appoinkments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import it.appoinkments.data.AppDatabase
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    //______________________________________________________________________________________________
    // attributes
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
    private lateinit var app_context : Context
    private var last_notification: Calendar? = null


    //______________________________________________________________________________________________
    // system callbacks
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
    override fun onReceive(context: Context?, intent: Intent?) {
        // check if already notified today
        var today: Calendar = Calendar.getInstance()
        if (last_notification != null) {
            if (last_notification!!.get(Calendar.DATE) == today.get(Calendar.DATE)
                && last_notification!!.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && last_notification!!.get(Calendar.YEAR) == today.get(Calendar.YEAR)){
                return  // don't repeat notification
            }
        }
        last_notification = today    // set today for next iteration

        //retrieve context
        if (context != null) {
            app_context = context
        }

        // create notification channel
        createNotificationChannel()

        // retrieve data
        var db = AppDatabase.getAppDatabase(app_context)
        var date_init: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.DATE, -1)  // the day before
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }
        var date_end: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }
        var list = db.appointmentDao().findToDo(date_init.timeInMillis, date_end.timeInMillis)
//        if (list.isEmpty()) {
//            showNotification(app_context, "Buongiorno \uD83C\uDF79", "Non ci sono appOINKment per oggi. Prendi una sdraio e goditi il tuo cocktail.")
//            return
//        }

        // forge notification message
        var text: String = ""
        var title: String = ""
        var description: String = ""
        when (list.size) {
            0 -> {
                title = "Buongiorno \uD83C\uDF79"
                text = "Non hai alcun appuntamento"
                description = "Non ci sono appOINKment per oggi. Prendi una sdraio e goditi il tuo cocktail."
            }
            1 -> {
                title = "Hai un appOINKment oggi \uD83D\uDC37"
                text = "Hai un appuntamento"
                description = "L'allevatore " + list[0].farmer + " ti sta aspettando"
            }
            else -> {
                title = "Hai più appOINKments oggi \uD83D\uDC37"
                text = "Hai " + list.size.toString() + " appuntamenti"
                description = "Ti stanno aspettando in molti, quindi smetti di cazzeggiare!"
            }
        }

        // send notification
        showNotification(app_context, title, text, description)
    }


    //______________________________________________________________________________________________
    // internal facilities
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = app_context?.getString(R.string.channel_name)
            val descriptionText = app_context?.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(app_context?.getString(R.string.channel_id), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                app_context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(context: Context, title: String, text: String, description: String) {
        // intent
        val intent = Intent(context, Activity_RecyclerAppointments::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // builder
        var builder = NotificationCompat.Builder(context, context.getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_pets)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(description))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // show notification
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, builder.build())
        }

    }

}