package paba.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterNote(private val listTodo : MutableList<Note>) :
    RecyclerView.Adapter<AdapterNote.ListViewHolder>() {
        private lateinit var onItemClickCallBack: OnItemClickCallBack

    interface OnItemClickCallBack {
        fun editData(dtTodo: Note, listTodo:MutableList<Note>, position: Int)
        fun deleteData(dtTodo: Note, listTodo:MutableList<Note>, position: Int)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var _judul = itemView.findViewById<TextView>(R.id.itemJudulNote)
        var _isi = itemView.findViewById<TextView>(R.id.itemContentNote)
        var _btnEdit= itemView.findViewById<Button>(R.id.btnEditNote)
        var _btnDelete = itemView.findViewById<Button>(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterNote.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterNote.ListViewHolder, position: Int) {
        var todo = listTodo[position]

        holder._judul.setText(todo.judul)
        holder._isi.setText(todo.isi)

        holder._btnEdit.setOnClickListener {
            onItemClickCallBack.editData(todo, listTodo, position)
        }

        holder._btnDelete.setOnClickListener {
            onItemClickCallBack.deleteData(todo, listTodo, position)
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
