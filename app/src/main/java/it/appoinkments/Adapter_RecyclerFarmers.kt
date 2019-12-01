package it.appoinkments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.text.method.TextKeyListener.clear
import androidx.lifecycle.ViewModel



class Adapter_RecyclerFarmers(private var farmers: List<String>, private var total_loads: List<Int>,
                              private var total_pigs: List<Int>, val context: Context) :
    RecyclerView.Adapter<Adapter_RecyclerFarmers.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val card: CardView) : RecyclerView.ViewHolder(card) {

        //----- attributes -------------------------------------------------------------------------
        public var farmer : TextView = card.findViewById(R.id.farmer)
        public var total_pigs: TextView = card.findViewById(R.id.total_pigs)
        public var total_loads: TextView = card.findViewById(R.id.total_loads)
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): Adapter_RecyclerFarmers.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_farmer, parent, false) as CardView

        // set the view's size, margins, paddings and layout parameters
        cardView.setOnClickListener { v ->
            var item : String = farmers[ (parent as RecyclerView).getChildLayoutPosition(v) ]

            val intent = Intent(parent.context, Activity_RecyclerLoads::class.java)
            intent.putExtra("farmer", item)
            intent.putExtra("old_search", (context as Activity_RecyclerFarmers).name)
            intent.putExtra("old_start", (context as Activity_RecyclerFarmers).date_start)
            intent.putExtra("old_stop", (context as Activity_RecyclerFarmers).date_stop)
            parent.context.startActivity(intent)
        }

        // return view holder
        return MyViewHolder(cardView)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        var farmer: String = farmers[position]

        holder.farmer.text = farmers[position]
        holder.total_loads.text = "Carichi totali: " + total_loads[position].toString()
        holder.total_pigs.text = "Maiali totali: " + total_pigs[position].toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = farmers.size

    // update data, without re-instantiate the whole recycler
    fun updateData(farmers: List<String>, total_loads: List<Int>,
                   total_pigs: List<Int>) {
        this.farmers = farmers
        this.total_loads = total_loads
        this.total_pigs = total_pigs
        notifyDataSetChanged()
    }
}
