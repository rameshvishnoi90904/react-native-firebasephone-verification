# react-native-phone-verification
React Native Android custom component for firebase phone authentication

Installation
> npm install react-native-phone-verification

or

> git clone https://github.com/rameshvishnoi90904/react-native-phone-verification.git

##step 1: In settings.gradle
```
include ':ReactNativePhoneVerification'
project(':ReactNativePhoneVerification').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-phone-verification/android')

```

##step 2: In android/app/build.gradle
```
dependencies {
compile project(':ReactNativePhoneVerification')
}
```

##step 3: In MainApplication.java

```

import com.reactnative.phoneverification.PhoneVerificationPackage;

@Override protected List getPackages() {
  return Arrays.asList(
    new MainReactPackage(),
    new PhoneVerificationPackage()
  );
}

```
##step 4: In index.js
```javascript
import PhoneVerification from 'react-native-phone-verification';

// invoke this function to initiate phone verification
PhoneVerification.sendVerificationCode("+91","9004******");


// invoke this function to verify code
PhoneVerification.verify("123456");

```
