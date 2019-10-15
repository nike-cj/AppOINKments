package it.appoinkments.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import javax.sql.DataSource

@Dao
interface LoadDAO {
    @get:Query("SELECT * FROM load")
    val all: List<Load>

//    @Query("SELECT * FROM Load")
//    fun loadsAll(): DataSource.Factory<Int, Load>

    @Query("SELECT * FROM load where lid = :load_id ")
    fun findById(load_id: Long): Load

    @Query("SELECT * FROM load where farmer LIKE :name ")
    fun findByFarmer(name: String): List<Load>

//    @Query("UPDATE load SET name = :name where lid = :id")
//    fun updateName(name: String, id: Int)

    @Query("SELECT COUNT(*) from load")
    fun countLoads(): Int

    @Insert
    fun insert(load: Load) : Long

    @Insert
    fun insertAll(vararg loads: Load)

    @Delete
    fun delete(load: Load)

    @Query("DELETE FROM load")
    fun deleteAll()
}