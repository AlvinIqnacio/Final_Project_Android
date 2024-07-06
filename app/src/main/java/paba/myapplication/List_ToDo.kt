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
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
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
 * Use the [List_ToDo.newInstance] factory method to
 * create an instance of this fragment.
 */
class List_ToDo : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var adapterTodo : AdapterToDo
    private var arTodo :MutableList<ToDo> = mutableListOf()


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
        return inflater.inflate(R.layout.fragment_list_to_do, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _btnAddTodo = view.findViewById<FloatingActionButton>(R.id.btnPageAddToDo)

        _btnAddTodo.setOnClickListener {
            val mAdd_ToDo = Add_ToDo()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, mAdd_ToDo, Add_ToDo:: class. java.simpleName)
                addToBackStack(null)
                commit()
            }
        }

        adapterTodo = AdapterToDo(arTodo)
        val _rvTodo = view.findViewById<RecyclerView>(R.id.rvListToDo)
        _rvTodo.layoutManager = LinearLayoutManager(MainActivity())
        _rvTodo.adapter = adapterTodo

        readData()

        adapterTodo.setOnItemClickCallBack(
            object : AdapterToDo.OnItemClickCallBack{

                override fun selesaiData(dtTodo: ToDo, listTodo: MutableList<ToDo>, position: Int, check: Boolean) {
                    CoroutineScope(Dispatchers.IO).async {
                        val temp = listTodo[position]
                        temp.checked = check
                        MainActivity.db.collection("tbToDo")
                            .document(temp.judul)
                            .set(temp)
                            .addOnSuccessListener {
                                Log.d("Firebase", "Berhasil diUpdate")
                                readData()
                            }.addOnFailureListener { e ->
                                Log.w("Firebase", e.message.toString())
                            }
                    }
                }

                override fun editData(dtTodo: ToDo, listTodo: MutableList<ToDo>, position: Int) {
                    MainActivity.page = "Add_ToDo"
                    MainActivity.position = position
                    MainActivity.judul = dtTodo.judul

                    val mAdd_ToDo = Add_ToDo()
                    val mFragmentManager = parentFragmentManager
                    mFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, mAdd_ToDo, Add_ToDo:: class. java.simpleName)
                        addToBackStack(null)
                        commit()
                    }
                }

                override fun deleteData(dtTodo: ToDo, listTodo: MutableList<ToDo>, position: Int) {
                    CoroutineScope(Dispatchers.IO).async {
                        val temp = listTodo[position]
                        MainActivity.db.collection("tbToDo")
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
            MainActivity.db.collection("tbToDo").orderBy("checked", Query.Direction.ASCENDING)
                .get().addOnSuccessListener {
                    result ->
                var temp : ArrayList<ToDo> = arrayListOf()
                for (document in result){
                    val readData = ToDo(
                        document.data.get("judul").toString(),
                        document.data.get("isi").toString(),
                        document.data.get("waktu").toString(),
                        document.data.get("checked").toString().toBoolean()
                    )
                    temp.add(readData)
                    Log.d("data ROOM", readData.toString())
                }
                adapterTodo.isiData(temp)
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
         * @return A new instance of fragment List_ToDo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            List_ToDo().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}