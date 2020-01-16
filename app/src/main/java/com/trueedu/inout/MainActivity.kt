package com.trueedu.inout

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trueedu.inout.db.InOut
import com.trueedu.inout.db.InOutRecord
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    private fun initUi() {
        Log.d(TAG, "initUi()")
        recyclerView.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        adapter = Adapter(applicationContext, mutableListOf(InOutRecord(InOut.IN, 0L)))
        recyclerView.adapter = adapter

        inButton.setOnClickListener {
            Log.d(TAG, "click inButton")
            adapter.put(InOutRecord(InOut.IN, Calendar.getInstance().timeInMillis))
            adapter.notifyDataSetChanged()
        }
        outButton.setOnClickListener {
            Log.d(TAG, "click outButton")
            adapter.put(InOutRecord(InOut.OUT, Calendar.getInstance().timeInMillis))
            adapter.notifyDataSetChanged()
        }
    }

    private class Adapter(
        applicationContext: Context,
        private val data: MutableList<InOutRecord>
    ) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        private val inflater = LayoutInflater.from(applicationContext)!!

        override fun getItemCount() = data.size

        override fun getItemViewType(position: Int) = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = inflater.inflate(R.layout.item_inout_record, parent, false)
            return HeaderViewHolder(view)
        }

        fun put(record: InOutRecord) {
            data.add(record)
        }

        private inner class HeaderViewHolder(view: View) : ViewHolder(view) {
            override fun bind(record: InOutRecord, position: Int) {
                root.setBackgroundColor(
                    if (record.inOut == InOut.IN) Color.rgb(220, 181, 255)
                    else Color.rgb(182, 187, 222)
                )
                root.setOnClickListener { Log.d(TAG, "click") }
                root.findViewById<TextView>(R.id.inoutIcon).let {
                    it.setBackgroundResource(
                        if (record.inOut == InOut.IN) R.drawable.button_in
                        else R.drawable.button_out
                    )
                }
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
