package com.example.locationapiservices.AppDb

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
class Usersettings() : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0

    @ColumnInfo(name = "SettingsName")
    var settingsName : String? = ""



    @ColumnInfo(name = "usermood")
    var usermood : String? = ""

    @ColumnInfo(name = "latitude")
    var latitude : Double? = null

    @ColumnInfo(name = "longitude")
    var longitude : Double? = null

    @ColumnInfo(name = "Vibrate")
    var Vibrate : Int? = null

    @ColumnInfo(name = "Mediavolume")
    var MediaVolume : Int? = null

    @ColumnInfo(name = "Callvolume")
    var callVolume : Int? = null

    @ColumnInfo(name = "Alarmvolume")
    var AlarmVolume : Int? = null

    @ColumnInfo(name = "Distance")
    var Distance : String? = null




}