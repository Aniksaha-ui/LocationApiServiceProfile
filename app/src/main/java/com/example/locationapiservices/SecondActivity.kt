package com.example.locationapiservices

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.example.locationapiservices.AppDb.Usersettings
import com.example.locationapiservices.AppDb.appDb
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.activity_second.longitude
import kotlinx.android.synthetic.main.recyclerview_row.*


class SecondActivity : AppCompatActivity(),RecyclerViewAdapter.RowClickListener{

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var viewModel: SecondActivityViewModel
    var distance:String?=null
    var currentSeekBarValue:Int=0
    var currentSeekBarValue1:Int=0
    var currentSeekBarValue2:Int=0
    private var latitudes: Double? = null
    private var longitudes: Double? = null
    private var get_place: TextView? = null
    private var longititudes: TextView? = null
    private var select_res : TextView? = null
    private var SettingsName:EditText?=null
    private var DistanceValue:EditText?=null
    lateinit var option : Spinner
    lateinit var  select: String
    private var savesetting : Button? = null
    private var getlat : String? = null
    private var getlong : String? = null
    private var getsp : String? = null
    private var getlatu: Double? = null
    private var getlongi: Double? = null
    private var upbutton:Button?=null
    private var downbutton:Button?=null
    private var seekBar:SeekBar?=null
    private var seekBar2:SeekBar?=null
    private var seekBar3:SeekBar?=null
    private var audioManager:AudioManager?=null
    private var vibrateMood:Int?=null
    private var vibrateButton:CheckBox?=null
    var PLACE_PICKER_REQUEST = 1




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SecondActivityViewModel::class.java)
        var db = Room.databaseBuilder(applicationContext, appDb::class.java, "Usersettings").build()
        setContentView(R.layout.activity_second)
        get_place = findViewById<View>(R.id.text_view) as TextView
        SettingsName = findViewById<View>(R.id.settingsName) as EditText
        longititudes = findViewById<View>(R.id.longitude) as TextView
//        option = findViewById<View>(R.id.sp_option) as Spinner
        select_res = findViewById<View>(R.id.sp_result) as TextView
        savesetting = findViewById<View>(R.id.save_settings) as Button
        seekBar=findViewById(R.id.mediavolume) as SeekBar
        seekBar2=findViewById(R.id.CallVolume) as SeekBar
        seekBar3=findViewById(R.id.AlarmVolume) as SeekBar
        audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        DistanceValue=findViewById(R.id.distance) as EditText
        vibrateButton=findViewById<View>(R.id.vibarate) as CheckBox


        vibrateButton!!.setOnClickListener{
           if(vibrateButton!!.isChecked()) {
                    vibrateMood=1
            }
            else{
                vibrateMood= 2
            }
        }




        savesetting!!.setOnClickListener {

            getlat = get_place!!.text.toString()
            getlong = longititudes!!.text.toString()
            getsp = select_res!!.text.toString()
            currentSeekBarValue= seekBar!!.progress.toInt()
            currentSeekBarValue1=seekBar2!!.progress.toInt()
            currentSeekBarValue2=seekBar3!!.progress.toInt()
            var mediaVolume=currentSeekBarValue
            var settingsName=SettingsName!!.text.toString()
            var callVolume=currentSeekBarValue1
            var AlarmVolume=currentSeekBarValue2
            var Vibarte=vibrateMood
            var usermood:String?=null
            var distance=DistanceValue!!.text.toString()



            if(savesetting!!.text.equals("Save")) {
                Thread {
                    var i = 0
                    var usersettings = Usersettings()
                    usersettings.latitude = latitudes
                    usersettings.longitude = longitudes
                    usersettings.usermood = null
                    usersettings.MediaVolume = currentSeekBarValue
                    usersettings.settingsName = SettingsName!!.text.toString()
                    usersettings.callVolume = currentSeekBarValue1
                    usersettings.AlarmVolume = currentSeekBarValue2
                    usersettings.Vibrate = vibrateMood
                    usersettings.Distance = distance
                    db.UserSetting_dao().saveUsersettings(usersettings)
                }.start()
            }



            val intent = Intent(this@SecondActivity, MainActivity::class.java)
            startActivity(intent)

        }


        val intent:Intent = getIntent ()
        val id =intent.getIntExtra("data",-1)
        var settingsData : Usersettings
        viewModel.getOneUserSettings(id).observe(this, Observer {



        })



//        d("settingName",settingsData.settingsName.toString())

//
//        if(savesetting!!.text.equals("update")){
//
//            Thread{
//                var i= 0
//                var usersettings = Usersettings()
//               usersettings.settingsName= settingsName.getTag(settingsName.id).toString().toInt().toString()
//             db.usersetting_dao().updateUserSettings(usersettings)
//            }.start()
//        }








        get_place!!.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()
            val intent: Intent
            try {
                intent = builder.build(this@SecondActivity)
                startActivityForResult(intent, PLACE_PICKER_REQUEST)
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            }
        }

//        val options = arrayOf("silent", "general")
//        option.adapter =ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
//
//        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//              select_res!!.text  = options.get(position).toString()
//            }
//        }




    }




    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(data, this@SecondActivity)
                 latitudes = place.latLng.latitude
                 longitudes = place.latLng.longitude
                val placename = place.name
                val address = latitudes.toString() + longitude.toString()
                get_place!!.text = latitudes.toString()
                longititudes!!.text = longitudes.toString()
            }
        }
    }

    override fun onDeleteUserClickListener(user: Usersettings) {
        viewModel.deleteUserSettingsInfo(user)
    }

    override fun onItemClickListener(user: Usersettings) {
   TODO("Not Implement")


    }


}


