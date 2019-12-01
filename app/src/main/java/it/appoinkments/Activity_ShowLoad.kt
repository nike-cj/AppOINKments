package it.appoinkments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import it.appoinkments.data.*
import java.text.SimpleDateFormat


class Activity_ShowLoad : AppCompatActivity() {

    //______________________________________________________________________________________________
    // attributes
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
    private var parent_activity: String = ""
    private var parent_farmer: String? = ""

    private val loadViewModel: LoadViewModel by lazy {
        ViewModelProviders.of(this,   ViewModelFactory(this.application)).get(LoadViewModel::class.java)
    }
    private val appointmentViewModel: AppointmentViewModel by lazy {
        ViewModelProviders.of(this,   ViewModelFactory(this.application)).get(AppointmentViewModel::class.java)
    }

    //______________________________________________________________________________________________
    // system callbacks
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_load)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Load details"

        // retrieve parent information
        parent_activity = intent.getStringExtra("parent_activity")
        parent_farmer = intent.getStringExtra("farmer")
        if (parent_farmer == null)
            parent_farmer = ""

        // retrieve Load
        val load_id : Long = intent.getLongExtra("load_id", -1)
        val load : Load = loadViewModel.getLoad(load_id)

        // fill data
        findViewById<TextView>(R.id.content_farmer).text = load.farmer
        findViewById<TextView>(R.id.content_number_pigs).text = load.nr_pigs.toString()
        findViewById<TextView>(R.id.content_round).text = load.round
        findViewById<TextView>(R.id.content_source).text = load.origin
        val date_format : SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        findViewById<TextView>(R.id.content_date_arrival).text = date_format.format(load.date_arrival)
        findViewById<TextView>(R.id.content_date_vaccination_first).text = date_format.format(load.date_vaccination_first)
        findViewById<TextView>(R.id.content_date_vaccination_second).text = date_format.format(load.date_vaccination_second)
        findViewById<TextView>(R.id.content_date_vaccination_third).text = date_format.format(load.date_vaccination_third)
        findViewById<TextView>(R.id.content_date_vermicide_first).text = date_format.format(load.date_vermicide_first)
        findViewById<TextView>(R.id.content_date_vermicide_second).text = date_format.format(load.date_vermicide_second)
        
        // check and cross marks
        if (appointmentViewModel.isCompleted(load_id, AppointmentType.vaccination_first))
            findViewById<TextView>(R.id.done_vaccination_first).text = "✓"
        else
            findViewById<TextView>(R.id.done_vaccination_first).text = "✗"
        if (appointmentViewModel.isCompleted(load_id, AppointmentType.vaccination_second))
            findViewById<TextView>(R.id.done_vaccination_second).text = "✓"
        else
            findViewById<TextView>(R.id.done_vaccination_second).text = "✗"
        if (appointmentViewModel.isCompleted(load_id, AppointmentType.vaccination_third))
            findViewById<TextView>(R.id.done_vaccination_third).text = "✓"
        else
            findViewById<TextView>(R.id.done_vaccination_third).text = "✗"
        if (appointmentViewModel.isCompleted(load_id, AppointmentType.vermicide_first))
            findViewById<TextView>(R.id.done_vermicide_first).text = "✓"
        else
            findViewById<TextView>(R.id.done_vermicide_first).text = "✗"
        if (appointmentViewModel.isCompleted(load_id, AppointmentType.vermicide_second))
            findViewById<TextView>(R.id.done_vermicide_second).text = "✓"
        else
            findViewById<TextView>(R.id.done_vermicide_second).text = "✗"

        // click for changing completion
        findViewById<TextView>(R.id.done_vaccination_first).setOnClickListener { view ->
            if (appointmentViewModel.isCompleted(load_id, AppointmentType.vaccination_first)) {
                appointmentViewModel.markNotCompleted(load_id, AppointmentType.vaccination_first)
                (view as TextView).text = "✗"
            } else {
                appointmentViewModel.markCompleted(load_id, AppointmentType.vaccination_first)
                (view as TextView).text = "✓"
            }
        }
        findViewById<TextView>(R.id.done_vaccination_second).setOnClickListener { view ->
            if (appointmentViewModel.isCompleted(load_id, AppointmentType.vaccination_second)) {
                appointmentViewModel.markNotCompleted(load_id, AppointmentType.vaccination_second)
                (view as TextView).text = "✗"
            } else {
                appointmentViewModel.markCompleted(load_id, AppointmentType.vaccination_second)
                (view as TextView).text = "✓"
            }
        }
        findViewById<TextView>(R.id.done_vaccination_third).setOnClickListener { view ->
            if (appointmentViewModel.isCompleted(load_id, AppointmentType.vaccination_third)) {
                appointmentViewModel.markNotCompleted(load_id, AppointmentType.vaccination_third)
                (view as TextView).text = "✗"
            } else {
                appointmentViewModel.markCompleted(load_id, AppointmentType.vaccination_third)
                (view as TextView).text = "✓"
            }
        }
        findViewById<TextView>(R.id.done_vermicide_first).setOnClickListener { view ->
            if (appointmentViewModel.isCompleted(load_id, AppointmentType.vermicide_first)) {
                appointmentViewModel.markNotCompleted(load_id, AppointmentType.vermicide_first)
                (view as TextView).text = "✗"
            } else {
                appointmentViewModel.markCompleted(load_id, AppointmentType.vermicide_first)
                (view as TextView).text = "✓"
            }
        }
        findViewById<TextView>(R.id.done_vermicide_second).setOnClickListener { view ->
            if (appointmentViewModel.isCompleted(load_id, AppointmentType.vermicide_second)) {
                appointmentViewModel.markNotCompleted(load_id, AppointmentType.vermicide_second)
                (view as TextView).text = "✗"
            } else {
                appointmentViewModel.markCompleted(load_id, AppointmentType.vermicide_second)
                (view as TextView).text = "✓"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_show, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent_activity activity in AndroidManifest.xml.
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
