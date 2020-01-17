package com.trueedu.inout

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trueedu.inout.db.InOut
import com.trueedu.inout.db.InOutRecord
import com.trueedu.inout.db.InOutRecordBase
import com.trueedu.inout.utils.toDateString
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    @SuppressLint("CheckResult")
    private fun init() {
        Single.fromCallable { InOutRecordBase.getInstance(applicationContext) }
            .map { it.inOutRecordDao().getAll() }
            .map { it.toMutableList() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { Log.d(TAG, "load records") }
            .subscribe({ initUi(it) }, { it.printStackTrace() /* ignore exceptions*/ })
    }

    private fun initUi(records: MutableList<InOutRecord>) {
        Log.d(TAG, "initUi()")
        recyclerView.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        adapter = Adapter(applicationContext, records, this::deleteInOutRecord)
        recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        inButton.setOnClickListener {
            Log.d(TAG, "click inButton")
            putInOutRecord(InOut.IN)
        }
        outButton.setOnClickListener {
            Log.d(TAG, "click outButton")
            putInOutRecord(InOut.OUT)
        }
    }

    @SuppressLint("CheckResult")
    private fun putInOutRecord(inOut: InOut) {
        val record = InOutRecord(inOut, Calendar.getInstance().timeInMillis)
        adapter.put(record)
        adapter.notifyDataSetChanged()
        Single.fromCallable { InOutRecordBase.getInstance(applicationContext) }
            .subscribeOn(Schedulers.io())
            .subscribe( { it.inOutRecordDao().insert(record) }, {})
    }

    @SuppressLint("CheckResult")
    private fun deleteInOutRecord(record: InOutRecord) {
        adapter.notifyDataSetChanged()
        Single.fromCallable { InOutRecordBase.getInstance(applicationContext) }
            .subscribeOn(Schedulers.io())
            .subscribe( { it.inOutRecordDao().delete(record) }, {})
    }

    class Adapter(
        applicationContext: Context,
        private val data: MutableList<InOutRecord>,
        private val deleteCallback: (InOutRecord) -> Unit
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

        fun deleteItem(position: Int) {
            val record = data[position]
            data.removeAt(position)
            notifyDataSetChanged()
            deleteCallback(record)
        }

        private inner class HeaderViewHolder(view: View) : ViewHolder(view) {
            override fun bind(record: InOutRecord, position: Int) {
                root.setBackgroundResource(
                    if (record.inOut == InOut.IN) R.drawable.bg_round_in
                    else R.drawable.bg_round_out
                )
                root.setOnClickListener { Log.d(TAG, "click") }
                root.findViewById<TextView>(R.id.inoutIcon).let {
                    it.setBackgroundResource(
                        if (record.inOut == InOut.IN) R.drawable.button_in
                        else R.drawable.button_out
                    )
                }
                root.findViewById<TextView>(R.id.dateTextView).let {
                    it.text = toDateString(record.timestamp)
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

    class SwipeToDeleteCallback(private val adapter: Adapter) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            adapter.deleteItem(position)
        }
    }
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
