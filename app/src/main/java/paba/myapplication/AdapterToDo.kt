package paba.myapplication

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterToDo(private val listTodo : MutableList<ToDo>) :
    RecyclerView.Adapter<AdapterToDo.ListViewHolder>() {
        private lateinit var onItemClickCallBack: OnItemClickCallBack

    interface OnItemClickCallBack {
        fun selesaiData(dtTodo: ToDo, listTodo:MutableList<ToDo>, position: Int, check : Boolean)

        fun editData(dtTodo: ToDo, listTodo:MutableList<ToDo>, position: Int)

        fun deleteData(dtTodo: ToDo, listTodo:MutableList<ToDo>, position: Int)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var _judul = itemView.findViewById<TextView>(R.id.itemJudulTodo)
        var _isi = itemView.findViewById<TextView>(R.id.itemIsiTodo)
        var _waktu = itemView.findViewById<TextView>(R.id.waktuItemTodo)
        var _btnEdit= itemView.findViewById<Button>(R.id.btnEditTodo)
        var _btnDelete = itemView.findViewById<Button>(R.id.btnDeleteTodo)
        var checkBox = itemView.findViewById<CheckBox>(R.id.itemIsFinished)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterToDo.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterToDo.ListViewHolder, position: Int) {
        var todo = listTodo[position]

        Log.d("test",todo.toString())
        holder._judul.setText(todo.judul)
        holder._isi.setText(todo.isi)
        holder._waktu.setText(todo.waktu)

        holder.checkBox.setChecked(todo.checked)

        if (todo.checked){
            holder._judul.setTextColor(Color.parseColor("#FFCDCDCD"))
            holder._isi.setTextColor(Color.parseColor("#FFCDCDCD"))
            holder._waktu.setTextColor(Color.parseColor("#FFCDCDCD"))
        } else {
            holder._judul.setTextColor(Color.parseColor("#00ADB5"))
            holder._isi.setTextColor(Color.parseColor("#000000"))
            holder._waktu.setTextColor(Color.parseColor("#000000"))
        }

        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            onItemClickCallBack.selesaiData(todo, listTodo, position, isChecked)
        }

        holder._btnDelete.setOnClickListener {
            onItemClickCallBack.deleteData(todo, listTodo, position)
        }

        holder._btnEdit.setOnClickListener {
            onItemClickCallBack.editData(todo, listTodo, position)
        }
    }

    override fun getItemCount(): Int {
        return listTodo.size
    }

    fun isiData(list: List<ToDo>){
        listTodo.clear()
        listTodo.addAll(list)
        notifyDataSetChanged()
    }

}
