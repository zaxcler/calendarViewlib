package com.zaxcler.calendardemo

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.zaxcler.calendarviewlib.StateData

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val calendar = Calendar.getInstance()
        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()

            calendar.add(Calendar.MONTH,-1)
            val list = arrayListOf<StateData>()
            list.add(StateData("2018-12-1",1))
            list.add(StateData("2018-12-2",2))
            list.add(StateData("2018-12-3",1))
            list.add(StateData("2018-12-4",1))
            calendarView.setData(calendar,list)
        }
        calendarView.addOnDateChooseListener{
            Toast.makeText(this,it.date,Toast.LENGTH_LONG).show()
        }
        val list = arrayListOf<StateData>()
        list.add(StateData("2018-12-01",1))
        list.add(StateData("2018-12-02",2))
        list.add(StateData("2018-12-03",1))
        list.add(StateData("2018-12-04",1))
        calendarView.setData(calendar,list)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
