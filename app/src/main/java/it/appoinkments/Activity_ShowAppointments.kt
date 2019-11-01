package it.appoinkments

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.appoinkments.data.AppointmentViewModel
import it.appoinkments.data.ViewModelFactory
import kotlinx.android.synthetic.main.activity_show_appointments.*
import android.widget.Toast
import android.os.SystemClock
import androidx.recyclerview.widget.ItemTouchHelper
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    //______________________________________________________________________________________________
    // attributes
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val appointmentViewModel: AppointmentViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(this.application)).get(AppointmentViewModel::class.java)
    }

    var alarmMgr : AlarmManager? = null
    lateinit var alarmIntent : PendingIntent


    //______________________________________________________________________________________________
    // system callbacks
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_appointments)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "AppOINKments \uD83D\uDC37"

        // create notification channel
        createNotificationChannel()

        // create recycler view
        viewManager = LinearLayoutManager(this)
        viewAdapter = Adapter_ShowAppointments(appointmentViewModel.getNotCompleted(), applicationContext)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        // enable card swiping
        val touchHandler = ItemTouchHelper(SwipeHandler(viewAdapter as Adapter_ShowAppointments, 0, (ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)))
        touchHandler.attachToRecyclerView(recyclerView)

        // assign action to + button
        fab.setOnClickListener { view ->
            val intent = Intent(this, Activity_NewLoad::class.java)
            startActivity(intent)
            finish()
        }

        //----- set timer for notification -----
        alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent : Intent = Intent(applicationContext, AlarmReceiver::class.java)
        alarmIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, 0);

        var calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())
        calendar.set(Calendar.HOUR_OF_DAY, 14)
        calendar.set(Calendar.MINUTE, 0)
        // setRepeating() lets you specify a precise custom interval--in this case, 1 day
        alarmMgr?.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
            AlarmManager.INTERVAL_HALF_HOUR,
            alarmIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.farmers -> {
                //val intent = Intent(this, MainActivity::class.java)
                //startActivity(intent)
                //finish()
                //TODO create list of farmer

                showNotification()
                Toast.makeText(
                    applicationContext,
                    "Sent fake notification",
                    Toast.LENGTH_LONG
                ).show()

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.channel_id), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        // intent
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        // builder
        var builder = NotificationCompat.Builder(this, getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_pets)
            .setContentTitle("AppOINKment today \uD83D\uDC37")
            .setContentText("Contenuto prova " + SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance().time))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // show notification
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, builder.build())
        }

    }

}


class SwipeHandler(val adapterShowAppointments: Adapter_ShowAppointments, dragDirs : Int, swipeDirs : Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapterShowAppointments.removeCard(viewHolder.adapterPosition)
    }
}