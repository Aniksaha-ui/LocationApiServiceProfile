package com.example.locationapiservices

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.locationapiservices.AppDb.Usersettings
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity(),RecyclerViewAdapter.RowClickListener {

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var viewModel: SecondActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)



        var Addnew=findViewById(R.id.Addnew) as Button


        Addnew.setOnClickListener{
            val intent = Intent(this@SettingsActivity, SecondActivity::class.java)
            startActivity(intent)
        }




        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SettingsActivity)
            recyclerViewAdapter = RecyclerViewAdapter(this@SettingsActivity)
            adapter = recyclerViewAdapter
            val divider = DividerItemDecoration(
                applicationContext, StaggeredGridLayoutManager.VERTICAL
            )
            addItemDecoration(divider)
        }

        viewModel = ViewModelProviders.of(this).get(SecondActivityViewModel::class.java)
        viewModel.getAllUsersObserversSettings().observe(this, Observer {
            recyclerViewAdapter.setListData(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        })

    }

    override fun onDeleteUserClickListener(user: Usersettings) {
        viewModel.deleteUserSettingsInfo(user)
    }

    override fun onItemClickListener(user: Usersettings) {

        val intt = Intent(getApplicationContext(), UpdateActivity::class.java)
        intt.putExtra("data",user.id)
        startActivity(intt)



    }

}