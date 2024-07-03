package paba.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
 * Use the [List_Jadwal.newInstance] factory method to
 * create an instance of this fragment.
 */
class List_Jadwal : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var adapterJadwal : AdapterJadwal
    private var arJadwal :MutableList<Jadwal> = mutableListOf()

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
        return inflater.inflate(R.layout.fragment_list__jadwal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _btnAddJadwal = view.findViewById<FloatingActionButton>(R.id.btnPageAddJadwal)

        _btnAddJadwal.setOnClickListener {
            val mAdd_Jadwal = Add_Jadwal()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, mAdd_Jadwal, Add_Jadwal:: class. java.simpleName)
                addToBackStack(null)
                commit()
            }
        }

        adapterJadwal = AdapterJadwal(arJadwal)
        val _rvJadwal = view.findViewById<RecyclerView>(R.id.rvListJadwal)
        _rvJadwal.layoutManager = LinearLayoutManager(MainActivity())
        _rvJadwal.adapter = adapterJadwal

        readData()

        adapterJadwal.setOnItemClickCallBack(
            object : AdapterJadwal.OnItemClickCallBack{
                override fun deleteData(
                    dtJadwal: Jadwal,
                    listJadwal: MutableList<Jadwal>,
                    position: Int
                ) {
                    CoroutineScope(Dispatchers.IO).async {
                        val temp = listJadwal[position]
                        MainActivity.db.collection("tbJadwal")
                            .document(temp.nama_mk)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("Firebase", "Berhasil diHapus")
                                readData()
                            }.addOnFailureListener { e ->
                                Log.w("Firebase", e.message.toString())
                            }
                    }
                }
            }
        )
    }

    fun readData(){
        CoroutineScope(Dispatchers.Main).async {
            MainActivity.db.collection("tbJadwal").orderBy("urutan", Query.Direction.ASCENDING)
                .get().addOnSuccessListener {
                    result ->
                var temp : ArrayList<Jadwal> = arrayListOf()
                for (document in result){
                    val readData = Jadwal(
                        document.data.get("nama_mk").toString(),
                        document.data.get("ruangan").toString(),
                        document.data.get("jam").toString(),
                        document.data.get("pengajar").toString(),
                        document.data.get("hari").toString(),
                        document.data.get("urutan").toString().toInt()
                    )
                    temp.add(readData)
                    Log.d("data ROOM", readData.toString())
                }
                adapterJadwal.isiData(temp)
                Log.d("data ROOM", temp.toString())
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
         * @return A new instance of fragment List_Jadwal.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            List_Jadwal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}