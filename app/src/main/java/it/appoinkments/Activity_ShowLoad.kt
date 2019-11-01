package it.appoinkments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import it.appoinkments.data.Load
import it.appoinkments.data.LoadViewModel
import it.appoinkments.data.ViewModelFactory
import java.text.SimpleDateFormat


class Activity_ShowLoad : AppCompatActivity() {

    //______________________________________________________________________________________________
    // attributes
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
    private val loadViewModel: LoadViewModel by lazy {
        ViewModelProviders.of(this,   ViewModelFactory(this.application)).get(LoadViewModel::class.java)
    }

    //______________________________________________________________________________________________
    // system callbacks
    //‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_load)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "AppOINKments \uD83D\uDC37"

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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_show, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
