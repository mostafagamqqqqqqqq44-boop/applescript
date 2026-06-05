# سكربت التفاحه - Apple Script

تطبيق Android احترافي مع نظام مصادقة يستخدم كود سري متغير كل 60 دقيقة (TOTP).

## المميزات

- ✅ تسجيل دخول آمن باستخدام معرف الحساب وكود سري متغير
- ✅ كود TOTP يتغير كل 60 ثانية
- ✅ تصميم عصري وجذاب باستخدام Jetpack Compose
- ✅ واجهة مستخدم باللغة العربية
- ✅ شاشة تسجيل دخول أنيقة
- ✅ شاشة رئيسية تعرض الكود الحالي والوقت المتبقي
- ✅ تشفير HMAC-SHA256 للأمان

## التقنيات المستخدمة

- **Kotlin** - لغة البرمجة
- **Jetpack Compose** - لبناء واجهة المستخدم
- **Material Design 3** - نظام التصميم
- **Navigation Compose** - للتنقل بين الشاشات
- **TOTP (Time-based One-Time Password)** - للكود السري المتغير

## هيكل المشروع

```
app/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/applescript/app/
│   │       │   ├── MainActivity.kt
│   │       │   ├── data/
│   │       │   │   └── AuthManager.kt
│   │       │   ├── ui/
│   │       │   │   ├── screens/
│   │       │   │   │   ├── LoginScreen.kt
│   │       │   │   │   └── HomeScreen.kt
│   │       │   │   └── theme/
│   │       │   │       ├── Theme.kt
│   │       │   │       └── Type.kt
│   │       │   └── util/
│   │       │       └── TOTPUtil.kt
│   │       ├── res/
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

## كيفية التشغيل

1. افتح المشروع في Android Studio
2. انتظر حتى يتم تحميل التبعيات
3. قم بتوصيل جهاز Android أو ابدأ محاكي
4. اضغط على زر Run

## نظام الكود السري

التطبيق يستخدم خوارزمية TOTP (Time-based One-Time Password):
- الكود يتغير كل 60 ثانية
- يستخدم تشفير HMAC-SHA256
- الكود مكون من 6 أرقام

## الأمان

- تخزين محلي آمن للبيانات
- تشفير قوي للكود السري
- لا يتم إرسال البيانات إلى سيرفر خارجي

## بناء APK باستخدام GitHub Actions (مجاني)

بما أن جهازك ضعيف، يمكنك بناء APK تلقائياً على GitHub Actions مجاناً:

### الخطوات:

1. **إنشاء حساب GitHub** (إذا لم يكن لديك)
   - اذهب لـ: https://github.com
   - سجل حساب جديد

2. **إنشاء Repository جديد**
   - اضغط على **+** في أعلى اليمين
   - اختر **New repository**
   - اسمه: `applescript`
   - اختر **Public** أو **Private**
   - اضغط **Create repository**

3. **رفع المشروع إلى GitHub**
   - افتح الترمنال في VS Code
   - تأكد أنك في مجلد المشروع: `c:\Users\911\Desktop\app`
   - شغل الأوامر التالية:

```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/USERNAME/applescript.git
git push -u origin main
```

*(استبدل USERNAME باسم المستخدم الخاص بك على GitHub)*

4. **تفعيل GitHub Actions**
   - بعد الرفع، اذهب لصفحة Repository على GitHub
   - اضغط على **Actions** في الشريط العلوي
   - سيبدأ البناء تلقائياً

5. **تحميل APK**
   - انتظر انتهاء البناء (يستغرق 5-10 دقائق)
   - اضغط على **Actions** → اختر آخر build
   - في الأسفل تحت **Artifacts**، اضغط على **debug-apk**
   - حمل الملف: `app-debug.apk`

### تفعيل البناء اليدوي:
- في صفحة GitHub Actions
- اضغط **Run workflow** → **Run workflow**
- سيبدأ البناء فوراً

## التطوير المستقبلي

- [ ] إضافة قاعدة بيانات سيرفر
- [ ] إضافة ميزة النسخ الاحتياطي
- [ ] إضافة دعم البصمة/الوجه
- [ ] إضافة إعدادات متقدمة
