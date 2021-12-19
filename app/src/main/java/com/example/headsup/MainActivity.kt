package com.example.headsup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var myRV: RecyclerView
    lateinit var rvAdapter: RVAdapter
    lateinit var apiInterface: ApiInterface
    var celebList = ArrayList<celebItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set up recyclerView
        myRV = findViewById(R.id.rvMain)
        rvAdapter = RVAdapter(celebList)
        myRV.adapter = rvAdapter
        myRV.layoutManager = LinearLayoutManager(this)
        val moreButton = findViewById<ImageButton>(R.id.ibAdd)

        apiInterface = APIClient().getClient()?.create(ApiInterface::class.java)!!
        gettingData()

        moreButton.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
    }

    //API functions:CRUD
    private fun gettingData() {
        //get request
        apiInterface.getData().enqueue(object : Callback<celeb> {
            override fun onResponse(call: Call<celeb>, response: Response<celeb>) {
                //retrieve all data needed
                val response = response.body()!!
                for (i in 0 until response!!.size) {
                    val pkAPI = response[i].pk
                    val nameAPI = response[i].name
                    val taboo1API = response[i].taboo1
                    val taboo2API = response[i].taboo2
                    val taboo3API = response[i].taboo3

//                    userID = response[i].pk
                    val celebObject = celebItem(nameAPI, pkAPI, taboo1API, taboo2API, taboo3API)
                    celebList.add(celebObject)
                    rvAdapter.notifyDataSetChanged()
                }
                Log.d("GET-success", "get request success $response")
            }

            override fun onFailure(call: Call<celeb>, t: Throwable) {
                Log.d("GET-error", "$t")
            }

        })
    }
}