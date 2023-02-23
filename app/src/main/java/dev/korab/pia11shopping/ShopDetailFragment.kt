package dev.korab.pia11shopping

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev.korab.pia11shopping.databinding.FragmentShopDetailBinding
import dev.korab.pia11shopping.databinding.FragmentShoppingBinding
import java.io.ByteArrayOutputStream
import java.io.File

class ShopDetailFragment(val currentShop : ShoppingItem) : Fragment() {

    var _binding : FragmentShopDetailBinding? = null
    val binding get() = _binding!!

    val model by activityViewModels<ShoplistViewModel>()

    lateinit var imageuri : Uri

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri

        Log.i("Pia11Debug","Galleri Resultat")

        uri?.let {
            val source = ImageDecoder.createSource(requireActivity().contentResolver, it)
            val bitmap = ImageDecoder.decodeBitmap(source)

            binding.detailImageView.setImageBitmap(bitmap)

            model.saveShopImage(currentShop, bitmap)

        }
    }

    val getContentcamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {

        if(it == true) {
            Log.i("PIA11DEBUG", "KAMERA OK")
            val source = ImageDecoder.createSource(requireActivity().contentResolver, imageuri)
            val bitmap = ImageDecoder.decodeBitmap(source)

            binding.detailImageView.setImageBitmap(bitmap)

            model.saveShopImage(currentShop, bitmap)

        } else {
            Log.i("PIA11DEBUG", "KAMERA EJ OK")

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShopDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detailNameET.setText(currentShop.shopname)

        currentShop.shopamount?.let {
            binding.detailAmountET.setText(it.toString())
        }

        binding.detailSaveButton.setOnClickListener {

            currentShop.shopname = binding.detailNameET.text.toString()
            currentShop.shopamount = binding.detailAmountET.text.toString().toInt()

            model.saveShopItem(currentShop)

            requireActivity().supportFragmentManager.popBackStack()

        }

        binding.galleryButton.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.cameraButton.setOnClickListener {
            val tempfile = getPhotoFile("bilden")
            imageuri = FileProvider.getUriForFile(requireContext(), "dev.korab.pia11shopping.fileprovider", tempfile)

            getContentcamera.launch(imageuri)
        }

        downloadImage()
    }

    private fun getPhotoFile(fileName: String): File {
        val directoryStorage = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directoryStorage)
    }

    fun downloadImage() {
        var storageRef = Firebase.storage.reference
        var imageRef = storageRef.child("shoppingimages").child(Firebase.auth.currentUser!!.uid)

        imageRef.child(currentShop.fbid!!).getBytes(1_000_000).addOnSuccessListener {
            var bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)

            binding.detailImageView.setImageBitmap(bitmap)

        }.addOnFailureListener {

        }

    }
}