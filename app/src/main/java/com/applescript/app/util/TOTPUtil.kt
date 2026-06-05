package com.applescript.app.util

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object TOTPUtil {
    private const val TIME_STEP_SECONDS = 60
    private const val CODE_DIGITS = 6
    
    /**
     * توليد كود TOTP بناءً على السر والوقت الحالي
     */
    fun generateTOTP(secret: String): String {
        val timeSteps = System.currentTimeMillis() / 1000 / TIME_STEP_SECONDS
        return generateTOTP(secret, timeSteps.toString(), CODE_DIGITS)
    }
    
    /**
     * توليد كود TOTP بناءً على السر وخطوة الوقت المحددة
     */
    private fun generateTOTP(secret: String, timeSteps: String, codeDigits: Int): String {
        try {
            val key = secret.toByteArray()
            val time = hexStringToByteArray(timeSteps.padStart(16, '0'))
            
            val hmacSha256 = Mac.getInstance("HmacSHA256")
            val secretKey = SecretKeySpec(key, "HmacSHA256")
            hmacSha256.init(secretKey)
            val hash = hmacSha256.doFinal(time)
            
            val offset = hash[hash.size - 1].toInt() and 0xF
            val binary = ((hash[offset].toInt() and 0x7F) shl 24) or
                        ((hash[offset + 1].toInt() and 0xFF) shl 16) or
                        ((hash[offset + 2].toInt() and 0xFF) shl 8) or
                        (hash[offset + 3].toInt() and 0xFF)
            
            val otp = binary % Math.pow(10.0, codeDigits.toDouble()).toInt()
            return String.format("%0${codeDigits}d", otp)
        } catch (e: Exception) {
            e.printStackTrace()
            return "000000"
        }
    }
    
    /**
     * التحقق من صحة الكود
     */
    fun verifyCode(secret: String, code: String): Boolean {
        val expectedCode = generateTOTP(secret)
        return expectedCode == code
    }
    
    /**
     * حساب الوقت المتبقي قبل تغيير الكود
     */
    fun getTimeRemaining(): Int {
        val currentTime = System.currentTimeMillis() / 1000
        return (TIME_STEP_SECONDS - (currentTime % TIME_STEP_SECONDS)).toInt()
    }
    
    /**
     * توليد سر عشوائي
     */
    fun generateSecret(): String {
        val random = SecureRandom()
        val bytes = ByteArray(20)
        random.nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }
    
    private fun hexStringToByteArray(hex: String): ByteArray {
        val len = hex.length
        val data = ByteArray(len / 2)
        for (i in 0 until len step 2) {
            data[i / 2] = ((hex[i].digitToInt(16) shl 4) + hex[i + 1].digitToInt(16)).toByte()
        }
        return data
    }
}
