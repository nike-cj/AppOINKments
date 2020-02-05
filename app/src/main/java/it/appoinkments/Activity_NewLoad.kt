package it.appoinkments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import it.appoinkments.data.*
import java.text.SimpleDateFormat
import java.util.*


class Activity_NewLoad : AppCompatActivity() {

    //______________________________________________________________________________________________
    // attributes
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
    var load_id : Long = -1L


    private val loadViewModel: LoadViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(this.application)).get(LoadViewModel::class.java)
    }
    private val appointmentViewModel: AppointmentViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(this.application)).get(AppointmentViewModel::class.java)
    }

    //______________________________________________________________________________________________
    // system callbacks
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_load)
        //setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "AppOINKments \uD83D\uDC37"

        // distinguish between edit/create Load
        load_id = intent.getLongExtra("load_id", -1L)

        // fill fields or default
        var load : Load
        if (load_id != -1L) {
            load = loadViewModel.getLoad(load_id)
        } else {
            val c = Calendar.getInstance()
            val arrival = c.time
            c.add(Calendar.DAY_OF_YEAR, 7)  // one week
            val vaccination_first = c.time
            c.add(Calendar.DAY_OF_YEAR, 14)  // three weeks
            val vermicide_first = c.time
            c.add(Calendar.DAY_OF_YEAR, 14)  // five weeks
            val vaccination_second = c.time
            c.add(Calendar.DAY_OF_YEAR, 25)  // forty days
            val vermicide_second = c.time
            c.add(Calendar.DAY_OF_YEAR, 40)  // three months
            val vaccination_third = c.time

            load = Load (
                date_arrival = arrival,
                date_vaccination_first = vaccination_first,
                date_vaccination_second = vaccination_second,
                date_vaccination_third = vaccination_third,
                date_vermicide_first = vermicide_first,
                date_vermicide_second = vermicide_second,
                farmer = "",
                nr_pigs = 0,
                round = "",
                origin = ""
            )
        }
        fillFields(load)


        // create DatePicker at click
        val date_input = findViewById<TextInputEditText>(R.id.date_arrival)
        date_input.setOnClickListener { view: View ->
            view.hideKeyboard()
            showDatePickerDialog(view)
        }
        date_input.setOnFocusChangeListener { view, focused ->
            if (focused) {
                view.hideKeyboard()
                showDatePickerDialog(view)
            }
        }

        findViewById<TextInputEditText>(R.id.date_vaccination_first)
            .setOnClickListener { view ->
                view.hideKeyboard()
                showDatePickerDialog(view)
            }
        findViewById<TextInputEditText>(R.id.date_vaccination_first)
            .setOnFocusChangeListener { view, focused ->
                if (focused) {
                    view.hideKeyboard()
                    showDatePickerDialog(view)
                }
            }

        findViewById<TextInputEditText>(R.id.date_vaccination_second)
            .setOnClickListener { view ->
                view.hideKeyboard()
                showDatePickerDialog(view)
            }
        findViewById<TextInputEditText>(R.id.date_vaccination_second)
            .setOnFocusChangeListener { view, focused ->
                if (focused) {
                    view.hideKeyboard()
                    showDatePickerDialog(view)
                }
            }

        findViewById<TextInputEditText>(R.id.date_vaccination_third)
            .setOnClickListener { view ->
                view.hideKeyboard()
                showDatePickerDialog(view)
            }
        findViewById<TextInputEditText>(R.id.date_vaccination_third)
            .setOnFocusChangeListener { view, focused ->
                if (focused) {
                    view.hideKeyboard()
                    showDatePickerDialog(view)
                }
            }

        findViewById<TextInputEditText>(R.id.date_vermicide_first)
            .setOnClickListener { view ->
                view.hideKeyboard()
                showDatePickerDialog(view)
            }
        findViewById<TextInputEditText>(R.id.date_vermicide_first)
            .setOnFocusChangeListener { view, focused ->
                if (focused) {
                    view.hideKeyboard()
                    showDatePickerDialog(view)
                }
            }

        findViewById<TextInputEditText>(R.id.date_vermicide_second)
            .setOnClickListener { view ->
                view.hideKeyboard()
                showDatePickerDialog(view)
            }
        findViewById<TextInputEditText>(R.id.date_vermicide_second)
            .setOnFocusChangeListener { view, focused ->
                if (focused) {
                    view.hideKeyboard()
                    showDatePickerDialog(view)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_new_load, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.done -> {
                var new_load: Boolean = true
                if (load_id != -1L)
                    new_load = false

                storeData()

                if (new_load) {
//                    val intent = Intent(this, Activity_RecyclerAppointments::class.java)
//                    startActivity(intent)
                    finish()
                } else {
//                    val intent = Intent(this, Activity_ShowLoad::class.java)
//                    intent.putExtra("load_id", load_id)
//                    startActivity(intent)
                    finish()
                }

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

//        val intent = Intent(this, Activity_RecyclerAppointments::class.java)
//        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun showDatePickerDialog(v: View) {
        val newFragment = Fragment_DatePicker(v)
        newFragment.show(supportFragmentManager, "datePicker")
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun fillFields(load: Load) {
        // fill generic info
        findViewById<TextInputEditText>(R.id.farmer)
            .setText(load.farmer)
        findViewById<TextInputEditText>(R.id.number_pig)
            .setText(load.nr_pigs.toString())
        findViewById<TextInputEditText>(R.id.cycle)
            .setText(load.round)
        findViewById<TextInputEditText>(R.id.source)
            .setText(load.origin)

        // fill the date
        findViewById<TextInputEditText>(R.id.date_arrival)
            .setText(SimpleDateFormat("dd/MM/yyyy").format(load.date_arrival))
        findViewById<TextInputEditText>(R.id.date_vaccination_first)
            .setText(SimpleDateFormat("dd/MM/yyyy").format(load.date_vaccination_first))
        findViewById<TextInputEditText>(R.id.date_vaccination_second)
            .setText(SimpleDateFormat("dd/MM/yyyy").format(load.date_vaccination_second))
        findViewById<TextInputEditText>(R.id.date_vaccination_third)
            .setText(SimpleDateFormat("dd/MM/yyyy").format(load.date_vaccination_third))
        findViewById<TextInputEditText>(R.id.date_vermicide_first)
            .setText(SimpleDateFormat("dd/MM/yyyy").format(load.date_vermicide_first))
        findViewById<TextInputEditText>(R.id.date_vermicide_second)
            .setText(SimpleDateFormat("dd/MM/yyyy").format(load.date_vermicide_first))
    }

    fun storeData() {
        // distinguish between edit/create Load
        if (load_id == -1L)
            create()
        else
            update()
    }

    fun create() {
        // create Load object
        var date_format = SimpleDateFormat("dd/MM/yyyy")
        var load: Load = Load (
            farmer = (findViewById<TextInputEditText>(R.id.farmer) as EditText).text.toString(),
            date_arrival = date_format
                .parse( (findViewById<TextInputEditText>(R.id.date_arrival) as EditText).text.toString() ),
            nr_pigs = (findViewById<TextInputEditText>(R.id.number_pig) as EditText).text.toString().toInt(),
            origin = (findViewById<TextInputEditText>(R.id.source) as EditText).text.toString(),
            round = (findViewById<TextInputEditText>(R.id.cycle) as EditText).text.toString(),

            date_vaccination_first = date_format
                .parse( (findViewById<TextInputEditText>(R.id.date_vaccination_first) as EditText).text.toString() ),
            date_vaccination_second = date_format
                .parse( (findViewById<TextInputEditText>(R.id.date_vaccination_second) as EditText).text.toString() ),
            date_vaccination_third = date_format
                .parse( (findViewById<TextInputEditText>(R.id.date_vaccination_third) as EditText).text.toString() ),
            date_vermicide_first = date_format
                .parse( (findViewById<TextInputEditText>(R.id.date_vermicide_first) as EditText).text.toString() ),
            date_vermicide_second = date_format
                .parse( (findViewById<TextInputEditText>(R.id.date_vermicide_second) as EditText).text.toString() )
        )

        // store Load on DB
        load_id = loadViewModel.addLoad(load)
        load.lid = load_id


        // create Appointment objects
        var vaccination_first: Appointment = Appointment (
            date = load.date_vaccination_first,
            type = AppointmentType.vaccination_first,
            farmer = load.farmer,
            nr_pigs = load.nr_pigs,
            round = load.round,
            completed = false,
            load_id = load.lid
        )
        var vaccination_second: Appointment = Appointment (
            date = load.date_vaccination_second,
            type = AppointmentType.vaccination_second,
            farmer = load.farmer,
            nr_pigs = load.nr_pigs,
            round = load.round,
            completed = false,
            load_id = load.lid
        )
        var vaccination_third: Appointment = Appointment (
            date = load.date_vaccination_third,
            type = AppointmentType.vaccination_third,
            farmer = load.farmer,
            nr_pigs = load.nr_pigs,
            round = load.round,
            completed = false,
            load_id = load.lid
        )
        var vermicide_first: Appointment = Appointment (
            date = load.date_vermicide_first,
            type = AppointmentType.vermicide_first,
            farmer = load.farmer,
            nr_pigs = load.nr_pigs,
            round = load.round,
            completed = false,
            load_id = load.lid
        )
        var vermicide_second: Appointment = Appointment (
            date = load.date_vermicide_second,
            type = AppointmentType.vermicide_second,
            farmer = load.farmer,
            nr_pigs = load.nr_pigs,
            round = load.round,
            completed = false,
            load_id = load.lid
        )

        // store Appointments on DB
        appointmentViewModel.addAppointment(vaccination_first)
        appointmentViewModel.addAppointment(vaccination_second)
        appointmentViewModel.addAppointment(vaccination_third)
        appointmentViewModel.addAppointment(vermicide_first)
        appointmentViewModel.addAppointment(vermicide_second)
    }

    fun update() {
        // re-create Load object
        var date_format = SimpleDateFormat("dd/MM/yyyy")
        var load: Load = Load (
            farmer = (findViewById<TextInputEditText>(R.id.farmer) as EditText).text.toString(),
            date_arrival = date_format
                .parse( (findViewById<TextInputEditText>(R.id.date_arrival) as EditText).text.toString() ),
            nr_pigs = (findViewById<TextInputEditText>(R.id.number_pig) as EditText).text.toString().toInt(),
            origin = (findViewById<TextInputEditText>(R.id.source) as EditText).text.toString(),
            round = (findViewById<TextInputEditText>(R.id.cycle) as EditText).text.toString(),

            date_vaccination_first = date_format
                .parse( (findViewById<TextInputEditText>(R.id.date_vaccination_first) as EditText).text.toString() ),
            date_vaccination_second = date_format
                .parse( (findViewById<TextInputEditText>(R.id.date_vaccination_second) as EditText).text.toString() ),
            date_vaccination_third = date_format
                .parse( (findViewById<TextInputEditText>(R.id.date_vaccination_third) as EditText).text.toString() ),
            date_vermicide_first = date_format
                .parse( (findViewById<TextInputEditText>(R.id.date_vermicide_first) as EditText).text.toString() ),
            date_vermicide_second = date_format
                .parse( (findViewById<TextInputEditText>(R.id.date_vermicide_second) as EditText).text.toString() )
        )

        // link with previous entry
        load.lid = load_id

        // update Load table
        loadViewModel.update(load)


        // retrieve with previous Appointment entries
        var vaccination_first   = appointmentViewModel.getByLoad(load.lid, AppointmentTypeConverter.toDatabase(AppointmentType.vaccination_first))
        var vaccination_second  = appointmentViewModel.getByLoad(load.lid, AppointmentTypeConverter.toDatabase(AppointmentType.vaccination_second))
        var vaccination_third   = appointmentViewModel.getByLoad(load.lid, AppointmentTypeConverter.toDatabase(AppointmentType.vaccination_third))
        var vermicide_first     = appointmentViewModel.getByLoad(load.lid, AppointmentTypeConverter.toDatabase(AppointmentType.vermicide_first))
        var vermicide_second    = appointmentViewModel.getByLoad(load.lid, AppointmentTypeConverter.toDatabase(AppointmentType.vermicide_second))

        // update Appointment objects
        vaccination_first.apply {
            date = load.date_vaccination_first
            farmer = load.farmer
            nr_pigs = load.nr_pigs
            round = load.round
        }
        vaccination_second.apply {
            date = load.date_vaccination_second
            farmer = load.farmer
            nr_pigs = load.nr_pigs
            round = load.round
        }
        vaccination_third.apply {
            date = load.date_vaccination_third
            farmer = load.farmer
            nr_pigs = load.nr_pigs
            round = load.round
        }
        vermicide_first.apply {
            date = load.date_vermicide_first
            farmer = load.farmer
            nr_pigs = load.nr_pigs
            round = load.round
        }
        vermicide_second.apply {
            date = load.date_vermicide_second
            farmer = load.farmer
            nr_pigs = load.nr_pigs
            round = load.round
        }

        // update Appointments table
        appointmentViewModel.update(vaccination_first)
        appointmentViewModel.update(vaccination_second)
        appointmentViewModel.update(vaccination_third)
        appointmentViewModel.update(vermicide_first)
        appointmentViewModel.update(vermicide_second)
    }

}
