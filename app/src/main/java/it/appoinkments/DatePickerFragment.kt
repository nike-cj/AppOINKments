package it.appoinkments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment(source: View) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var _source : View
    init {
        _source = source
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        // retrieve selected date
        val str = (_source as EditText).text.toString()
        val c = Calendar.getInstance()
        c.time = SimpleDateFormat("dd/MM/yyyy").parse(str)


        // given date as the default date in the picker
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // retrieve selected date
        val chosen_date = Calendar.getInstance()
        chosen_date.set(year, month, day)

        // affect all dates or only the selected one
        if (_source.id == R.id.date_arrival) {
            // assign date value to EditText
            val date = SimpleDateFormat("dd/MM/yyyy").format(chosen_date.time)
            activity?.findViewById<TextInputEditText>(R.id.date_arrival)
                ?.setText(date)

            // set default deadlines
            chosen_date.add(Calendar.DAY_OF_YEAR, 7)  // one week
            activity?.findViewById<TextInputEditText>(R.id.date_vaccination_first)
                ?.setText(SimpleDateFormat("dd/MM/yyyy").format(chosen_date.time))
            chosen_date.add(Calendar.DAY_OF_YEAR, 14)  // three weeks
            activity?.findViewById<TextInputEditText>(R.id.date_vermicide_first)
                ?.setText(SimpleDateFormat("dd/MM/yyyy").format(chosen_date.time))
            chosen_date.add(Calendar.DAY_OF_YEAR, 14)  // five weeks
            activity?.findViewById<TextInputEditText>(R.id.date_vaccination_second)
                ?.setText(SimpleDateFormat("dd/MM/yyyy").format(chosen_date.time))
            chosen_date.add(Calendar.DAY_OF_YEAR, 55)  // three months
            activity?.findViewById<TextInputEditText>(R.id.date_vaccination_third)
                ?.setText(SimpleDateFormat("dd/MM/yyyy").format(chosen_date.time))
            activity?.findViewById<TextInputEditText>(R.id.date_vermicide_second)
                ?.setText(SimpleDateFormat("dd/MM/yyyy").format(chosen_date.time))
        }
        else {
            // assign date value to EditText
            val date = SimpleDateFormat("dd/MM/yyyy").format(chosen_date.time)
            activity?.findViewById<TextInputEditText>(_source.id)
                ?.setText(date)
        }
    }
}
