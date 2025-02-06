package com.example.gdlibrary

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private lateinit var imageView: ImageView
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector

    private var scaleFactor = 1.0f
    private val images = arrayOf(
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3,
        R.drawable.img4
    )

    private var currentImageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        setImage()

        gestureDetector = GestureDetector(this, this)
        gestureDetector.setOnDoubleTapListener(this)

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
    }

    private fun setImage() {
        imageView.setImageResource(images[currentImageIndex])
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    override fun onSingleTapConfirmed(p0: MotionEvent): Boolean {
        Toast.makeText(this, "Изображение №${currentImageIndex + 1}", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onDoubleTap(p0: MotionEvent): Boolean {
        scaleFactor *= 1.5f
        setImageScaleFactor()
        return true
    }

    private fun setImageScaleFactor() {
        imageView.scaleX = scaleFactor
        imageView.scaleY = scaleFactor
    }

    override fun onLongPress(p0: MotionEvent) {
        scaleFactor = 1.0f
        setImageScaleFactor()
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        val sensitivity = 50
        if (p0 != null) {
            if (p0.x - p1.x > sensitivity) {
                handleSwipe(true)
            } else if (p1.x - p0.x > sensitivity) {
                handleSwipe(false)
            }
        }
        return true
    }

    private fun handleSwipe(leftSwipe: Boolean) {
        if (leftSwipe) {
            currentImageIndex = (currentImageIndex + 1) % images.size
        } else {
            currentImageIndex = (currentImageIndex - 1 + images.size) % images.size
        }
        setImage()  // Обновляем изображение после свайпа
    }

    override fun onDown(p0: MotionEvent): Boolean = true

    override fun onShowPress(p0: MotionEvent) {}

    override fun onSingleTapUp(p0: MotionEvent): Boolean = false

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean = false

    override fun onDoubleTapEvent(p0: MotionEvent): Boolean = false

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(0.1f, 10.0f)
            setImageScaleFactor()
            return true
        }
    }
}