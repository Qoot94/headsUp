package com.example.headsup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity2 : AppCompatActivity() {
    lateinit var apiInterface:ApiInterface
    var pk = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        //set up UI
        val pkText = findViewById<EditText>(R.id.etPK)
        val nameText = findViewById<EditText>(R.id.etName)
        val taboo1Text = findViewById<EditText>(R.id.etTaboo1)
        val taboo2Text = findViewById<EditText>(R.id.etTaboo2)
        val taboo3Text = findViewById<EditText>(R.id.etTaboo3)

        val deleteButton = findViewById<ImageButton>(R.id.ibDel)
        val updateButton = findViewById<ImageButton>(R.id.ibUpdate)
        val addButton = findViewById<ImageButton>(R.id.ibAdding)

        apiInterface = APIClient().getClient()?.create(ApiInterface::class.java)!!

        addButton.setOnClickListener {
            val call: Call<celebItem> = apiInterface.addData(
                celebItem(
                    nameText.text.toString(),
                    0,
                    taboo1Text.text.toString(),
                    taboo2Text.text.toString(),
                    taboo3Text.text.toString()
                )
            )
            call.enqueue(object : Callback<celebItem> {
                override fun onResponse(call: Call<celebItem>, response: Response<celebItem>) {
                    val response = response.body()!!
                    pk = response.pk

                    Log.d("POST-success", "get request success $response")
                }

                override fun onFailure(call: Call<celebItem>, t: Throwable) {
                    Log.d("POST-error", "$t")
                }
            })
        }

        updateButton.setOnClickListener {
            apiInterface.updateData(
                pk, celebItem(
                    nameText.text.toString(),
                    pk,
                    taboo1Text.text.toString(),
                    taboo2Text.text.toString(),
                    taboo3Text.text.toString()
                )
            ).enqueue(object : Callback<celebItem> {
                override fun onResponse(call: Call<celebItem>, response: Response<celebItem>) {
                    Toast.makeText(
                        applicationContext,
                        "celeb $pk updated to api ",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<celebItem>, t: Throwable) {
                    Log.d("error-update", "$t")
                }
            })
        }

        deleteButton.setOnClickListener {
            val celebID = pkText.text.toString().toInt()
            //delete request
            apiInterface.deleteData(celebID).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Toast.makeText(
                        applicationContext, "User $celebID deleted",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("error-delete", "$t")
                }
            })
        }
    }
}