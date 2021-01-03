package com.example.locationapiservices

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.example.locationapiservices.AppDb.Usersettings
import com.example.locationapiservices.AppDb.appDb
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.activity_update.*

class UpdateActivity : AppCompatActivity() {

    lateinit var viewModel: SecondActivityViewModel
    var distanceUpdate:String?=null
    private var SettingsNameUpdate:EditText?=null
    private var DistanceValueUpdate:EditText?=null
    private var get_placeUpdate: TextView? = null
    private var longititudesUpdate: TextView? = null
    private var seekBarUpdate: SeekBar?=null
    private var seekBar2Update: SeekBar?=null
    private var seekBar3Update: SeekBar?=null
    private var vibrateMoodUpdate:Int?=null
    private var vibrateButtonUpdate:CheckBox?=null
    private var UpdateButton : Button? = null
    var currentSeekBarValueUpdate:Int=0
    var currentSeekBarValue1Update:Int=0
    var currentSeekBarValue2Update:Int=0
    private var getlat : String? = null
    private var getlong : String? = null
    private var getsp : String? = null
    private var getlatu: Double? = null
    private var getlongi: Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        var db = Room.databaseBuilder(applicationContext, appDb::class.java, "Usersettings").build()
        viewModel = ViewModelProviders.of(this).get(SecondActivityViewModel::class.java)

        SettingsNameUpdate = findViewById<View>(R.id.settingsNameUpdate) as EditText
        DistanceValueUpdate=findViewById(R.id.distanceUpdate) as EditText
        get_placeUpdate = findViewById<View>(R.id.text_viewUpdate) as TextView
        longititudesUpdate = findViewById<View>(R.id.longitudeUpdate) as TextView
        seekBarUpdate=findViewById(R.id.mediavolumeUpdate) as SeekBar
        seekBar2Update=findViewById(R.id.CallVolumeUpdate) as SeekBar
        seekBar3Update=findViewById(R.id.AlarmVolumeUpdate) as SeekBar
        vibrateButtonUpdate =findViewById<View>(R.id.vibarateUpdate) as CheckBox

        vibrateButtonUpdate!!.setOnClickListener{
            if(vibrateButtonUpdate!!.isChecked()) {
                vibrateMoodUpdate=1
            }
            else{
                vibrateMoodUpdate= 2
            }
        }

        //data recieve from recyclerview
        val intent: Intent = getIntent ()
        val id =intent.getIntExtra("data",-1)
        var settingsData : Usersettings
        this.viewModel.getOneUserSettings(id).observe(this, Observer { database->
            setText(database)
        })


        update!!.setOnClickListener{


            getlat = get_placeUpdate!!.text.toString()
            getlong = longititudesUpdate!!.text.toString()
            currentSeekBarValueUpdate= seekBarUpdate!!.progress.toInt()
            currentSeekBarValue1Update=seekBar2Update!!.progress.toInt()
            currentSeekBarValue2Update=seekBar3Update!!.progress.toInt()
            var mediaVolume=currentSeekBarValueUpdate
            var settingsName=SettingsNameUpdate!!.text.toString()
            var callVolume=currentSeekBarValue1Update
            var AlarmVolume=currentSeekBarValue2Update
            var Vibarte=vibrateMoodUpdate
            var usermood:String?=null
            var distance=DistanceValueUpdate!!.text.toString()
            val user = Usersettings()

                Thread {
                    var i=0
                    var usersettings = Usersettings()
                    usersettings.id=settingsNameUpdate.getTag(settingsNameUpdate.id).toString().toInt()
                    usersettings.latitude = getlat.toString().toDouble()
                    usersettings.longitude =getlong.toString().toDouble()
                    usersettings.usermood = null
                    usersettings.MediaVolume = mediaVolume
                    usersettings.settingsName = settingsName
                    usersettings.callVolume =callVolume
                    usersettings.AlarmVolume =AlarmVolume
                    usersettings.Vibrate =Vibarte
                    usersettings.Distance =distance
                    db.UserSetting_dao().updateUserSettings(usersettings)
                }.start()

            val intent = Intent(this@UpdateActivity, MainActivity::class.java)
            startActivity(intent)
        }


    }

    private fun setText(database: Usersettings) {
        settingsNameUpdate!!.setText(database.settingsName!!.toString())
        DistanceValueUpdate!!.setText(database.Distance!!.toString())
        text_viewUpdate!!.text=database.latitude.toString()
        longitudeUpdate!!.text= database.longitude.toString()

        mediavolumeUpdate!!.setProgress(database.MediaVolume!!.toInt())
        CallVolumeUpdate!!.setProgress(database.callVolume!!.toInt())
        AlarmVolumeUpdate!!.setProgress(database.AlarmVolume!!.toInt())
        if(database.Vibrate==1){
            vibarateUpdate.isChecked =true
            vibrateMoodUpdate=1
        }

        else if(database.Vibrate==2){
            vibarateUpdate.isChecked=false
            vibrateMoodUpdate= 2
        }
        settingsNameUpdate.setTag(settingsNameUpdate.id, database.id)
    }

}