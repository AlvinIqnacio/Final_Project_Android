package paba.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Add_Detail_Nilai.newInstance] factory method to
 * create an instance of this fragment.
 */
class Add_Detail_Nilai : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var nama_mk : String
    lateinit var _etAddNamaDetailNilai : EditText
    lateinit var _etAddNilaiDetailNilai : EditText

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
        return inflater.inflate(R.layout.fragment_add__detail__nilai, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nama_mk = arguments?.getString("nama_mk").toString()
        _etAddNamaDetailNilai= view.findViewById<EditText>(R.id.etNamaDetailNilai)
        _etAddNilaiDetailNilai= view.findViewById<EditText>(R.id.etNilaiDetailNilai)

        val _btnSave = view.findViewById<Button>(R.id.btnSaveAddDetailNilai)
        _btnSave.setOnClickListener {
            val mBundleNama = Bundle()
            mBundleNama.putString("nama_mk", nama_mk)

            val mDetailNilai = DetailNilai()
            mDetailNilai.arguments = mBundleNama

            tambahData(nama_mk,_etAddNamaDetailNilai.text.toString(), _etAddNilaiDetailNilai.text.toString())
            updateTotalNilai(nama_mk)

            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, mDetailNilai, DetailNilai:: class. java.simpleName)
                addToBackStack(null)
                commit()
            }

        }
    }

    fun updateTotalNilai(nama_mk: String){
        var listNilai : MutableList<Map<String,String>> = arrayListOf()
        MainActivity.db.collection("tbNilai")
            .document(nama_mk).get()
            .addOnSuccessListener {
                    result ->
                result.data?.forEach {
                    val dt: MutableMap<String, String> = HashMap(2)
                    if (it.key != "nama" && it.key != "totalNilai"){
                        dt["field"] = it.key.toString()
                        dt["nilai"] = it.value.toString()
                        listNilai.add(dt)
                    }
                }
                Log.d("checkresult", listNilai.toString())
                var total = 0
                for (nilai in listNilai){
                    total += nilai["nilai"]!!.toInt()
                }
                if  (listNilai.size!=0){
                    total /= listNilai.size
                }

                MainActivity.db.collection("tbNilai")
                    .document(nama_mk)
                    .update("totalNilai",total)
                    .addOnSuccessListener {
                        Log.d("TotalNilai", "Berhasil diUpdate")
                    }.addOnFailureListener { e ->
                        Log.w("Firebase", e.message.toString())
                    }
            }.addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }

    fun tambahData(nama_mk: String, nama:String, value: String){
        val map: MutableMap<String, String> = HashMap(2)
        map.put(nama,value)
        val userDocumentReference = MainActivity.db.collection("tbNilai").document(nama_mk)
        userDocumentReference.set(map, SetOptions.merge()).addOnSuccessListener {
            Log.d("sukses", "sukess")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Add_Detail_Nilai.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Add_Detail_Nilai().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}