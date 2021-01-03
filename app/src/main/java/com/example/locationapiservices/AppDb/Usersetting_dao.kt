package com.example.locationapiservices.AppDb

import androidx.room.*


@Dao
interface Usersetting_dao {
    @Insert
    fun saveUsersettings(usersettings: Usersettings)

    @Query("select * from Usersettings")
    fun selectall() : List<Usersettings>

    @Query("select * from Usersettings where id=:id")
    fun getSettingById(id:Int) : Usersettings

    @Delete
    fun deteteusersettings(user: Usersettings?)

    @Update
    fun updateUserSettings(user: Usersettings?)
}