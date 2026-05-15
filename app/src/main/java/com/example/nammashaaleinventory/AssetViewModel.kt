package com.example.nammashaaleinventory

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammashaaleinventory.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AssetViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).assetDao()
    
    val allAssets = dao.getAllAssets().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val repairNeededAssets = dao.getRepairNeededAssets().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val totalAssetsCount = dao.getTotalAssetsCount().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val needsRepairCount = dao.getNeedsRepairCount().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val brokenCount = dao.getBrokenCount().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun registerAsset(name: String, cat: String, serial: String, cond: String, uri: String?, issue: String?) {
        viewModelScope.launch {
            val date = if (cond != "Good") SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()) else null
            dao.insertAsset(
                Asset(
                    name = name, 
                    category = cat, 
                    serialNumber = serial, 
                    condition = cond, 
                    imageUri = uri,
                    issueDescription = issue,
                    issueDate = date
                )
            )
        }
    }

    fun generateAndShareReport(context: Context) {
        val assets = allAssets.value
        val goodCount = totalAssetsCount.value - needsRepairCount.value - brokenCount.value
        
        val reportHeader = """
            School Inventory Report
            Date: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())}
            
            Total Assets: ${totalAssetsCount.value}
            Good: $goodCount
            Needs Repair: ${needsRepairCount.value}
            Broken: ${brokenCount.value}
            
            Asset Details:
        """.trimIndent()

        val assetDetails = assets.joinToString("\n") { asset ->
            "- ${asset.name} | ${asset.category} | ${asset.condition} | ${asset.serialNumber}"
        }

        val fullReport = "$reportHeader\n$assetDetails\n\nGenerated via Namma Shaale App"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, fullReport)
        }
        context.startActivity(Intent.createChooser(intent, "Share Report"))
    }

    fun updateAssetCondition(assetId: Int, newCondition: String) {
        viewModelScope.launch {
            dao.updateAssetCondition(assetId, newCondition)
        }
    }
}
