package com.sodino.photos

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ImgAdapter : RecyclerView.Adapter<ImgVH>() {
    private val list = LinkedList<String>()
    private val map = mutableMapOf<String, Bitmap>()
    private var selectPath = ""
    private val clickListener = View.OnClickListener {
        val activity = it.context
        if (activity !is MainActivity) {
            return@OnClickListener
        }
        val path = it.getTag(R.id.img)
        if (path !is String) {
            return@OnClickListener
        }
        val position = it.getTag(R.id.imgOK)
        if (position !is Int) {
            return@OnClickListener
        }
        activity.reSetPreview(path, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgVH {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item, parent, false)
        val vh = ImgVH(view, clickListener)
        return vh
    }

    override fun onBindViewHolder(holder: ImgVH, position: Int) {
        val path = list.getOrNull(position) ?: return
        holder.itemView.setTag(R.id.img, path)
        holder.itemView.setTag(R.id.imgOK, position)
        holder.imgView.setImageBitmap(map[path])
        val visibility = if (path == selectPath) {
                                View.VISIBLE
                            } else {
                                View.INVISIBLE
                            }
        holder.imgOK.visibility = visibility
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(list: List<String>, mapBitmap: MutableMap<String, Bitmap>) {
        this.list.clear()
        this.list.addAll(list)
        map.clear()
        map.putAll(mapBitmap)
        notifyDataSetChanged()
    }

    fun setSelectPath(path: String) {
        this.selectPath = path
        notifyDataSetChanged()
    }
}

class ImgVH(
    view: View,
    listener: View.OnClickListener
) : RecyclerView.ViewHolder(view) {
    val imgView : ImageView = view.findViewById(R.id.img)
    val imgOK : ImageView = view.findViewById(R.id.imgOK)

    init {
        view.setOnClickListener(listener)
        imgOK.setColorFilter(Color.WHITE)
    }
}