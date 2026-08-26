package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AdminSecurityConfig
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
import com.example.data.model.StudentRecord
import com.example.data.model.TeacherAccount

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
        GroupChatMessage::class,
        TeacherAccount::class,
        StudentRecord::class,
        AdminSecurityConfig::class
    ],
    version = 7,
    exportSchema = false
)
abstract class SchoolDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao

    companion object {
        @Volatile
        private var INSTANCE: SchoolDatabase? = null

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS `admin_security`")
                db.execSQL(
                    """
                    CREATE TABLE `admin_security` (
                        `id` INTEGER NOT NULL,
                        `adminPasskey` TEXT NOT NULL,
                        `adminName` TEXT NOT NULL,
                        `adminEmail` TEXT NOT NULL,
                        `adminPhone` TEXT NOT NULL,
                        `activeTerm` TEXT NOT NULL,
                        `activeSession` TEXT NOT NULL,
                        `bankName` TEXT NOT NULL,
                        `bankAccountNumber` TEXT NOT NULL,
                        `bankAccountName` TEXT NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS `admin_security`")
                db.execSQL(
                    """
                    CREATE TABLE `admin_security` (
                        `id` INTEGER NOT NULL,
                        `adminPasskey` TEXT NOT NULL,
                        `adminName` TEXT NOT NULL,
                        `adminEmail` TEXT NOT NULL,
                        `adminPhone` TEXT NOT NULL,
                        `activeTerm` TEXT NOT NULL,
                        `activeSession` TEXT NOT NULL,
                        `bankName` TEXT NOT NULL,
                        `bankAccountNumber` TEXT NOT NULL,
                        `bankAccountName` TEXT NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_4_6 = object : Migration(4, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS `admin_security`")
                db.execSQL(
                    """
                    CREATE TABLE `admin_security` (
                        `id` INTEGER NOT NULL,
                        `adminPasskey` TEXT NOT NULL,
                        `adminName` TEXT NOT NULL,
                        `adminEmail` TEXT NOT NULL,
                        `adminPhone` TEXT NOT NULL,
                        `activeTerm` TEXT NOT NULL,
                        `activeSession` TEXT NOT NULL,
                        `bankName` TEXT NOT NULL,
                        `bankAccountNumber` TEXT NOT NULL,
                        `bankAccountName` TEXT NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent()
                )
            }
        }

        fun getDatabase(context: Context): SchoolDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SchoolDatabase::class.java,
                    "graziel_royal_school_db"
                )
                    .addMigrations(MIGRATION_4_5, MIGRATION_5_6, MIGRATION_4_6)
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
