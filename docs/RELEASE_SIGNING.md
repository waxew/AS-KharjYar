# AS-KharjYar Release Signing

## Policy

Release signing is part of the application identity. The same keystore must be used for every future update.

Private files:
- `*.jks`
- `keystore.properties`

must never be committed.

## Current release certificate

SHA-256 certificate fingerprint:

`38:F0:6B:51:5B:E5:81:CA:F7:63:1B:4A:21:1A:8D:FE:25:79:F4:90:02:B5:90:98:FA:C3:35:B1:A4:3D:9B:B3`

Keystore SHA-256:

`eb6d07e36f4bd445f1b6bd1dc121c3a00c2c2557ce5e3a3fe9583930ac788102`

Before publishing, configure CI secrets or a local release environment with this keystore.
