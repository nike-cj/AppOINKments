package it.appoinkments

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import it.appoinkments.data.Appointment
import java.text.SimpleDateFormat
import java.util.*

class MyAdapter(private val myDataset: List<Appointment>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val card: CardView) : RecyclerView.ViewHolder(card) {
        init {
            card.setOnClickListener { v ->
                var intent = Intent(v.context, ShowLoad::class.java)
                //TODO fill bundle
                var activity = v.context as AppCompatActivity
                activity.startActivity(intent)
                activity.finish()
            }
        }

        //----- attributes -------------------------------------------------------------------------
        public var farmer : TextView = card.findViewById(R.id.farmer)
        public var date: TextView = card.findViewById(R.id.date)
        public var appointment_type: TextView = card.findViewById(R.id.appointment_type)
        public var nr_pigs: TextView = card.findViewById(R.id.nr_pigs)
        public var round: TextView = card.findViewById(R.id.round)

        //----- constructor ------------------------------------------------------------------------
//        constructor() : this(card_appointment) {
//
//        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_appointment, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters
        ///TODO
        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        var item: Appointment = myDataset[position]
        var now = Calendar.getInstance()
        var target_date = Calendar.getInstance()
        target_date.time = item.date

        holder.farmer.text = item.farmer
        when (target_date.get(Calendar.DATE) - now.get(Calendar.DATE)) {
            -1 -> holder.date.text = "Ieri"
            0 -> holder.date.text = "Oggi"
            1 -> holder.date.text = "Domani"
            2 -> holder.date.text = "Dopodomani"
            else -> holder.date.text = SimpleDateFormat("dd/MM/yyyy").format(item.date)
        }
        holder.appointment_type.text = item.type.toString()
        holder.nr_pigs.text = item.nr_pigs.toString() + " maiali"
        holder.round.text = item.round
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}
