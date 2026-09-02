# سیاست آپدیت و حفظ داده — AS-KharjYar

این سند قرارداد دائمی انتشار نسخه‌های بعدی «خرج‌یار» است. هدف اصلی: نسخه جدید باید روی نسخه قبلی نصب شود و حساب‌ها، تراکنش‌ها و تنظیمات کاربر بدون اجازه صریح او حذف یا Reset نشوند.

## 1) هویت نصب Android

- `applicationId` باید برای تمام نسخه‌های عمومی روی `com.asteam.kharjyar` ثابت بماند.
- `versionCode` در هر انتشار باید افزایش پیدا کند.
- `versionName` باید با نسخه محصول هماهنگ باشد.
- کلید Signing نسخه Release باید ثابت بماند. گم‌کردن یا تغییر بدون برنامه کلید امضا می‌تواند مسیر Update روی نصب قبلی را قطع کند.
- اطلاعات Keystore و رمزها نباید داخل Git ذخیره شوند؛ فقط Local/CI Secret.

## 2) Room Database

هویت فعلی دیتابیس عمداً legacy باقی مانده است:

- Database class: `WalleeDatabase`
- Database file: `wallee-db`

این دو نام صرفاً برای برندینگ نباید تغییر کنند. تغییر نام فایل DB باعث می‌شود برنامه روی نصب قدیمی یک دیتابیس خالی جدید ببیند.

### قانون تغییر Schema

هر بار Entity/Table/Column/Index تغییر می‌کند:

1. `version` در `@Database` دقیقاً یک مرحله یا طبق برنامه افزایش پیدا کند.
2. Migration صریح مانند `Migration(1, 2)` نوشته شود.
3. Migration با `.addMigrations(...)` به `Room.databaseBuilder` ثبت شود.
4. schema جدید در `app/room-schemas` export و commit شود.
5. تست Migration باید حداقل یک دیتابیس نسخه قبل را باز کند و صحت داده‌ها را بعد از ارتقا بررسی کند.
6. در نسخه Release استفاده از `fallbackToDestructiveMigration` ممنوع است.

اگر Migration فراموش شود، رفتار مطلوب Crash/Failure آشکار در توسعه است؛ حذف خودکار داده رفتار قابل قبول نیست.

## 3) DataStore و Proto

نام فایل‌های فعلی DataStore باید ثابت بمانند:

- `credential-preference.pb`
- `user-preference.pb`
- `theme-preference.pb`
- `language-preference.pb`
- `onboarding-preference.pb`

برای فایل‌های `.proto`:

- شماره Field موجود نباید برای مفهوم دیگری دوباره استفاده شود.
- Field حذف‌شده باید `reserved` شود.
- افزودن Field جدید باید با شماره جدید انجام شود.
- Defaultهای جدید باید با نسخه قبلی سازگار باشند.

## 4) Source namespace و نام‌های legacy

نام package سورس فعلی هنوز `com.wisnu.kurniawan.wallee` است. تغییر package سورس به‌خودی‌خود داده Room را حذف نمی‌کند، اما این پروژه علاوه بر کد Kotlin دارای Room schema، Proto، Manifest initializer و تست‌هایی است که به نام‌های کامل کلاس‌ها متکی هستند.

بنابراین migration کامل namespace باید در یک تغییر مستقل، قابل بازبینی و همراه با بررسی همه referenceها انجام شود؛ نه به‌عنوان Search/Replace برندینگ.

## 5) Backup / Restore

Android Auto Backup جایگزین Migration دیتابیس نیست. قبل از فعال‌کردن یا تغییر سیاست Cloud Backup باید مشخص شود:

- چه داده‌ای اجازه خروج از دستگاه دارد؛
- آیا Backup رمزگذاری می‌شود؛
- Restore روی نسخه‌های مختلف دیتابیس چگونه مدیریت می‌شود؛
- سیاست حریم خصوصی با رفتار واقعی هم‌خوان است.

برای نسخه Local-First، قابلیت Backup/Restore دستی و قابل کنترل توسط کاربر گزینه ترجیحی آینده است.

## 6) چک‌لیست قبل از هر Release

- [ ] `applicationId` همان `com.asteam.kharjyar` است.
- [ ] `versionCode` افزایش یافته است.
- [ ] کلید Signing همان کلید انتشار قبلی است.
- [ ] اگر Room تغییر کرده، Migration صریح وجود دارد.
- [ ] هیچ `fallbackToDestructiveMigration` در مسیر Release وجود ندارد.
- [ ] schemaهای Room جدید commit شده‌اند.
- [ ] Field numberهای Proto تغییر معنایی یا reuse نشده‌اند.
- [ ] نصب نسخه جدید روی نسخه قبلی با داده نمونه تست شده است.
- [ ] حساب‌ها، تراکنش‌ها، زبان، پوسته و تنظیمات بعد از Upgrade باقی مانده‌اند.
- [ ] Privacy Policy با قابلیت‌های واقعی نسخه هماهنگ است.

## 7) اصل تصمیم‌گیری

در خرج‌یار، «حفظ داده کاربر» بر «پاکیزگی نام‌های داخلی و برندینگ کامل namespace» اولویت دارد. هر rename یا refactor که احتمال ایجاد storage identity جدید دارد باید با Migration صریح انجام شود.
