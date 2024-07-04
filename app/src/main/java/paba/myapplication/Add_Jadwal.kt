package paba.myapplication

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Add_Jadwal.newInstance] factory method to
 * create an instance of this fragment.
 */
class Add_Jadwal : Fragment(), OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

//    lateinit var _etAddHariJadwal : EditText
    lateinit var _etAddNamaMKJadwal : EditText
    lateinit var _etAddRuanganJadwal : EditText
    lateinit var _etAddJamJadwal : EditText
    lateinit var _etAddPengajarJadwal : EditText
    lateinit var spinnerHari : Spinner

    lateinit var namaHari : String

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
        return inflater.inflate(R.layout.fragment_add__jadwal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        _etAddHariJadwal = view.findViewById<EditText>(R.id.etAddHariJadwal)
        _etAddNamaMKJadwal = view.findViewById<EditText>(R.id.etAddNamaMKJadwal)
        _etAddRuanganJadwal = view.findViewById<EditText>(R.id.etAddRuanganJadwal)
        _etAddJamJadwal = view.findViewById<EditText>(R.id.etAddJamJadwal)
        _etAddPengajarJadwal = view.findViewById<EditText>(R.id.etAddPengajarJadwal)

        spinnerHari = view.findViewById(R.id.spinnerHari)
        spinnerHari.onItemSelectedListener = this

        this.context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.hari_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears.
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner.
                spinnerHari.adapter = adapter
            }
        }

        class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                // An item is selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos).
//                namaHari = parent.getItemAtPosition(pos)
//                _etAddHariJadwal.setText(namaHari.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback.
            }
        }

        var urutan = 1
        val _btnAddJadwal = view.findViewById<Button>(R.id.btnAddJadwal)
        _btnAddJadwal.setOnClickListener {
            if (namaHari == "Senin") {
                urutan = 1
            } else if (namaHari == "Selasa") {
                urutan = 2
            } else if (namaHari == "Rabu") {
                urutan = 3
            } else if (namaHari == "Kamis") {
                urutan = 4
            } else if (namaHari == "Jumat") {
                urutan = 5
            } else if (namaHari == "Sabtu") {
                urutan = 6
            }
            tambahData(MainActivity.db,namaHari,_etAddNamaMKJadwal.text.toString(),
                _etAddRuanganJadwal.text.toString(),_etAddJamJadwal.text.toString(),_etAddPengajarJadwal.text.toString(), urutan)
            val mList_Jadwal = List_Jadwal()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, mList_Jadwal, List_Jadwal:: class. java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }


    fun tambahData(db: FirebaseFirestore, hari: String, nama_mk: String, ruangan: String, jam: String, pengajar: String, urutan: Int){
        val dataBaru = Jadwal(nama_mk,ruangan,jam, pengajar, hari, urutan)
        var judul = dataBaru.nama_mk
        if (MainActivity.position != -1 && MainActivity.page == "Add_Jadwal"){
            judul = MainActivity.judul
        }
        db.collection("tbJadwal")
            .document(judul)
            .set(dataBaru)
            .addOnSuccessListener {
                _etAddNamaMKJadwal.setText("")
                _etAddRuanganJadwal.setText("")
                _etAddJamJadwal.setText("")
                _etAddPengajarJadwal.setText("")
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
         * @return A new instance of fragment Add_Jadwal.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Add_Jadwal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item is selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos).
        namaHari = parent.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback.
    }
}