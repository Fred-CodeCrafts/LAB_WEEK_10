package com.fredcodecrafts.lab_week_10



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareViewModel()
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {
        // Observe total from ViewModel
        updateText(viewModel.total) // first update
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
            updateText(viewModel.total)
        }
    }
}
