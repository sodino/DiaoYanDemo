package com.sodino.photos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class MainActivity :
    AppCompatActivity(),
    CoroutineScope by MainScope(), View.OnClickListener {
    val imgs1 = mutableListOf<String>(
                        "1/1原图.jpg",
                        "1/1A.jpg",
                        "1/1B.jpg",
                        "1/1C.jpg",
                        "1/1D.jpg",
                        "1/1E.jpg",
                        "1/1F.jpg"
                )
    val imgs2 = mutableListOf<String>(
                        "2/2原图.jpg",
                        "2/2A.jpg",
                        "2/2B.jpg",
                        "2/2C.jpg",
                        "2/2D.jpg",
                        "2/2E.jpg",
                        "2/2F.jpg"
                )
    val imgs3 = mutableListOf<String>(
                        "3/3原图.jpg",
                        "3/3A.jpg",
                        "3/3B.jpg",
                        "3/3C.jpg",
                        "3/3D.jpg",
                        "3/3E.jpg",
                        "3/3F.jpg"
                )
    val mapBitmap = mutableMapOf<String, Bitmap>()
    val adapter = ImgAdapter()
    var currentImgs : List<String> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DemoDecoration())

        initViews()

        initCurrentImgs(imgs1)
    }

    private fun initViews() {
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        img.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val firstPath = currentImgs.firstOrNull() ?: return true
                val nowImgPath = v.getTag(R.id.img)
                if (nowImgPath !is String) {
                    return true
                }
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val firstBitmap = mapBitmap[firstPath] ?: return true
                    img.setImageBitmap(firstBitmap)
                    return true
                }
                if (event.action == MotionEvent.ACTION_UP) {
                    val nowImgBitmap = mapBitmap[nowImgPath] ?: return true
                    img.setImageBitmap(nowImgBitmap)
                    return true
                }

                return true
            }
        }
        )
    }

    private fun initCurrentImgs(list : List<String>) {
        currentImgs = list
        mapBitmap.clear()
        list.forEach {
            val inStream = assets.open(it)
            val bitmap = BitmapFactory.decodeStream(inStream)
            mapBitmap[it] = bitmap
        }

        adapter.update(list, mapBitmap)

        reSetPreview(list.first(), 0)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.btn1 -> {initCurrentImgs(imgs1)}
            R.id.btn2 -> {initCurrentImgs(imgs2)}
            R.id.btn3 -> {initCurrentImgs(imgs3)}
        }
    }

    fun reSetPreview(path: String, position : Int) {
        val bitmap = mapBitmap[path] ?: return
//        Glide.with(img)
//            .load(bitmap)
//            .into(img)
        img.setImageBitmap(bitmap)
        img.setTag(R.id.img, path)
        adapter.setSelectPath(path)
        recyclerView.scrollToPosition(position)

        val name = path.substring(2, path.length -4)
        txtName.text = name
    }
}