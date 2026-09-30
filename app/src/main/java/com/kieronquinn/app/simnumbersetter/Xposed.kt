package com.kieronquinn.app.simnumbersetter

import android.content.Intent
import com.kieronquinn.app.simnumbersetter.service.PhoneNumberSetterService
import com.kieronquinn.app.simnumbersetter.utils.extensions.checkSecurity
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface

/**
 *  Xposed hook to allow binding to TelephonyDebugService (an exported, but protected service in
 *  com.android.phone), returning [IPhoneNumberSetter]. This can then be accessed via the UI
 *  and used to query and set the number.
 */
class Xposed : XposedModule() {

    override fun onPackageReady(param: XposedModuleInterface.PackageReadyParam) {
        if (param.packageName != "com.android.phone") return
        hookTelephonyDebugService(param)
    }

    private fun hookTelephonyDebugService(param: XposedModuleInterface.PackageReadyParam) {
        val serviceClass = param.classLoader.loadClass(
            "com.android.phone.TelephonyDebugService"
        )
        val onBind = serviceClass.getDeclaredMethod("onBind", Intent::class.java)
        hook(onBind)
            .setId("telephony-debug-onbind")
            .intercept { chain ->
                val intent = chain.getArg(0) as Intent
                val ctx = com.android.internal.telephony.PhoneFactory
                    .getDefaultPhone()
                    ?.context
                if (ctx != null && intent.checkSecurity(ctx)) {
                    PhoneNumberSetterService()
                } else {
                    chain.proceed()
                }
            }
    }
}
