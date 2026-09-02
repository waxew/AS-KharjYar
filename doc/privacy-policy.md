# سیاست حریم خصوصی خرج‌یار (AS-KharjYar)

آخرین بازبینی: ۲ سپتامبر ۲۰۲۶

خرج‌یار یک برنامه مدیریت هزینه، درآمد، حساب و تراکنش شخصی است که نسخه AS Team آن با رویکرد Local-First توسعه داده می‌شود.

## داده‌های ذخیره‌شده

اطلاعاتی که کاربر ایجاد می‌کند شامل حساب‌ها، تراکنش‌ها، موجودی‌ها، تنظیمات، زبان، پوسته و واحد پول انتخابی است و به‌صورت محلی روی دستگاه نگهداری می‌شود.

## ارسال اطلاعات به سرویس‌های ثالث

نسخه فعلی AS-KharjYar از Firebase Analytics یا Firebase Crashlytics استفاده نمی‌کند. اطلاعات مالی، انتخاب کشور، واحد پول و تراکنش‌های کاربر به سرویس‌های تحلیلی یا تبلیغاتی ارسال نمی‌شوند.

قابلیت «بررسی بروزرسانی» فقط پس از اقدام کاربر به API عمومی GitHub Releases برای مخزن رسمی AS-KharjYar متصل می‌شود تا متادیتای آخرین نسخه منتشرشده را دریافت کند. هیچ حساب، تراکنش، موجودی، نام نمایشی، تصویر پروفایل یا فایل پشتیبان در این درخواست ارسال نمی‌شود. مانند هر ارتباط HTTPS عادی، GitHub ممکن است اطلاعات فنی متداول شبکه مانند IP و User-Agent را طبق سیاست‌های خودش پردازش کند.

## Backup و Restore

خرج‌یار دارای Backup/Restore دستی داخل برنامه است. این قابلیت فقط با اقدام مستقیم کاربر اجرا می‌شود و فایل پشتیبان تا زمانی که کاربر آن را به سرویس دیگری منتقل نکند، روی دستگاه باقی می‌ماند.

Android Cloud Backup و Device Transfer خودکار برای جلوگیری از انتقال ناخواسته اطلاعات مالی غیرفعال شده‌اند.

## حذف اطلاعات

کاربر می‌تواند با پاک‌کردن Data برنامه از تنظیمات Android یا حذف برنامه، داده‌های محلی را حذف کند.

## مجوزها و دسترسی‌ها

دسترسی اینترنت فقط برای بررسی دستی نسخه جدید در GitHub Releases استفاده می‌شود. انتخاب فایل Backup/Restore از Android Storage Access Framework انجام می‌شود و برنامه مجوز گسترده دسترسی به حافظه عمومی درخواست نمی‌کند.

## منبع و مجوز

AS-KharjYar بر پایه پروژه متن‌باز Compose Expense / Wallee توسعه یافته و مجوز Apache License 2.0 و attribution مربوط به پروژه اصلی در مخزن حفظ شده است.

## ارتباط با پشتیبانی

AS.Developers.Support@Gmail.Com

---

# KharjYar Privacy Policy

Last reviewed: September 2, 2026

KharjYar is a local-first personal expense and transaction manager maintained as the AS Team build.

User-created financial data and preferences are stored locally on the device. The application does not use Firebase Analytics or Firebase Crashlytics, and financial records are not sent to analytics or advertising services.

When the user explicitly taps Check for updates, KharjYar contacts the public GitHub Releases API for the official AS-KharjYar repository to retrieve release metadata. Accounts, transactions, balances, profile details, and backup files are not included in this request. GitHub may process ordinary HTTPS connection metadata such as IP address and User-Agent under its own policies.

Manual Backup and Restore is controlled by the user inside the application. Automatic Android cloud backup and device transfer are disabled to prevent unintended transfer of financial data. File selection uses Android's Storage Access Framework without broad storage permission.

Security and privacy questions can be sent to AS.Developers.Support@Gmail.Com.
