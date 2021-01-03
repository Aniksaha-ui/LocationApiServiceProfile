package com.example.locationapiservices

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.locationapiservices.AppDb.Usersettings
import com.example.locationapiservices.AppDb.appDb

class SecondActivityViewModel(app: Application): AndroidViewModel(app) {

    lateinit var allUsers : MutableLiveData<List<Usersettings>>
    lateinit var oneUser : MutableLiveData<Usersettings>

    init{
        allUsers = MutableLiveData()
        oneUser= MutableLiveData()
        getAllUsersSettings()

    }

    fun getAllUsersObserversSettings(): MutableLiveData<List<Usersettings>> {
        return allUsers
    }




    fun getAllUsersSettings() {
        val userDao = appDb.getAppDatabase((getApplication()))?.UserSetting_dao()
        val list = userDao?.selectall()
        allUsers.postValue(list)
    }

    fun getOneUserSettings(id:Int):MutableLiveData<Usersettings> {
        val userDao = appDb.getAppDatabase((getApplication()))?.UserSetting_dao()
        val settiengInformation = userDao?.getSettingById(id)
        oneUser.postValue(settiengInformation)
        return  oneUser
    }

    fun insertUserSettingsInfo(entity: Usersettings){
        val userDao = appDb.getAppDatabase(getApplication())?.UserSetting_dao()
        userDao?.saveUsersettings(entity)
        getAllUsersSettings()
    }

    fun updateUserSettingsInfo(entity: Usersettings){
        val userDao = appDb.getAppDatabase(getApplication())?.UserSetting_dao()
        userDao?.updateUserSettings(entity)
        getAllUsersSettings()
    }

    fun deleteUserSettingsInfo(entity: Usersettings){
        val userDao = appDb.getAppDatabase(getApplication())?.UserSetting_dao()
        userDao?.deteteusersettings(entity)
        getAllUsersSettings()
    }
}
