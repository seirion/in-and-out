package com.trueedu.inout

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    private fun initUi() {
        Log.d(TAG, "initUi()")
        val layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = Adapter(this)
    }

    private class Adapter(private val activity: Activity) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        private val inflater = LayoutInflater.from(activity.applicationContext)!!

        override fun getItemCount() = 1

        override fun getItemViewType(position: Int) =
            if (position == 0) 0 else 1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = inflater.inflate(R.layout.item_header, parent, false)
            return HeaderViewHolder(view)
        }

        private inner class HeaderViewHolder(view: View) : ViewHolder(view) {
            override fun bind(data: Int, position: Int) {
                root.setOnClickListener { Log.d(TAG, "click") }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(position, position)
        }

        open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            protected val root = view
            open fun bind(data: Int, position: Int) {}
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
