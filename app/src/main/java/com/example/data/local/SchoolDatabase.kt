package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AdmissionApplication
import com.example.data.model.Announcement
import com.example.data.model.Assignment
import com.example.data.model.AttendanceRecord
import com.example.data.model.CbtQuestion
import com.example.data.model.CbtSubmission
import com.example.data.model.CbtTest
import com.example.data.model.FeeItem
import com.example.data.model.GroupChatMessage
import com.example.data.model.PaymentTransaction
import com.example.data.model.StaffClockRecord

@Database(
    entities = [
        Announcement::class,
        Assignment::class,
        FeeItem::class,
        PaymentTransaction::class,
        AdmissionApplication::class,
        AttendanceRecord::class,
        CbtTest::class,
        CbtQuestion::class,
        CbtSubmission::class,
        StaffClockRecord::class,
        GroupChatMessage::class
    ],
    version = 2,
    exportSchema = false
)
abstract class SchoolDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao

    companion object {
        @Volatile
        private var INSTANCE: SchoolDatabase? = null

        fun getDatabase(context: Context): SchoolDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SchoolDatabase::class.java,
                    "graziel_royal_school_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
