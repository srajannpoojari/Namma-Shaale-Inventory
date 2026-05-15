package com.example.nammashaaleinventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: Asset)

    @Query("SELECT * FROM assets ORDER BY id DESC")
    fun getAllAssets(): Flow<List<Asset>>

    @Query("SELECT * FROM assets WHERE condition = 'Needs Repair' OR condition = 'Broken'")
    fun getRepairNeededAssets(): Flow<List<Asset>>

    @Query("SELECT COUNT(*) FROM assets")
    fun getTotalAssetsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM assets WHERE condition = 'Needs Repair'")
    fun getNeedsRepairCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM assets WHERE condition = 'Broken'")
    fun getBrokenCount(): Flow<Int>

    @Query("UPDATE assets SET condition = :newCondition WHERE id = :assetId")
    suspend fun updateAssetCondition(assetId: Int, newCondition: String)
}
