# AS-KharjYar | خرج‌یار

[![AS Android CI](https://github.com/waxew/AS-KharjYar/actions/workflows/as-android-ci.yml/badge.svg)](https://github.com/waxew/AS-KharjYar/actions/workflows/as-android-ci.yml)

«خرج‌یار» نسخه شخصی‌سازی‌شده AS Team برای مدیریت هزینه‌ها، درآمدها، حساب‌ها و تراکنش‌های شخصی است. رابط اصلی برنامه با Jetpack Compose پیاده‌سازی شده و داده‌های مالی در لایه محلی برنامه نگهداری می‌شوند.

## هویت پروژه

- Repository: `AS-KharjYar`
- Application ID: `com.asteam.kharjyar`
- Product name: `خرج‌یار`
- Current AS version: `1.2.0` (`versionCode 12`)
- Primary language: Persian (`fa`)
- Minimum SDK: 21
- Target SDK: 35
- Java: 17

## تغییرات AS Team

- تغییر نام محصول به «خرج‌یار» و Application ID به `com.asteam.kharjyar`
- افزودن زبان فارسی و شروع نصب جدید با locale فارسی
- پشتیبانی RTL برای رابط فارسی
- افزودن منوی همبرگری سمت راست مطابق الگوی مشترک AS Team
- نمایش نسخه، درباره نرم‌افزار، تماس با پشتیبانی و اشتراک‌گذاری از Drawer
- حذف اتصال Firebase Analytics و Crash Reporting پروژه upstream از نسخه AS
- محلی نگه‌داشتن تحلیل تراکنش‌ها
- جداکردن اطلاعات signing از Git و نگهداری آن در local/CI secrets
- افزودن GitHub Actions برای ساخت Debug APK روی هر push و pull request

## فناوری‌ها

- Kotlin
- Jetpack Compose
- Coroutines و Flow
- Room
- DataStore
- Navigation Compose
- Hilt
- Material 3

## ساخت پروژه

```bash
./gradlew assembleDebug
```

برای تست واحد:

```bash
./gradlew testDebug
```

اطلاعات signing نسخه Release نباید داخل Git قرار بگیرد. فایل `keystore.properties` فقط باید به‌صورت local یا از طریق CI secrets تأمین شود.

## ساختار توسعه AS

تغییرات برندینگ، فارسی‌سازی و قابلیت‌های اختصاصی AS Team روی همین مخزن ادامه پیدا می‌کنند. هنگام تغییر مدل‌های Room/DataStore باید Migration سازگار نوشته شود تا نسخه‌های بعدی بتوانند روی نسخه قبلی نصب شوند و اطلاعات کاربر حفظ شود.

## منبع و مجوز

این پروژه بر پایه پروژه متن‌باز [Compose Expense / Wallee](https://github.com/wisnukurniawan/Compose-Expense) از `wisnukurniawan` توسعه داده شده است.

سورس upstream تحت **Apache License 2.0** منتشر شده است. فایل `LICENSE` اصلی در این مخزن حفظ شده و attribution پروژه اصلی نیز در این README ثبت شده است. تغییرات و شخصی‌سازی‌های AS Team به‌عنوان تغییرات مشتق‌شده روی همان پایه انجام می‌شوند.

## AS Team

Develop by AS Team Group  
Support: `AS.Developers.Support@Gmail.Com`
