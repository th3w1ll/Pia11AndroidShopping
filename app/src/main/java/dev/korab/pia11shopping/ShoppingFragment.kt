package dev.korab.pia11shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.korab.pia11shopping.databinding.FragmentShoppingBinding
import layout.ShoppingAdapter

class ShoppingFragment : Fragment() {

    var _binding : FragmentShoppingBinding? = null
    val binding get() = _binding!!

    var shopadapter = ShoppingAdapter()

    val model by viewModels<ShoplistViewModel> ()

    //////////////////////////////////////////////
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_shopping, container, false)

        shopadapter.frag = this

        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    //////////////////////////////////////////////
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //////////////////////////////////////////////
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shoppingRV.adapter = shopadapter
        binding.shoppingRV.layoutManager = LinearLayoutManager(requireContext())

        val shopObserver = Observer<List<ShoppingItem>> {
            shopadapter.notifyDataSetChanged()
        }

        model.shopitems.observe(viewLifecycleOwner, shopObserver)


        //Detta är vår logout button
        binding.logoutButton.setOnClickListener {
           // Toast.makeText(requireContext(),"Logga ut", Toast.LENGTH_SHORT).show()
            Firebase.auth.signOut()
        }


        //Detta är vår läggtillknapp
        binding.addShoppingButton.setOnClickListener {
            val addshopname = binding.shoppingNameET.text.toString()
            val addshopamount = binding.shoppingAmountET.text.toString()

            val amount = addshopamount.toIntOrNull()
            if (amount == null) {
                //TODO: Visa Felmedelande
                Toast.makeText(requireContext(),"Felaktig Inmatning", Toast.LENGTH_SHORT).show()
            } else {
                model.addshopping(addshopname, amount)

                binding.shoppingNameET.setText("")
                binding.shoppingAmountET.setText("")

            }
        }

        model.loadShopping()

    }
}