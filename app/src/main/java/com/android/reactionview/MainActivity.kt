package com.android.reactionview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reactionView.setVisibility(View.INVISIBLE)
        btLike.setOnLongClickListener {
            reactionView.show()
            true
        }
    }
}
