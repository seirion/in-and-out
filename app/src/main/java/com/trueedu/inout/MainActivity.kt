package com.trueedu.inout

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trueedu.inout.db.InOut
import com.trueedu.inout.db.InOutRecord
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    private fun initUi() {
        Log.d(TAG, "initUi()")
        recyclerView.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = Adapter(applicationContext, listOf(InOutRecord(InOut.IN, 0L)))
    }

    private class Adapter(
        applicationContext: Context,
        private val data: List<InOutRecord>
    ) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        private val inflater = LayoutInflater.from(applicationContext)!!

        override fun getItemCount() = data.size

        override fun getItemViewType(position: Int) = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = inflater.inflate(R.layout.item_inout_record, parent, false)
            return HeaderViewHolder(view)
        }

        private inner class HeaderViewHolder(view: View) : ViewHolder(view) {
            override fun bind(record: InOutRecord, position: Int) {
                root.setOnClickListener { Log.d(TAG, "click") }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(data[position], position)
        }

        open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            protected val root = view
            open fun bind(record: InOutRecord, position: Int) {}
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
