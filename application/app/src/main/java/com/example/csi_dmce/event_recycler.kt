package com.example.csi_dmce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.identity.AccessControlProfileId
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class event_recycler : AppCompatActivity() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<events>
    lateinit var imageId: Array<Int>
    lateinit var heading : Array<String>
    lateinit var dateId : Array<String>
    lateinit var timeId : Array<String>
    lateinit var venue : Array<String>
    lateinit var description : Array<String>
    lateinit var speakers : Array<String>
    lateinit var prerequisites : Array<String>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_recycler)


        imageId = arrayOf(

            R.drawable.github_logo,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
            R.drawable.f,
            R.drawable.g

        )

        heading = arrayOf(
            "Git And Github",
            "Hactoberfest",
            "Web Development",
            "App Development",
            "Blockchain Technology",
            "Flutter and Dart",
            "Workshop"
        )


        dateId = arrayOf(
            "DATE:-10/10/22",
            "DATE:-16/11/22",
            "DATE:-15/1/22",
            "DATE:-1/10/22",
            "DATE:-10/10/22",
            "DATE:-19/2/22",
            "DATE:-12/1/22"

        )

        timeId = arrayOf(
            "TIME:-12:00PM",
            "TIME:-3:00PM",
            "TIME:-11:00PM",
            "TIME:-12:00PM",
            "TIME:-10:00PM",
            "TIME:-12:00PM",
            "TIME:-1:00PM"


        )



        venue = arrayOf(
            "VENUE:- SEMINAR HALL(805)",
            "MODE:ONLINE",
            "VENUE:- SEMINAR HALL(805)",
            "VENUE:- SEMINAR HALL(805)",
            "MODE:ONLINE",
            "MODE:ONLINE",
            "VENUE:- SEMINAR HALL(805)"

        )

        description = arrayOf(

            getString(R.string.desc_a),
            getString(R.string.desc_b),
            getString(R.string.desc_c),
            getString(R.string.desc_d),
            getString(R.string.desc_e),
            getString(R.string.desc_f),
            getString(R.string.desc_g)



        )



        speakers = arrayOf(


            "Sneha Singh",
            "Patil Soham",
            "Daksh Roy ",
            "Rajesh Verma ",
            "Sneha Verma and Vaibhav Singh",
            "Vaibhav Singh",
            "Sneha Verma "





        )


        prerequisites = arrayOf(

            "No prequisites required",
            "No prequisites required",
            "No prequisites required",
            "No prequisites required",
            "No prequisites required",
            "No prequisites required",
            "No prequisites required"



        )





        newRecyclerView=findViewById(R.id.recylerview)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<events>()
        getUserdata()

    }

    private fun getUserdata() {
        for (i in imageId.indices){

            val events = events(imageId[i],heading[i],dateId[i],timeId[i])
            newArrayList.add(events)

        }

        var adapter = EventAdapter(newArrayList)

        newRecyclerView.adapter = adapter
        adapter.setOnItemClickListner(object : EventAdapter.onItemClickListner{
            override fun onItemClick(position: Int) {


                //Toast.makeText(this@event_recycler,"next",Toast.LENGTH_SHORT).show()

                val intent = Intent(this@event_recycler,event_page::class.java)
                intent.putExtra("heading",newArrayList[position].eventname)
                intent.putExtra("imageId",newArrayList[position].titleimage)
                intent.putExtra("venue",venue[position])
                intent.putExtra("dateId",newArrayList[position].date)
                intent.putExtra("timeId",newArrayList[position].time)
                intent.putExtra("description",description[position])
                intent.putExtra("speakers",speakers[position])
                intent.putExtra("prerequisites",prerequisites[position])
                startActivity(intent)



            }


        })
    }
}