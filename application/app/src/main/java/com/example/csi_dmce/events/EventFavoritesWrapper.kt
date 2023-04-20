package com.example.csi_dmce.events

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity
data class Favorite(
    @PrimaryKey val eventId: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean
)

@Dao
interface FavoriteDao {
    @Query("SELECT is_favorite FROM favorite WHERE eventId = :eventId")
    fun getFavorite(eventId: String): Boolean

    @Query("SELECT eventId FROM favorite WHERE is_favorite = 1")
    fun getAllFavorites(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setFavorite(favEvent: Favorite)

    @Update
    fun unsetFavorite(favEvent: Favorite)
}

@Database(entities = [Favorite::class], version = 1)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}


class FavDbWrappers {
   companion object {
       fun returnFavDbInstance(ctx: Context): FavoriteDatabase {
           return Room.databaseBuilder(
               ctx,
               FavoriteDatabase::class.java, "event-favorites"
           )
               .allowMainThreadQueries()
               .build()
       }
   }
}
