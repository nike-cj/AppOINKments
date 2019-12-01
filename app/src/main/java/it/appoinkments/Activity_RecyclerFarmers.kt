package it.appoinkments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.appoinkments.data.Load
import it.appoinkments.data.LoadViewModel
import it.appoinkments.data.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class Activity_RecyclerFarmers : AppCompatActivity() {

    //______________________________________________________________________________________________
    // attributes
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    var farmers: MutableList<String> = mutableListOf<String>()
    var total_loads: MutableList<Int> = mutableListOf<Int>()
    var total_pigs: MutableList<Int> = mutableListOf<Int>()

    var name: String = ""
    var date_start: Calendar = Calendar.getInstance().apply {
        set(2019, 1, 1)
    }
    var date_stop: Calendar = Calendar.getInstance()

    private val loadViewModel: LoadViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(this.application)).get(LoadViewModel::class.java)
    }


    //______________________________________________________________________________________________
    // system callbacks
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_farmers)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Farmers summary"

        // retrieve data
        val farmers: List<String> = loadViewModel.getFarmers()
        var total_loads = mutableListOf<Int>()
        var total_pigs = mutableListOf<Int>()
        for (item in farmers) {
            total_loads.add(loadViewModel.getTotalLoads(item))
            total_pigs.add(loadViewModel.getTotalPigs(item))
        }

        // create recycler view
        viewManager = LinearLayoutManager(this)

        viewAdapter = Adapter_RecyclerFarmers(farmers, total_loads, total_pigs, this)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_show_farmers, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.search -> {
                val fm: FragmentManager = supportFragmentManager
                val searchFragment = Fragment_Search()
                var bundle = Bundle().apply {
                    putString("old_search", name)
                }
                searchFragment.arguments = bundle

                searchFragment.show(fm, "SearchFragment_tag")
                return true
            }
            R.id.filter -> {
                val fm: FragmentManager = supportFragmentManager
                val filterFragment = Fragment_Filter()
                var bundle = Bundle().apply {
                    putString("old_start", SimpleDateFormat("dd/MM/yyyy").format(date_start.time))
                    putString("old_stop", SimpleDateFormat("dd/MM/yyyy").format(date_stop.time))
                }
                filterFragment.arguments = bundle

                filterFragment.show(fm, "FilterFragment_tag")
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, Activity_RecyclerAppointments::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    //______________________________________________________________________________________________
    // internal facilities
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾

    fun retrieveData() {
        // delete previous data
        farmers.clear()
        total_loads.clear()
        total_pigs.clear()

        // query the database
        var list_loads: List<Load>
        if (name == "") {
            list_loads = loadViewModel.getSummary(date_start, date_stop)
        } else {
            list_loads = loadViewModel.getSummary(name, date_start, date_stop)
        }

        // prepare data structures to feed the adapter
        for (load in list_loads) {
            var index: Int = farmers.indexOf(load.farmer)
            if (index == -1) {   // not present
                farmers.add(load.farmer)
                total_loads.add(1)
                total_pigs.add(load.nr_pigs)
            } else {    // increase sum
                total_loads[index] ++
                total_pigs[index] += load.nr_pigs
            }
        }

//        val farmers: List<String> = loadViewModel.getFarmers()
//        var total_loads = mutableListOf<Int>()
//        var total_pigs = mutableListOf<Int>()
//        for (item in farmers) {
//            total_loads.add(loadViewModel.getTotalLoads(item))
//            total_pigs.add(loadViewModel.getTotalPigs(item))
//        }
    }

    fun updateSearch(target_name: String) {
        supportFragmentManager
        this.name = target_name
        update()
    }

    fun updateFilter(date_start: Calendar, date_stop: Calendar) {
        this.date_start = date_start
        this.date_stop  = date_stop
        update()
    }

    fun update() {
        retrieveData()
        (viewAdapter as Adapter_RecyclerFarmers).updateData(farmers, total_loads, total_pigs)
    }

}