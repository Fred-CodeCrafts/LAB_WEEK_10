package com.fredcodecrafts.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.fredcodecrafts.lab_week_10.database.Total
import com.fredcodecrafts.lab_week_10.database.TotalDatabase
import com.fredcodecrafts.lab_week_10.database.TotalObject
import com.fredcodecrafts.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val db by lazy { prepareDatabase() }
    private val viewModel by lazy { ViewModelProvider(this)[TotalViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeValueFromDatabase()
        prepareViewModel()
    }

    private fun prepareDatabase(): TotalDatabase = Room.databaseBuilder(
        applicationContext, TotalDatabase::class.java, "total-database"
    ).allowMainThreadQueries().build()

    private fun initializeValueFromDatabase() {
        val totalList = db.totalDao().getTotal(ID)
        if (totalList.isEmpty())
            db.totalDao().insert(Total(id = 1, total = TotalObject(0, Date().toString())))
        else
            viewModel.setTotal(totalList.first().total.value)
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this) { total -> updateText(total) }
        findViewById<Button>(R.id.button_increment).setOnClickListener { viewModel.incrementTotal() }
    }

    override fun onPause() {
        super.onPause()
        val timestamp = Date().toString()
        db.totalDao().update(Total(ID, TotalObject(viewModel.total.value!!, timestamp)))
    }

    override fun onStart() {
        super.onStart()
        val total = db.totalDao().getTotal(ID).first()
        Toast.makeText(this, "Last updated: ${total.total.date}", Toast.LENGTH_SHORT).show()
    }

    companion object { const val ID: Long = 1 }
}
