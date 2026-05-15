package com.example.nammashaaleinventory.data


class AssetRepository(private val assetDao: AssetDao) {

    suspend fun insert(asset: Asset) {
        assetDao.insertAsset(asset)
    }
}
