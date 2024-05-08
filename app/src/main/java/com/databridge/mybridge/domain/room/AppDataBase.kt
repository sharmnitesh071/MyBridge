package com.databridge.mybridge.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.databridge.mybridge.domain.model.Data

@Database(
    entities = [
        Data::class // todo remove
    ],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getUserDao(): UserDao // todo remove
}