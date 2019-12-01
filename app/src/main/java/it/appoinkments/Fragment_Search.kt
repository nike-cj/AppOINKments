package it.appoinkments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_farmer_search.*

class Fragment_Search : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_farmer_search, container)

        this.dialog.setTitle("Cerca per nome")

        // set previous search
        val bundle: Bundle? = this.arguments
        var name: String = ""
        if (bundle != null) {
            name = bundle.getString("old_search")
        }
        rootView.findViewById<TextView>(R.id.name).text = name

        // button action
        rootView.findViewById<Button>(R.id.submit).setOnClickListener{ view ->
            var target_name: String = rootView.findViewById<TextView>(R.id.name).text.toString()
            (activity as Activity_RecyclerFarmers).updateSearch(target_name)
            dismiss()
        }

        return rootView
    }
}