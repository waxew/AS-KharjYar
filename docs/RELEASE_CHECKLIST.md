# AS-KharjYar Final Release Checklist

## Build

- [ ] assembleDebug succeeds
- [ ] assembleRelease succeeds
- [ ] lintRelease succeeds
- [ ] unit tests succeed

## Identity

- [ ] applicationId remains `com.asteam.kharjyar`
- [ ] versionCode increased
- [ ] release keystore is unchanged

## Data safety

- [ ] Upgrade tested over previous version
- [ ] Room migration tested when schema changes
- [ ] Backup/Restore tested
- [ ] User data preserved

## Store package

- [ ] Signed APK/AAB generated
- [ ] SHA-256 checksum generated
- [ ] Signature verification completed
- [ ] Release notes prepared
