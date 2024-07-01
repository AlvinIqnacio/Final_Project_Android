package paba.myapplication

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterTodo(private val listTodo : MutableList<Note>) :
    RecyclerView.Adapter<AdapterTodo.ListViewHolder>() {
        private lateinit var onItemClickCallBack: OnItemClickCallBack

    interface OnItemClickCallBack {
        fun delData(dtTodo: Note, listTodo:MutableList<Note>, position: Int)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var _judul = itemView.findViewById<TextView>(R.id.itemJudulNote)
        var _isi = itemView.findViewById<TextView>(R.id.itemContentNote)
        var _btnSelesai= itemView.findViewById<Button>(R.id.btnEditNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterTodo.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterTodo.ListViewHolder, position: Int) {
        var todo = listTodo[position]

        holder._judul.setText(todo.judul)
        holder._isi.setText(todo.isi)

        holder._btnSelesai.setOnClickListener {
            onItemClickCallBack.delData(todo, listTodo, position)
        }
    }

    override fun getItemCount(): Int {
        return listTodo.size
    }

    fun isiData(list: List<Note>){
        listTodo.clear()
        listTodo.addAll(list)
        notifyDataSetChanged()
    }

}
