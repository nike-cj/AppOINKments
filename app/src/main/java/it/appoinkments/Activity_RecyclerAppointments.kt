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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.appoinkments.data.AppointmentViewModel
import it.appoinkments.data.ViewModelFactory
import kotlinx.android.synthetic.main.activity_show_appointments.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class Activity_RecyclerAppointments : AppCompatActivity() {

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

        // create recycler view
        viewManager = LinearLayoutManager(this)
        viewAdapter = Adapter_RecyclerAppointments(appointmentViewModel.getNotCompleted(), applicationContext)

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
        val touchHandler = ItemTouchHelper(SwipeHandler(viewAdapter as Adapter_RecyclerAppointments, 0, (ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)))
        touchHandler.attachToRecyclerView(recyclerView)

        // assign action to + button
        fab.setOnClickListener { view ->
            val intent = Intent(this, Activity_NewLoad::class.java)
            startActivity(intent)
            finish()
        }

        //----- set timer for notification -----
        // check if alarm already set
        var alarmIntentTemp = Intent(this as Context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this as Context, 0, alarmIntentTemp,
            PendingIntent.FLAG_NO_CREATE  //just check existence, do not create
        )

        if (pendingIntent == null) {
            // create pending intent
            alarmMgr = (this as Context).getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmIntent = Intent(this as Context, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(this as Context, 0, intent, 0)
            }

            // set the alarm to start at approximately 8:00 a.m.
            var calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 8)
                set(Calendar.MINUTE, 0)
            }

            // setup the alarm for triggering once a day
            alarmMgr?.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmIntent
            )
        }
    }

    override fun onResume() {
        super.onResume()
        update()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_show_appointments, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.farmers -> {
                val intent = Intent(this, Activity_RecyclerFarmers::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun update() {
        (viewAdapter as Adapter_RecyclerAppointments).updateData(appointmentViewModel.getNotCompleted())
    }
}


class SwipeHandler(val adapterRecyclerAppointments: Adapter_RecyclerAppointments, dragDirs : Int, swipeDirs : Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapterRecyclerAppointments.removeCard(viewHolder.adapterPosition)
    }
}