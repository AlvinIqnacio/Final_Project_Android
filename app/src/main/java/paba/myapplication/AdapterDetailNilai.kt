package paba.myapplication

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterDetailNilai(private val listNilai : MutableList<Map<String,String>>) :
    RecyclerView.Adapter<AdapterDetailNilai.ListViewHolder>() {
    private lateinit var onItemClickCallBack: OnItemClickCallBack
    var editBool :Boolean = false
    var used = "no"

    interface OnItemClickCallBack {
//        fun editData(dtTodo: Note, listTodo:MutableList<Note>, position: Int)
//        fun deleteData(dtTodo: Note, listTodo:MutableList<Note>, position: Int)
        fun editData(namaDetailNilai : String, value : String)
        fun deleteData(namaDetailNilai: String)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var _nama = itemView.findViewById<TextView>(R.id.namaNilaiDetailNilai)
        var _nilai = itemView.findViewById<TextView>(R.id.itemNilaiDetailNilai)
        var _btnEdit = itemView.findViewById<Button>(R.id.btnEditDetailNilai)
        var _btnDelete = itemView.findViewById<Button>(R.id.btnDeleteDetailNilai)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterDetailNilai.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_nilai,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterDetailNilai.ListViewHolder, position: Int) {
        var nama = listNilai[position]
        Log.d("check", nama.toString())
        holder._nama.setText(nama["field"])
        holder._nilai.setText(nama["nilai"])

        if (used=="no"){
            holder._nilai.isEnabled = false
        }

        holder._btnEdit.setOnClickListener {
            used = "yes"
            if (editBool){
                editBool = false
                onItemClickCallBack.editData(nama["field"].toString(), holder._nilai.text.toString())
                holder._btnEdit.setText("Edit")
            }else{
                editBool = true
                holder._btnEdit.setText("Save")
                holder._btnEdit.setBackgroundColor(Color.parseColor("#4CAF50"))
            }
            holder._nilai.isEnabled = editBool
            notifyDataSetChanged()
        }

        holder._btnDelete.setOnClickListener{
            onItemClickCallBack.deleteData(nama["field"].toString())
        }
    }

    override fun getItemCount(): Int {
        return listNilai.size
    }

    fun isiData(list:  MutableList<Map<String,String>>){
        listNilai.clear()
        listNilai.addAll(list)
        Log.d("check", list.toString())
        notifyDataSetChanged()
    }

    fun getListNilai(): MutableList<Map<String, String>> {
        return listNilai
    }


}