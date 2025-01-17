package paba.myapplication

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Add_Note.newInstance] factory method to
 * create an instance of this fragment.
 */
class Add_Note : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var _etAddJudul : EditText
    lateinit var _etAddNotes : EditText

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
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (MainActivity.page == "Add_Note" && MainActivity.position!=-1){
            MainActivity.db.collection("tbNote")
                .document(MainActivity.judul)
                .get()
                .addOnSuccessListener {
                        result ->
                    _etAddJudul.setText(result.data!!.get("judul").toString())
                    _etAddNotes.setText(result.data!!.get("isi").toString())
                    Log.d("data ROOM", result.toString())
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }

        }

        _etAddJudul = view.findViewById<EditText>(R.id.etAddJudulTodo)
        _etAddNotes = view.findViewById<EditText>(R.id.etAddNotesTodo)
        val _btnAddNote = view.findViewById<Button>(R.id.btnAddTodo)
        _btnAddNote.setOnClickListener {
            tambahData(MainActivity.db,_etAddJudul.text.toString(),_etAddNotes.text.toString())
            val mList_Note = List_Note()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, mList_Note, List_Note:: class. java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    fun tambahData(db: FirebaseFirestore, judul: String, isi : String){
        val dataBaru = Note(judul,isi)
        var judul = dataBaru.judul
        if (MainActivity.position != -1 && MainActivity.page == "Add_Note"){
            judul = MainActivity.judul
        }
        db.collection("tbNote")
            .document(judul)
            .set(dataBaru)
            .addOnSuccessListener {
                _etAddJudul.setText("")
                _etAddNotes.setText("")
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
         * @return A new instance of fragment Add_not.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Add_Note().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}