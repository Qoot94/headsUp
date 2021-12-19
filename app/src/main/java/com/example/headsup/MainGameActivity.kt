package com.example.headsup

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MainGameActivity : AppCompatActivity() {
    lateinit var apiInterface: ApiInterface
    lateinit var timerText: TextView
    lateinit var timer: CountDownTimer
    var celebList = ArrayList<celebItem>()
    var savedTimer : String = "00:00"

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_game)
        apiInterface = APIClient().getClient()?.create(ApiInterface::class.java)!!

        //set up UI
        val celebButton = findViewById<ImageButton>(R.id.ibCelebList)
        val startFrame = findViewById<LinearLayout>(R.id.llStart)
        val playFrame = findViewById<LinearLayout>(R.id.llGame)
        val startButton = findViewById<Button>(R.id.btStart)
        timerText = findViewById(R.id.tvTimer)

        gettingData()

         timer = object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timerText.text =  "00:${ millisUntilFinished / 1000}"

            }

            override fun onFinish() {
                timerText.text  = "00:00"
                alert2()
            }
        }.start()

        //check orientation
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // start game and timer+ save timer on state changed
            playFrame.visibility = View.VISIBLE
            celebButton.visibility = View.GONE
            startFrame.visibility = View.GONE
            timer.start()

        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // stop game, refresh item displayed
            playFrame.visibility = View.GONE
            celebButton.visibility = View.VISIBLE
            startFrame.visibility = View.VISIBLE
            timer.cancel()
        }

        //button interactions
        celebButton.setOnClickListener {
            startActivity(Intent(this, CelebListActivity::class.java))
        }

    }
    //save state
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("myTime", timerText.text.toString())
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        timerText.text= savedInstanceState.getString("myTime", "00:00")
    }

    //API functions:CRUD
    fun gettingData() {
        //get request
        GlobalScope.launch(Dispatchers.IO) {
            val response = apiInterface.getData().awaitResponse()
            if (response.isSuccessful) {
                for (i in 0 until response.body()!!.size) {
                    val pkAPI = response.body()!![i].pk
                    val nameAPI = response.body()!![i].name
                    val taboo1API = response.body()!![i].taboo1
                    val taboo2API = response.body()!![i].taboo2
                    val taboo3API = response.body()!![i].taboo3

                    val celebObject = celebItem(nameAPI, pkAPI, taboo1API, taboo2API, taboo3API)
                    celebList.add(celebObject)
                    startGame()
                }
                withContext(Main) {
                    val rand = Random.nextInt(0, celebList.size)

                    val title = findViewById<TextView>(R.id.tvTitleg)
                    val hint1 = findViewById<TextView>(R.id.tvHint1g)
                    val hint2 = findViewById<TextView>(R.id.tvHint2g)
                    val hint3 = findViewById<TextView>(R.id.tvHint3g)

                    title.text = celebList[rand].name
                    hint1.text = celebList[rand].taboo1
                    hint2.text = celebList[rand].taboo2
                    hint3.text = celebList[rand].taboo3

                    println(celebList[rand])

                }

            }
        }
    }

    fun startGame() {
        val rand = Random.nextInt(0, celebList.size)
    }

    fun alert2() {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Game Over!!")
        //set message for alert dialog
        builder.setMessage("Time is Up")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()
            this.recreate()
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            Toast.makeText(applicationContext, "clicked No", Toast.LENGTH_LONG).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

}