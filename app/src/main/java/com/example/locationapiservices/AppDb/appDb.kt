package com.example.locationapiservices.AppDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [(Usersettings::class)],version = 2)
abstract class appDb : RoomDatabase() {

//    abstract fun pass_dao() : password_Dao
    abstract fun UserSetting_dao() : Usersetting_dao


    companion object {
        private var INSTANCE: appDb?= null

        val migration_2_3: Migration = object: Migration(2,3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Usersettings ADD COLUMN SettingsName TEXT DEFAULT ''")
            }
        }

        fun getAppDatabase(context: Context): appDb? {


            if(INSTANCE == null ) {

                INSTANCE = Room.databaseBuilder<appDb>(
                    context.applicationContext, appDb::class.java, "Usersettings"
                )
                    .addMigrations(migration_2_3)
                    .allowMainThreadQueries()
                    .build()

            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
