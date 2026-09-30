# SIM Number Setter

SIM Number Setter is a small Xposed module that invokes normally unused Android System code to set
the "subscriber number" on the device's SIM card. This is the number displayed in the system
settings, and used in apps such as Google Messages, as well as being available to third party apps
with sufficient permissions.

The number is not always set by carriers (leaving "Unknown"), or can be lost or invalid after
porting a number to a different SIM.

This small app allows you to fix it, permanently, using a rooted device with Xposed.

**If you don't have a rooted device, or don't want to root your main device, you may wish to
consider rooting an old device, and temporarily putting your SIM into that device, using this app,
and then putting it back in your main device - the fixed number will travel with it**

### Important Notice

Changing the SIM number is semi-permanent: the number change will survive reboots, uninstalling this
app, even switching the SIM to a different device, but can be changed again at any time using this
app.

SIM Number Setter does NOT:
- Change your actual phone number, no matter what you enter. You must contact your carrier if you wish to port your number or switch network.
- Unblock a network blocked SIM.
- Give you free data or calls.
- Change your IMEI.

SIM Number Setter uses the built-in Android methods to write data to the SIM.
The app nor the developer are not responsible for issues with the process, including any damage to
the SIM or network issues.

### Requirements

- Android 8.1 - 17
- A rooted device (SukiSU Ultra, KernelSU, or Magisk)
- Zygisk Next
- Vector 2.2 with libxposed API 102

**Vector 2.1 or older will not work.** If you currently have Vector 2.1 installed, uninstall the
manager app, reboot, and install Vector 2.2 fresh.

### Installation

1. Install SukiSU Ultra, Zygisk Next, and Vector 2.2
2. Install the SIM Number Setter APK
3. Enable the module in Vector and scope it to `com.android.phone`
4. Reboot the device
5. Open the app, grant superuser when prompted, grant the `DUMP` permission when prompted
6. Set the number and save

After the first install, scope changes only require a force-stop of `com.android.phone`, not a full
reboot.

### Building

Requires JDK 17, Gradle 9.8.0, AGP 9.4.0, Kotlin 2.4.20.

git clone https://github.com/itszoney/SIMNumberSetter.git
cd SIMNumberSetter
gradle wrapper --gradle-version 9.8.0 --distribution-type bin
./gradlew assembleDebug

The APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

Verify the module manifest packaged correctly:

unzip -l app-debug.apk | grep META-INF/xposed

You should see three lines:

META-INF/xposed/java_init.list
META-INF/xposed/module.prop
META-INF/xposed/scope.list

### Troubleshooting

If the module shows as enabled in Vector but nothing happens, check the packaged manifest. The
entry file must be named `java_init.list` inside the APK. Anything else, including
`java_initlist.list`, is invisible to the framework and the module will never load.

If logcat shows `bindService returned false`, the merged manifest is missing the package query:

<queries>
    <package android:name="com.android.phone"/>
</queries>

If logcat shows `onNullBinding`, the caller UID check inside the hook failed. Compare the value
from:

adb shell dumpsys package com.kieronquinn.app.simnumbersetter | grep userId

against the `pendingIntent.creatorUid` seen inside the hook.

If the `DUMP` grant fails, some OEMs (notably Xiaomi) require the developer-options toggle
"USB debugging (Security Settings) - Allow issuing permissions" to be enabled before `pm grant`
succeeds.

### Screenshots

[![Screenshots](https://i.imgur.com/UNKADmrl.png)](https://i.imgur.com/UNKADmr.png)

### Download

[Download from releases page](https://github.com/itszoney/SIMNumberSetter/releases)

### Credits

SIM Number Setter was originally written by Kieron Quinn. The hook design, the
`TelephonyDebugService` bind path, the AIDL contract, and the UI are his work. Original repository:
https://github.com/KieronQuinn/SIMNumberSetter

This fork carries the module to Android 17 (API 37) and libxposed API 102. The port covers:
- Migration from the legacy `de.robv.android.xposed` API to `io.github.libxposed:api:102.0.0`
- Module config moved from AndroidManifest meta-data to `META-INF/xposed/` resources
- Caller verification by UID, for the Android 15+ package-visibility model
- `PhoneFactory.getDefaultPhone` dispatched to the main Looper, avoiding the binder-thread crash
- Toolchain upgrade to AGP 9.4, Gradle 9.8, Kotlin 2.4, JDK 17
- Replacement of deprecated `launchWhenResumed` / `launchWhenCreated` with `repeatOnLifecycle`
- Replacement of `WindowCompat.setDecorFitsSystemWindows` with `enableEdgeToEdge`
- Migration to libsu 6.0.0 (`Shell.cmd` and `Shell.isAppGrantedRoot`)

The module does not exist without the frameworks it rests on:
- LSPosed - https://github.com/LSPosed/LSPosed
- Vector - https://github.com/JingMatrix/LSPosed
- SukiSU Ultra - https://github.com/SukiSU-Ultra/SukiSU-Ultra
- KernelSU - https://github.com/tiann/KernelSU
- Zygisk Next - https://github.com/Dr-TSNG/ZygiskNext
- libsu - https://github.com/topjohnwu/libsu
- MonetCompat - https://github.com/KieronQuinn/MonetCompat
- Koin - https://github.com/InsertKoinIO/koin

### License

GPL-3.0-only, same as the original. Any redistribution must preserve the license and credit the
original author.
