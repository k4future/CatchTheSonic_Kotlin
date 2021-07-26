package com.hegsam.catchthesonic

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    //CONFIG
    private var score : Int = 0 //Startup Score DEFAULT:0
    private val time : Int = 31 //Startup Time In Seconds DEFAULT:31
    private val sonicSpeed : Long = 500 //Sonic Respawn Speed In Miliseconds, 1000 Milisecond = 1 Second DEFAULT : 500
    private val timeMultiplier : Float = 1f //DEFAULT:1f
    /* Examples
    1x = 1f
    1.25x = 1.25f
    1.5x = 1.5f
    1.75x = 1.75f
    2x = 2f
    */

    //CONFIG END

    private var gameOver = false
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private var imageViewList = ArrayList<ImageView>(8)
    private var spawnedIndices = ArrayList<Int>(8)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //CUSTOMIZABLE (IF YOU WANT USE XML YOU SHOULD DELETE THOSE LINES)

        //YOU CAN EDIT ALL TEXTS IN TRANSLATION EDITOR AND YOU CAN ADD MORE LANGUAGE SUPPORT
        //DO NOT REMOVE %1$s FORMAT ARG IN EDITOR OR STRINGS FILE

        textView_Time.text = getString(R.string.time_text,time.toString())
        textView_Score.text = getString(R.string.score_text,score.toString())

        textView_Time.gravity = Gravity.CENTER
        textView_Score.gravity = Gravity.CENTER

        textView_Time.setTextSize(TypedValue.COMPLEX_UNIT_SP,24f) //24SP
        textView_Score.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f) //18SP

        //IF YOU WANT TO CHANGE THE TEXT COLORS REMOVE THESE COMMENT LINES AND EDIT AS YOU WISH
        //textView_Time.setTextColor(Color.RED)
        //textView_Score.setTextColor(Color.BLUE)

        //CUSTOMIZABLE END

        //IMAGE VIEWS
        imageViewList.add(imageView2)
        imageViewList.add(imageView3)
        imageViewList.add(imageView4)
        imageViewList.add(imageView5)
        imageViewList.add(imageView6)
        imageViewList.add(imageView7)
        imageViewList.add(imageView8)
        imageViewList.add(imageView9)
        imageViewList.add(imageView10)
        //IMAGE VIEWS END

        randomSonic()

        object : CountDownTimer((time*1000).toLong(),(timeMultiplier*1000).toLong()){
            override fun onTick(millisUntilFinished: Long) {
                textView_Time.text = getString(R.string.time_text,(millisUntilFinished/1000).toString())
            }

            override fun onFinish() {
                textView_Time.text = getString(R.string.time_text,"0")

                //PREVENT GETTING SCORES WHEN GAME IS OVER
                gameOver = true

                //STOP AND HIDE SONICS WHEN GAME OVER
                handler.removeCallbacks(runnable)
                imageViewList.forEach {
                    it.visibility = View.INVISIBLE
                }

                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle(getString(R.string.gameover_text))
                alertDialog.setMessage(getString(R.string.playagain_text))
                alertDialog.setPositiveButton(getString(R.string.yes_text)) { _, _ ->
                    val intent = intent
                    finish()
                    startActivity(intent)
                }

                alertDialog.setNegativeButton(getString(R.string.no_text)) { _, _ ->
                    Toast.makeText(this@MainActivity,getString(R.string.gameover_text),Toast.LENGTH_SHORT).show()
                }
                alertDialog.show()
            }

        }.start()
    }

    fun onClickSonic(view : View)
    {
        if (!gameOver)
        {
            score+=1
            textView_Score.text = getString(R.string.score_text,score.toString())
        }
    }

    private fun randomSonic()
    {
        runnable = object : Runnable
        {
            override fun run() {
                imageViewList.forEach {
                    it.visibility = View.INVISIBLE
                }
                imageViewList[returnRandomIndex()].visibility = View.VISIBLE
                handler.postDelayed(this,sonicSpeed)
            }
        }

        handler.post(runnable)

    }

    private fun returnRandomIndex() : Int
    {
        var generatedIndex = Random.nextInt(9)
        for (i in spawnedIndices)
        {
            if (spawnedIndices.contains(generatedIndex))
            {
                while (spawnedIndices.contains(generatedIndex))
                {
                    generatedIndex = Random.nextInt(9)
                }
            }
        }
        if (spawnedIndices.size == 8)
        {
            spawnedIndices.clear()
        }
        spawnedIndices.add(generatedIndex)
        println(generatedIndex)
        return generatedIndex
    }
}