package paba.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [List_Note.newInstance] factory method to
 * create an instance of this fragment.
 */
class List_Note : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var adapterNote : AdapterNote
    private var arTodo :MutableList<Note> = mutableListOf()

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
        return inflater.inflate(R.layout.fragment_list__note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _btnAddNote = view.findViewById<FloatingActionButton>(R.id.btnPageAddNote)

        _btnAddNote.setOnClickListener {
            val mAdd_Note = Add_Note()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, mAdd_Note, Add_Note:: class. java.simpleName)
                addToBackStack(null)
                commit()
            }
        }

        adapterNote = AdapterNote(arTodo)
        val _rvTodo = view.findViewById<RecyclerView>(R.id.rvListNote)
        _rvTodo.layoutManager = LinearLayoutManager(MainActivity())
        _rvTodo.adapter = adapterNote

        readData()

        adapterNote.setOnItemClickCallBack(
            object : AdapterNote.OnItemClickCallBack{
                override fun editData(dtNote: Note, listTodo:MutableList<Note>, position: Int) {
                    MainActivity.page = "Add_Note"
                    MainActivity.position = position
                    MainActivity.judul = dtNote.judul

                    val mAdd_Note = Add_Note()
                    val mFragmentManager = parentFragmentManager
                    mFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, mAdd_Note, Add_Note:: class. java.simpleName)
                        addToBackStack(null)
                        commit()
                    }

                }

                override fun deleteData(dtNote: Note, listTodo: MutableList<Note>, position: Int) {
                    CoroutineScope(Dispatchers.IO).async {
                        val temp = listTodo[position]
                        MainActivity.db.collection("tbNote")
                            .document(temp.judul)
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
            MainActivity.db.collection("tbNote").get().addOnSuccessListener {
                    result ->
                var temp : ArrayList<Note> = arrayListOf()
                for (document in result){
                    val readData = Note(
                        document.data.get("judul").toString(),
                        document.data.get("isi").toString()
                    )
                    temp.add(readData)
                    Log.d("data ROOM", readData.toString())
                }
                adapterNote.isiData(temp)
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
         * @return A new instance of fragment List_Note.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            List_Note().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}