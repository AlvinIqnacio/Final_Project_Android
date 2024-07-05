package paba.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailNilai.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailNilai : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var nama_mk : String
    lateinit var lvAdapter : SimpleAdapter
    var data: MutableList<Map<String, String>> = ArrayList()

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
        return inflater.inflate(R.layout.fragment_detail_nilai, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nama_mk = arguments?.getString("nama_mk").toString()
        val _lvDetail = view.findViewById<ListView>(R.id.lvDetail)
        lvAdapter = SimpleAdapter(
            this.context,
            data,
            android.R.layout.simple_list_item_2,
            arrayOf<String>("field","nilai"),
            intArrayOf(
                android.R.id.text1,
                android.R.id.text2
            )
        )
        _lvDetail.adapter = lvAdapter

        readData()
    }

    fun readData(){
        var dtDocument: MutableMap<String, String> = HashMap()
        CoroutineScope(Dispatchers.Main).async {
            MainActivity.db.collection("tbNilai")
                .document(nama_mk).get()
                .addOnSuccessListener {
                    result ->
                    result.data?.forEach {
                        val dt: MutableMap<String, String> = HashMap(2)
                        if (it.key != "nama" && it.key != "totalNilai"){
                            dt["field"] = it.key.toString()
                            dt["nilai"] = it.value.toString()
                            data.add(dt)
                        }
                    }
                    lvAdapter.notifyDataSetChanged()
                    Log.d("data ROOM", nama_mk)
            }.addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailNilai.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailNilai().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}