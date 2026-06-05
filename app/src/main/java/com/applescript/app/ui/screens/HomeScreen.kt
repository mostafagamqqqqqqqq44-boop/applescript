package com.applescript.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.applescript.app.util.TOTPUtil
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    accountId: String,
    onLogout: () -> Unit
) {
    var currentCode by remember { mutableStateOf(TOTPUtil.generateTOTP(accountId)) }
    var timeRemaining by remember { mutableStateOf(TOTPUtil.getTimeRemaining()) }
    var isRefreshing by remember { mutableStateOf(false) }
    
    // تحديث الكود والوقت كل ثانية
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            val newTimeRemaining = TOTPUtil.getTimeRemaining()
            
            if (newTimeRemaining == 60) {
                // الكود الجديد
                currentCode = TOTPUtil.generateTOTP(accountId)
            }
            
            timeRemaining = newTimeRemaining
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1C1C1E),
                        Color(0xFF2C2C2E)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // شريط التطبيق العلوي
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "مرحباً بك",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Text(
                        text = accountId,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                IconButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2C2C2E))
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "تسجيل الخروج",
                        tint = Color(0xFFFF2D2D)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // بطاقة الكود السري
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2C2C2E)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "الكود السري الحالي",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    
                    // عرض الكود
                    Text(
                        text = formatCode(currentCode),
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 8.sp
                    )
                    
                    // شريط التقدم
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = { timeRemaining / 60f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (timeRemaining <= 10) Color(0xFFFF2D2D) else Color(0xFF4CAF50),
                            trackColor = Color(0xFF1C1C1E)
                        )
                        
                        Text(
                            text = "ينتهي خلال $timeRemaining ثانية",
                            color = if (timeRemaining <= 10) Color(0xFFFF2D2D) else Color(0xFF4CAF50),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // زر التحديث
                    Button(
                        onClick = {
                            isRefreshing = true
                            currentCode = TOTPUtil.generateTOTP(accountId)
                            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                delay(500)
                                isRefreshing = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF2D2D),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "تحديث",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تحديث الكود",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // معلومات إضافية
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2C2C2E)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "معلومات الحساب",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Divider(color = Color(0xFF1C1C1E))
                    
                    InfoRow("معرف الحساب", accountId)
                    InfoRow("نوع التشفير", "HMAC-SHA256")
                    InfoRow("فترة التجديد", "60 ثانية")
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

fun formatCode(code: String): String {
    return code.chunked(3).joinToString(" ")
}
