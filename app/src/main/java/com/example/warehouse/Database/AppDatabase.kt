package com.example.warehouse.Database

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.warehouse.BuildConfig
import com.example.warehouse.Class.*
import com.example.warehouse.DAO.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Database(entities = [User::class, Materials::class, Racks::class, MaterialsRacks::class,
    Issue::class], version = AppDatabase.VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun materialDao(): MaterialDAO
    abstract fun rackDao(): RackDAO
    abstract fun materialRackDao(): MRackDAO
    abstract fun issueDao(): IssueDAO

    companion object {
        const val VERSION = 4
        private const val DATABASE_NAME = "hotayi_warehouse.db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                instance ?: synchronized(this) {
                    instance ?: buildDatabase(context).also { instance = it }
                }

        private fun appDatabaseCallback(): Callback = object : Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.d(TAG, "Database has been created.")

                // Throws exception
                CoroutineScope(Dispatchers.IO).launch {
                    instance?.userDao()?.let { populateUserDB(it) }
                    instance?.materialDao()?.let { populateMaterialDB(it) }
                    instance?.rackDao()?.let { populateRackDB(it) }
                    instance?.materialRackDao()?.let { populateMRackDB(it) }
                    instance?.issueDao()?.let { populateIssueDB(it) }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                Log.d(TAG, "Database has been opened.")
            }
        }

        private fun buildDatabase(appContext: Context): AppDatabase {
            val filesDir = appContext.getExternalFilesDir(null)
            val dataDir = File(filesDir, "data")
            if (!dataDir.exists())
                dataDir.mkdir()

            val builder =
                    Room.databaseBuilder(
                            appContext,
                            AppDatabase::class.java,
                            File(dataDir, DATABASE_NAME).toString()
                    ).fallbackToDestructiveMigration()

            // Execute the callback only in DEBUG mode.
            if (BuildConfig.DEBUG) {
                builder.addCallback(appDatabaseCallback())
            }
            return builder.build()
        }

        private suspend fun populateUserDB(userDao: UserDAO) {
            withContext(Dispatchers.IO) {
                userDao.insertAll(User(1, "Tester", "123456"))
            }
        }

        private suspend fun populateMaterialDB(materialDao: MaterialDAO) {
            withContext(Dispatchers.IO) {
                materialDao.insertAll(
                        Materials(1, "TST1234", 10, 1, "2021-12-11", 1),
                        Materials(2, "TST5678", 10, 2, "2021-12-11", 1),
                        Materials(3, "TST9101", 10, 3, "2021-12-11", 1)
                )
            }
        }

        private suspend fun populateRackDB(rackDao: RackDAO) {
            withContext(Dispatchers.IO) {
                rackDao.insertAll(
                        Racks(1, "TST"),
                        Racks(2, "DLP")
                )
            }
        }

        private suspend fun populateMRackDB(materialRackDao: MRackDAO) {
            withContext(Dispatchers.IO) {
                materialRackDao.insertAll(
                        MaterialsRacks(
                                1,
                                1,
                                "RANDOMGENERATED123456GAGA",
                                "TST1234",
                                "",
                                ""
                        ),
                        MaterialsRacks(
                                2,
                                1,
                                "RAND4545364564GAGAGAEAGAA",
                                "TST5678",
                                "2021-12-11",
                                ""
                        ),
                        MaterialsRacks(
                                3,
                                1,
                                "ASDADASDASQW464651ASDA656",
                                "TST9101",
                                "2021-12-11",
                                ""
                        )
                )
            }
        }

        private suspend fun populateIssueDB(issueDao: IssueDAO) {
            withContext(Dispatchers.IO) {
                issueDao.insertAll(Issue(1, "Test report", "TST9101"))
            }
        }
    }
}