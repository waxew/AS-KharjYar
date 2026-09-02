# سیاست حریم خصوصی خرج‌یار (AS-KharjYar)

آخرین بازبینی: ۲ سپتامبر ۲۰۲۶

خرج‌یار یک برنامه مدیریت هزینه، درآمد، حساب و تراکنش شخصی است که نسخه AS Team آن با رویکرد Local-First توسعه داده می‌شود.

## داده‌های ذخیره‌شده

اطلاعاتی که کاربر در برنامه ایجاد می‌کند، از جمله حساب‌ها، تراکنش‌ها، موجودی‌ها، تنظیمات، زبان، پوسته و واحد پول انتخابی، در حافظه محلی دستگاه و دیتابیس داخلی برنامه نگهداری می‌شوند.

## ارسال اطلاعات به سرویس‌های ثالث

نسخه فعلی AS-KharjYar برای Analytics یا Crash Reporting از Firebase Analytics یا Firebase Crashlytics استفاده نمی‌کند. انتخاب کشور و واحد پول، تراکنش‌ها و اطلاعات مالی کاربر برای این سرویس‌ها ارسال نمی‌شوند.

کد فعلی بخش ورود نیز سرویس احراز هویت آنلاین ندارد؛ اطلاعات ورود در پیاده‌سازی فعلی به سرور خارجی ارسال نمی‌شود.

## حذف اطلاعات

کاربر می‌تواند با پاک‌کردن Data برنامه از تنظیمات Android یا حذف برنامه، داده‌های محلی آن را از دستگاه پاک کند. در نسخه‌های آینده که قابلیت Backup/Restore اضافه شود، سیاست مربوط به نسخه‌های پشتیبان نیز باید در همین سند به‌روزرسانی شود.

## مجوزها و دسترسی‌ها

خرج‌یار باید فقط مجوزهایی را درخواست کند که برای قابلیت‌های فعال برنامه لازم هستند. هر قابلیت آینده که نیازمند دسترسی جدید یا انتقال داده باشد، قبل از انتشار باید در این سند ثبت شود.

## منبع و مجوز

AS-KharjYar بر پایه پروژه متن‌باز Compose Expense / Wallee توسعه یافته و مجوز Apache License 2.0 و attribution مربوط به پروژه اصلی در مخزن حفظ شده است. تغییرات، فارسی‌سازی و شخصی‌سازی نسخه خرج‌یار توسط AS Team انجام می‌شود.

## ارتباط با پشتیبانی

برای پرسش‌های مربوط به حریم خصوصی یا گزارش مشکل امنیتی:

AS.Developers.Support@Gmail.Com

---

# KharjYar Privacy Policy

Last reviewed: September 2, 2026

KharjYar is a local-first personal expense, income, account, and transaction manager maintained as the AS Team build of the project.

User-created financial data and app preferences are stored locally on the device. The current AS-KharjYar build does not use Firebase Analytics or Firebase Crashlytics, and financial records, country selection, and currency selection are not sent to those services.

The current login implementation does not authenticate against an external server. Local app data can be removed by clearing the application's data or uninstalling the application.

If a future version introduces cloud backup, synchronization, remote authentication, advertising, or another service that transfers user data, this policy must be updated before that version is published.

Security and privacy questions can be sent to AS.Developers.Support@Gmail.Com.
