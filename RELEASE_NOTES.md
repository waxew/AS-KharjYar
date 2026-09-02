# AS-KharjYar 1.2.0

Release tag: `v1.2.0+12`

## Highlights

- Persian/RTL product identity for خرج‌یار with the unified AS Team right-side drawer.
- Local-first expense, income, account, and transaction management.
- Manual financial database backup and restore through Android Storage Access Framework.
- Restore validation for SQLite integrity, Room schema version, required tables, and Room identity hash.
- Atomic database replacement with rollback protection and clean application restart.
- Version-aware update checker using the public GitHub Releases API and Android `versionCode`.
- Automatic Android cloud backup disabled; financial data leaves the device only when the user explicitly exports a backup.
- Firebase Analytics and Crashlytics removed from the AS build.
- Release quality gates include unit tests, Android Lint, Debug APK, Release APK, Release AAB, and emulator upgrade/runtime smoke testing.

## Stable Android identity

- Package: `com.asteam.kharjyar`
- Version name: `1.2.0`
- Version code: `12`
- Database filename retained for update compatibility: `wallee-db`
- Database schema version: `1`

## Update compatibility

The application ID, database filename, DataStore identities, and explicit data-safety rules are retained to preserve user data across updates. Destructive Room migration fallback is not enabled.

## Signing

Production release artifacts must be signed with the permanent AS-KharjYar release certificate documented in `docs/RELEASE_SIGNING.md`. Private keystore material is intentionally never stored in this repository.
