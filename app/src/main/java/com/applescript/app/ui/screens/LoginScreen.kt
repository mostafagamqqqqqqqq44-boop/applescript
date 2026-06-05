package com.applescript.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.applescript.app.data.AuthManager
import com.applescript.app.util.TOTPUtil
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    authManager: AuthManager? = null
) {
    var accountId by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var codeVisible by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf(TOTPUtil.getTimeRemaining()) }
    
    // تحديث الوقت المتبقي كل ثانية
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            timeRemaining = TOTPUtil.getTimeRemaining()
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
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // شعار التفاحة
            AppleLogo()
            
            // عنوان التطبيق
            Text(
                text = "سكربت التفاحه",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            
            // بطاقة تسجيل الدخول
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
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "تسجيل الدخول",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // حقل معرف الحساب
                    OutlinedTextField(
                        value = accountId,
                        onValueChange = { 
                            accountId = it
                            errorMessage = ""
                        },
                        label = { Text("معرف الحساب") },
                        placeholder = { Text("أدخل معرف الحساب") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF2D2D),
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color(0xFFFF2D2D),
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color(0xFFFF2D2D)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    
                    // حقل الكود السري
                    OutlinedTextField(
                        value = code,
                        onValueChange = { 
                            if (it.length <= 6) {
                                code = it
                                errorMessage = ""
                            }
                        },
                        label = { Text("الكود السري") },
                        placeholder = { Text("أدخل الكود المكون من 6 أرقام") },
                        singleLine = true,
                        visualTransformation = if (codeVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { codeVisible = !codeVisible }) {
                                Icon(
                                    imageVector = if (codeVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (codeVisible) "إخفاء" else "إظهار",
                                    tint = Color.Gray
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF2D2D),
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color(0xFFFF2D2D),
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color(0xFFFF2D2D)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    
                    // عرض الوقت المتبقي
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ينتهي خلال:",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "$timeRemaining ثانية",
                            color = if (timeRemaining <= 10) Color(0xFFFF2D2D) else Color(0xFF4CAF50),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    // رسالة الخطأ
                    AnimatedVisibility(
                        visible = errorMessage.isNotEmpty(),
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        Text(
                            text = errorMessage,
                            color = Color(0xFFFF2D2D),
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    
                    // زر تسجيل الدخول
                    Button(
                        onClick = {
                            if (accountId.isBlank() || code.isBlank()) {
                                errorMessage = "يرجى ملء جميع الحقول"
                                return@Button
                            }
                            
                            isLoading = true
                            
                            // محاكاة التحقق (في التطبيق الحقيقي، استخدم AuthManager)
                            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                delay(1000)
                                
                                // للتجربة: نقبل أي كود مكون من 6 أرقام
                                if (code.length == 6 && accountId.isNotEmpty()) {
                                    onLoginSuccess(accountId)
                                } else {
                                    errorMessage = "بيانات الدخول غير صحيحة"
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF2D2D),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "دخول",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppleLogo() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFFF2D2D)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🍎",
            fontSize = 60.sp
        )
    }
}
