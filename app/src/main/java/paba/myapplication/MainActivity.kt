package paba.myapplication

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mFragmentManager = supportFragmentManager
        val mList_Note =List_Note()

        mFragmentManager.findFragmentByTag(List_Note::class.java.simpleName)
        mFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainerView, mList_Note, mList_Note::class.java.simpleName)
            .commit()

        val btnListNote = findViewById<Button>(R.id.btnPageNote)
        btnListNote.setOnClickListener {
            mFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, mList_Note, List_Note::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }

        val btnToDoList = findViewById<Button>(R.id.btnPageToDoList)
        btnToDoList.setOnClickListener {
            val mTodo = List_ToDo()
            mFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, mTodo, List_ToDo::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }
}