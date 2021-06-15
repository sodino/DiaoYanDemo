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
    val imgs1 = mutableListOf<String>( // 滤镜组
                        "1/自拍-原图.jpg",
                        "1/A1.jpg",
                        "1/A2.jpg",
                        "1/A3.jpg",
                        "1/A4.jpg",
                        "1/A5.jpg",
                        "1/A6.jpg",
                        "1/A7.jpg",
                        "1/A8.JPG",
                        "1/A9.JPG"
                )
    val imgs2 = mutableListOf<String>( // 素材组
                        "2/原图.jpg",
                        "2/C1.jpg",
                        "2/C10.jpg",
                        "2/C2.jpg",
                        "2/C3.jpg",
                        "2/C4.jpg",
                        "2/C5.jpg",
                        "2/C6.jpg",
                        "2/C7.jpg",
                        "2/C7翅膀补图.jpg",
                        "2/C8.jpg",
                        "2/C9.jpg"
                )
    val imgs3 = mutableListOf<String>( // 妆容组
                        "3/原图.jpg",
                        "3/B1.jpg",
                        "3/B2.jpg",
                        "3/B3.jpg",
                        "3/B4.jpg",
                        "3/B5.jpg",
                        "3/B6.jpg",
                        "3/B7.jpg",
                        "3/B8.jpg",
                        "3/B9.jpg"
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