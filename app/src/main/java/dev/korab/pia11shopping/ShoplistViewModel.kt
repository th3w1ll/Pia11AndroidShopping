package dev.korab.pia11shopping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import javax.security.auth.callback.Callback

class ShoplistViewModel : ViewModel() {

    //var shopitems = mutableListOf<ShoppingItem>()

    val shopitems: MutableLiveData<List<ShoppingItem>> by lazy {
        MutableLiveData<List<ShoppingItem>>()
    }

    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


    //fun addshopping(addshopname : String, addshopamount : Int, callback: () -> Unit) {
    fun addshopping(addshopname : String, addshopamount : String) {

        if(addshopname == "") {
            errorMessage.value = "Ange Text"
            return
        }

        val shopamount = addshopamount.toIntOrNull()
        if (shopamount == null) {
            errorMessage.value = "Skriv en siffra"
            return
        }

         errorMessage.value = ""

        val tempShopItem = ShoppingItem(addshopname, shopamount)

        val database = Firebase.database
        val shopRef = database.getReference("androidshopping").child(Firebase.auth.currentUser!!.uid)
        shopRef.push().setValue(tempShopItem).addOnCompleteListener {
            loadShopping()
        }

    }


    //fun loadShopping(callback: () -> Unit) {
    fun loadShopping() {

        val database = Firebase.database
        val shopRef = database.getReference("androidshopping").child(Firebase.auth.currentUser!!.uid)

        shopRef.get().addOnSuccessListener {
            val shoplist = mutableListOf<ShoppingItem>()
            it.children.forEach { childsnap ->
                val tempShop = childsnap.getValue<ShoppingItem>()!!
                tempShop.fbid = childsnap.key
                shoplist.add(tempShop)
            }
            shopitems.value = shoplist
        }
    }


    //fun deleteShop(delitem : ShoppingItem, callback: () -> Unit) {
    fun deleteShop(delitem : ShoppingItem) {
        val database = Firebase.database
        val shopRef = database.getReference("androidshopping").child(Firebase.auth.currentUser!!.uid)

        shopRef.child(delitem.fbid!!).removeValue().addOnCompleteListener {
            loadShopping()
        }
    }

    fun doneShop(doneItem : ShoppingItem, isDone : Boolean) {
        val database = Firebase.database
        val shopRef = database.getReference("androidshopping").child(Firebase.auth.currentUser!!.uid)

        var saveitem = doneItem
        saveitem.shopdone = isDone
        shopRef.child(doneItem.fbid!!).setValue(saveitem).addOnCompleteListener {
            //loadShopping()
        }
    }



}