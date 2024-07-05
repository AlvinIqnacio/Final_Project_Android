package paba.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterNilai(private val listNilai : MutableList<Nilai>) :
    RecyclerView.Adapter<AdapterNilai.ListViewHolder>() {
    private lateinit var onItemClickCallBack: OnItemClickCallBack

    interface OnItemClickCallBack {
//        fun editData(dtTodo: Note, listTodo:MutableList<Note>, position: Int)
//        fun deleteData(dtTodo: Note, listTodo:MutableList<Note>, position: Int)
        fun seeData(nama_mk : String)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var _nama = itemView.findViewById<TextView>(R.id.namaNilai)
        var _totalNilai = itemView.findViewById<TextView>(R.id.totalNilai)
        var _btnToDetail = itemView.findViewById<ImageButton>(R.id.btnToDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterNilai.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nilai,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterNilai.ListViewHolder, position: Int) {
        var nilai = listNilai[position]

        holder._nama.setText(nilai.nama)
        holder._totalNilai.setText(nilai.totalNilai)

        holder._btnToDetail.setOnClickListener {
            onItemClickCallBack.seeData(nilai.nama)
        }

//        holder._btnEdit.setOnClickListener {
//            onItemClickCallBack.editData(todo, listTodo, position)
//        }
//
//        holder._btnDelete.setOnClickListener {
//            onItemClickCallBack.deleteData(todo, listTodo, position)
//        }
    }

    override fun getItemCount(): Int {
        return listNilai.size
    }

    fun isiData(list: List<Nilai>){
        listNilai.clear()
        listNilai.addAll(list)
        notifyDataSetChanged()
    }

}