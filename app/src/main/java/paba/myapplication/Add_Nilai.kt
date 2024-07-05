package paba.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Add_Nilai.newInstance] factory method to
 * create an instance of this fragment.
 */
class Add_Nilai : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var _etAddNamaMKNilai : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add__nilai, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _etAddNamaMKNilai = view.findViewById(R.id.etAddNamaMKNilai)
        val _btnAddNilai = view.findViewById<Button>(R.id.btnAddNilai)
        _btnAddNilai.setOnClickListener {
            tambahData(MainActivity.db,_etAddNamaMKNilai.text.toString(),"0")
            val mList_Nilai = List_Nilai()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, mList_Nilai, List_Nilai:: class. java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }
    fun tambahData(db: FirebaseFirestore, nama: String, totalNilai : String){
        val dataBaru = Nilai(nama,totalNilai)
        var nama_mk = dataBaru.nama
        if (MainActivity.position != -1 && MainActivity.page == "Add_Note"){
            nama_mk = MainActivity.judul
        }
        db.collection("tbNilai")
            .document(nama_mk)
            .set(dataBaru)
            .addOnSuccessListener {
                _etAddNamaMKNilai.setText("")
                Log.d("Firebase", "Data Berhasil Disimpan")
                MainActivity.position = -1
                MainActivity.judul = ""
                MainActivity.page = ""
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Add_Nilai.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Add_Nilai().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}