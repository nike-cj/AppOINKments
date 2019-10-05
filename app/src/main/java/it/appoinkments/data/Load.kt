package it.appoinkments.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "load")
@TypeConverters(DateConverter::class)
data class Load (@PrimaryKey(autoGenerate = true) val lid: Int = 0,
                 var farmer: String,
                 var date_arrival: Date,
                 var nr_pigs: Int,
                 var origin: String,
                 var round: String,
                 var date_vaccination_first: Date,
                 var date_vaccination_second: Date,
                 var date_vaccination_third: Date,
                 var date_vermicide_first: Date,
                 var date_vermicide_second: Date)