package com.kieronquinn.app.simnumbersetter.service

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.android.internal.telephony.Phone
import com.android.internal.telephony.PhoneFactory
import com.kieronquinn.app.simnumbersetter.IPhoneNumberSetter
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

/**
 *  Proxy Service for calls to [Phone] methods to be called via the [IPhoneNumberSetter] service.
 */
class PhoneNumberSetterService: IPhoneNumberSetter.Stub() {

    private fun getPhone(): Phone? {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return runCatching { PhoneFactory.getDefaultPhone() }.getOrNull()
        }
        val result = ArrayBlockingQueue<Phone?>(1)
        Handler(Looper.getMainLooper()).post {
            result.offer(runCatching { PhoneFactory.getDefaultPhone() }.getOrNull())
        }
        return result.poll(5, TimeUnit.SECONDS)
    }

    override fun setLine1Number(number: String, onComplete: Message?): Boolean {
        val phone = getPhone() ?: return false
        var tag = phone.line1AlphaTag
        if(tag.isNullOrEmpty()){
            tag = "Voice Line 1"
        }
        return phone.setLine1Number(tag, number, onComplete)
    }

    override fun getLine1Number(): String {
        return getPhone()?.line1Number ?: ""
    }

}
