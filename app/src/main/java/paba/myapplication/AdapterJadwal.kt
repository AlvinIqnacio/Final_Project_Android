package paba.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterJadwal (private val listJadwal : MutableList<Jadwal>) :
    RecyclerView.Adapter<AdapterJadwal.ListViewHolder>() {
    private lateinit var onItemClickCallBack: OnItemClickCallBack
    var listHari : MutableList<String> = ArrayList()

    interface OnItemClickCallBack {
//        fun editData(dtTodo: Note, listTodo:MutableList<Note>, position: Int)
        fun deleteData(dtJadwal: Jadwal, listJadwal:MutableList<Jadwal>, position: Int)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var _hari = itemView.findViewById<TextView>(R.id.itemHari)
        var _namaMK = itemView.findViewById<TextView>(R.id.itemNamaMK)
        var _ruangan = itemView.findViewById<TextView>(R.id.itemRuangan)
        var _jam = itemView.findViewById<TextView>(R.id.itemJam)
        var _pengajar = itemView.findViewById<TextView>(R.id.itemPengajar)
        var _btnDeleteJadwal = itemView.findViewById<ImageButton>(R.id.btnDeleteJadwal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterJadwal.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterJadwal.ListViewHolder, position: Int) {
        var jadwal = listJadwal[position]


        holder._hari.setText(jadwal.hari)
        if (jadwal.hari == "Senin"){
            holder._hari.setBackgroundColor(Color.parseColor("#2196F3"))
        } else if (jadwal.hari == "Selasa"){
            holder._hari.setBackgroundColor(Color.parseColor("#4CAF50"))
        } else if (jadwal.hari == "Rabu"){
            holder._hari.setBackgroundColor(Color.parseColor("#F44336"))
        } else if (jadwal.hari == "Kamis"){
            holder._hari.setBackgroundColor(Color.parseColor("#673AB7"))
        } else if (jadwal.hari == "Jumat"){
            holder._hari.setBackgroundColor(Color.parseColor("#FF9800"))
        } else if (jadwal.hari == "Sabtu"){
            holder._hari.setBackgroundColor(Color.parseColor("#000000"))
        }
        holder._namaMK.setText(jadwal.nama_mk)
        holder._ruangan.setText(jadwal.ruangan)
        holder._jam.setText(jadwal.jam)
        holder._pengajar.setText(jadwal.pengajar)

//        holder._btnEdit.setOnClickListener {
//            onItemClickCallBack.editData(todo, listJadwal, position)
//        }

        holder._btnDeleteJadwal.setOnClickListener {
            onItemClickCallBack.deleteData(jadwal, listJadwal, position)
        }
    }

    override fun getItemCount(): Int {
        return listJadwal.size
    }

    fun isiData(list: List<Jadwal>){
        listJadwal.clear()
        listJadwal.addAll(list)
        notifyDataSetChanged()
    }

}