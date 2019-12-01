package it.appoinkments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class Fragment_Filter : DialogFragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_farmer_filter, container)

        this.dialog.setTitle("Cerca per data")

        // set default date
        val bundle: Bundle? = this.arguments
        var date_start = "1/1/1970"
        var date_stop = "31/12/2050"
        if (bundle != null) {
            date_start = bundle.getString("old_start")
            date_stop = bundle.getString("old_stop")
        }
        rootView.findViewById<TextView>(R.id.date_start).text = date_start
        rootView.findViewById<TextView>(R.id.date_stop).text = date_stop

        // button action
        rootView.findViewById<Button>(R.id.submit).setOnClickListener{ view ->
            var start = Calendar.getInstance().apply {
                time = SimpleDateFormat("dd/MM/yyyy").parse(
                    rootView.findViewById<TextView>(R.id.date_start).text.toString()
                )
            }
            var stop = Calendar.getInstance().apply {
                time = SimpleDateFormat("dd/MM/yyyy").parse(
                    rootView.findViewById<TextView>(R.id.date_stop).text.toString()
                )
            }
            (activity as Activity_RecyclerFarmers).updateFilter(start, stop)
            dismiss()
        }

        rootView.findViewById<TextInputEditText>(R.id.date_start)
                .setOnClickListener { view ->
            view.hideKeyboard()
            showDatePickerDialog(view)
        }
        rootView.findViewById<TextInputEditText>(R.id.date_start)
                .setOnFocusChangeListener { view, focused ->
            if (focused) {
                view.hideKeyboard()
                showDatePickerDialog(view)
            }
        }

        rootView.findViewById<TextInputEditText>(R.id.date_stop)
                .setOnClickListener { view ->
            view.hideKeyboard()
            showDatePickerDialog(view)
        }
        rootView.findViewById<TextInputEditText>(R.id.date_stop)
                .setOnFocusChangeListener { view, focused ->
            if (focused) {
                view.hideKeyboard()
                showDatePickerDialog(view)
            }
        }

        return rootView
    }



    fun showDatePickerDialog(v: View) {
        val newFragment = Fragment_DatePicker(v)
        newFragment.show(activity?.supportFragmentManager, "datePicker")
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}