package github.luthfipun.chatroom.domain.util

import android.content.Context
import android.text.format.DateUtils

fun Context.formatMessageTime(time: Long): String {
    return DateUtils.formatDateTime(this, time, DateUtils.FORMAT_SHOW_TIME)
}

fun Context.formatMessageInfo(time: Long): String {
    return if (DateUtils.isToday(time)){
        DateUtils.formatDateTime(this, time, DateUtils.FORMAT_SHOW_TIME)
    }else DateUtils.formatDateTime(this, time, DateUtils.FORMAT_SHOW_WEEKDAY)
}