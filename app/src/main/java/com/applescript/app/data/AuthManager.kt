package com.applescript.app.data

import android.content.Context
import android.content.SharedPreferences
import com.applescript.app.util.TOTPUtil

class AuthManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_ACCOUNT_ID = "account_id"
        private const val KEY_SECRET = "secret"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    /**
     * حفظ بيانات الحساب
     */
    fun saveAccount(accountId: String, secret: String) {
        prefs.edit()
            .putString(KEY_ACCOUNT_ID, accountId)
            .putString(KEY_SECRET, secret)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }
    
    /**
     * التحقق من بيانات الدخول
     */
    fun verifyCredentials(accountId: String, code: String): Boolean {
        val savedAccountId = prefs.getString(KEY_ACCOUNT_ID, null)
        val savedSecret = prefs.getString(KEY_SECRET, null)
        
        if (savedAccountId != accountId || savedSecret == null) {
            return false
        }
        
        return TOTPUtil.verifyCode(savedSecret, code)
    }
    
    /**
     * تسجيل الخروج
     */
    fun logout() {
        prefs.edit()
            .clear()
            .apply()
    }
    
    /**
     * التحقق من حالة تسجيل الدخول
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * الحصول على معرف الحساب
     */
    fun getAccountId(): String? {
        return prefs.getString(KEY_ACCOUNT_ID, null)
    }
    
    /**
     * إنشاء حساب جديد
     */
    fun createAccount(accountId: String): String {
        val secret = TOTPUtil.generateSecret()
        saveAccount(accountId, secret)
        return secret
    }
}
