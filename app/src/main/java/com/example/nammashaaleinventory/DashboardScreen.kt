package com.example.nammashaaleinventory

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween

@Composable
fun DashboardScreen(
    viewModel: AssetViewModel,
    isAdmin: Boolean,
    onRoleChange: (Boolean) -> Unit,
    onAdd: () -> Unit,
    onList: () -> Unit,
    onRepair: () -> Unit
) {
    val total by viewModel.totalAssetsCount.collectAsState()
    val repair by viewModel.needsRepairCount.collectAsState()
    val broken by viewModel.brokenCount.collectAsState()
    val goodCount = total - repair - broken
    
    val context = LocalContext.current
    var visible by remember { mutableStateOf(value = false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + slideInVertically { 40 }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Dashboard",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Namma Shaale Inventory",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                }
                
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isAdmin) "Admin" else "Staff",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Switch(
                            checked = isAdmin,
                            onCheckedChange = onRoleChange,
                            modifier = Modifier.scale(0.7f)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Statistics Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                item { 
                    DashboardCard(
                        title = "Total Assets", 
                        count = total, 
                        color = MaterialTheme.colorScheme.primary,
                        icon = Icons.AutoMirrored.Filled.List
                    ) 
                }
                item { 
                    DashboardCard(
                        title = "Healthy", 
                        count = goodCount, 
                        color = Color(0xFF43A047),
                        icon = Icons.Default.CheckCircle
                    ) 
                }
                item { 
                    DashboardCard(
                        title = "Repair Needed", 
                        count = repair, 
                        color = Color(0xFFFBC02D),
                        icon = Icons.Default.Warning
                    ) 
                }
                item { 
                    DashboardCard(
                        title = "Broken Items", 
                        count = broken, 
                        color = MaterialTheme.colorScheme.error,
                        icon = Icons.Default.Close
                    ) 
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(), 
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onAdd, 
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) { 
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text("Add New Asset", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) 
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onList, 
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                    ) { 
                        Text("View Assets", fontWeight = FontWeight.SemiBold) 
                    }
                    OutlinedButton(
                        onClick = onRepair, 
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                    ) { 
                        Text("Repair Logs", fontWeight = FontWeight.SemiBold) 
                    }
                }
                
                if (isAdmin) {
                    Button(
                        onClick = { viewModel.generateAndShareReport(context) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) { 
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(Modifier.width(12.dp))
                        Text("Export Inventory Report", fontWeight = FontWeight.Bold) 
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, count: Int, color: Color, icon: ImageVector) {
    val animatedCount by animateIntAsState(
        targetValue = count,
        animationSpec = tween(durationMillis = 1000)
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp), 
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon, 
                        contentDescription = null, 
                        tint = color,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = title, 
                color = color, 
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = animatedCount.toString(), 
                style = MaterialTheme.typography.displaySmall, 
                color = color, 
                fontWeight = FontWeight.Black
            )
        }
    }
}

private fun Modifier.scale(scale: Float): Modifier = graphicsLayer(scaleX = scale, scaleY = scale)
