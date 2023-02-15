package layout

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.korab.pia11shopping.R
import dev.korab.pia11shopping.ShoppingFragment
import dev.korab.pia11shopping.ShoppingItem

class ShoppingAdapter : RecyclerView.Adapter<ShoppingAdapter.ViewHolder>() {

    //var shopitems = mutableListOf<ShoppingItem>()
    lateinit var frag : ShoppingFragment

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val shoppingName : TextView
        val shoppingDelete : ImageView
        val shoppingCheckbox : CheckBox

        init {
            shoppingName = view.findViewById(R.id.shopNameTV)
            shoppingDelete = view.findViewById(R.id.shopDeleteImage)
            shoppingCheckbox = view.findViewById(R.id.shopCheckbox)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var currentShop = frag.model.shopitems.value!![position]

        if(currentShop.shopamount == null) {
            holder.shoppingName.text = currentShop.shopname
        } else {
            holder.shoppingName.text = currentShop.shopname + " " + currentShop.shopamount!!.toString()
        }



        holder.shoppingDelete.setOnClickListener {
            frag.model.deleteShop(currentShop)
        }

        holder.shoppingCheckbox.isChecked = false
        currentShop.shopdone?.let {
            holder.shoppingCheckbox.isChecked = it
        }

        /*
        //TODO: Gör en markering ifall det är inköp eller ej. Typ markör
        holder.shoppingCheckbox.setOnCheckedChangeListener {compundButton, shopchecked ->
            Log.i("pia11debug", "Changed" + shopchecked.toString())
            frag.model.doneShop(currentShop, shopchecked)
        }
        */

        holder.shoppingCheckbox.setOnClickListener {
            Log.i("pia11debug", "Changed" + holder.shoppingCheckbox.isChecked.toString())
            frag.model.doneShop(currentShop, holder.shoppingCheckbox.isChecked)
        }


    }

    override fun getItemCount(): Int {
        frag.model.shopitems.value?.let {value ->
            return value.size
        }

        return 0

    }

}