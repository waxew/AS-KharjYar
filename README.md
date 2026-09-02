# AS-KharjYar | خرج‌یار

«خرج‌یار» نسخه شخصی‌سازی‌شده AS Team برای مدیریت هزینه‌ها، درآمدها، حساب‌ها و تراکنش‌های شخصی است. رابط برنامه با Jetpack Compose پیاده‌سازی شده و معماری نسخه AS تا حد ممکن Local-First نگه داشته می‌شود.

## هویت پروژه

- Repository: `AS-KharjYar`
- Application ID: `com.asteam.kharjyar`
- Product name: `خرج‌یار`
- Current AS version: `1.2.0` (`versionCode 12`)
- Primary language: Persian (`fa`)
- Minimum SDK: 21
- Target SDK: 35
- Java: 17
- Gradle root project: `AS-KharjYar`

> نام package سورس و بعضی نام‌های داخلی legacy هنوز `Wallee` هستند. این موارد عمداً به‌صورت مرحله‌ای مهاجرت می‌شوند تا هویت دیتابیس و داده‌های نصب‌های موجود با یک rename ساده آسیب نبینند.

## قابلیت‌ها و تغییرات AS Team

- نام و هویت نصب اختصاصی «خرج‌یار» با `com.asteam.kharjyar`
- فارسی‌سازی گسترده رابط و پشتیبانی RTL
- فارسی به‌عنوان زبان شروع نصب جدید، با امکان انتخاب زبان از داخل برنامه
- تشخیص امن کشور دستگاه و پیشنهاد واحد پول مناسب
- نمایش نام کشورها بر اساس زبان فعال برنامه
- فرمت فارسی برای واحد پول ایران
- مدیریت هزینه، درآمد، حساب و تراکنش روی دیتابیس داخلی Room
- Drawer سمت راست مطابق استاندارد مشترک AS Team
- ترتیب استاندارد Drawer: «تنظیمات» در index 0 و «اشتراک‌گذاری» در index 1
- عکس پروفایل دایره‌ای با انتخاب از فایل/گالری و نگهداری محلی URI
- نام نمایشی قابل ویرایش و ذخیره محلی
- خانه، پوسته و ظاهر، تماس با ما، درباره نرم‌افزار و نمایش نسخه
- دکمه بررسی بروزرسانی از بخش About و هدایت به Releases رسمی همین مخزن
- Back هنگام بازبودن Drawer ابتدا منو را می‌بندد
- حذف Firebase Analytics و Firebase Crashlytics از نسخه AS
- حذف dependencyهای شبکه بلااستفاده از مسیر اصلی برنامه
- عدم ارسال انتخاب کشور/ارز و اطلاعات onboarding به سرویس‌های Analytics/Crash Reporting
- نگهداری اطلاعات signing خارج از Git و فقط در Local/CI secrets

## حفظ داده و آپدیت‌خور بودن

`applicationId` عمومی باید روی `com.asteam.kharjyar` ثابت بماند و `versionCode` در هر Release افزایش پیدا کند.

Room در نسخه AS اجازه migration مخرب خودکار ندارد. `fallbackToDestructiveMigration` از مسیر دیتابیس حذف شده است؛ بنابراین هر تغییر Schema باید Migration صریح داشته باشد. نام legacy دیتابیس `wallee-db` نیز برای سازگاری نصب‌های قبلی عمداً حفظ می‌شود.

نام فایل‌های DataStore نیز هویت پایدار داده هستند و صرفاً برای برندینگ rename نمی‌شوند.

راهنمای کامل این سیاست:

- [`doc/UPDATE_AND_DATA_MIGRATION.md`](doc/UPDATE_AND_DATA_MIGRATION.md)

## حریم خصوصی

سیاست فعلی پروژه در فایل زیر ثبت شده است:

- [`doc/privacy-policy.md`](doc/privacy-policy.md)

نسخه فعلی AS-KharjYar از Firebase Analytics و Firebase Crashlytics استفاده نمی‌کند. در صورت اضافه‌شدن هر قابلیت آنلاین، Backup ابری، تبلیغات یا همگام‌سازی، رفتار واقعی برنامه و Privacy Policy باید هم‌زمان به‌روزرسانی شوند.

## فناوری‌ها

- Kotlin
- Jetpack Compose / Material 3
- Coroutines و Flow
- Room
- DataStore / Protocol Buffers
- Navigation Compose
- Hilt
- AndroidX App Locales

## ساخت پروژه

فرمان ساخت Debug:

```bash
./gradlew assembleDebug
```

فرمان تست واحد:

```bash
./gradlew testDebug
```

اطلاعات signing نسخه Release نباید داخل Git قرار بگیرد. فایل `keystore.properties` فقط باید به‌صورت local یا از طریق CI secret تأمین شود.

## منبع و مجوز

این پروژه بر پایه پروژه متن‌باز [Compose Expense / Wallee](https://github.com/wisnukurniawan/Compose-Expense) از `wisnukurniawan` توسعه داده شده است.

سورس upstream تحت **Apache License 2.0** منتشر شده است. فایل `LICENSE` اصلی در این مخزن حفظ شده و attribution پروژه اصلی نیز در این README ثبت شده است. تغییرات و شخصی‌سازی‌های AS Team به‌عنوان تغییرات مشتق‌شده روی همان پایه انجام می‌شوند.

## AS Team

Develop by AS Team Group  
Support: `AS.Developers.Support@Gmail.Com`
