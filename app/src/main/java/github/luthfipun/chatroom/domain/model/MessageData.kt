package github.luthfipun.chatroom.domain.model

import android.content.Context
import github.luthfipun.chatroom.domain.data.Message
import github.luthfipun.chatroom.domain.data.UserInfo
import github.luthfipun.chatroom.domain.util.*

data class MessageData(
    val id: Long = System.currentTimeMillis(),
    val text: String = "",
    val type: MessageType = MessageType.TEXT,
    val time: Long = System.currentTimeMillis(),
    val user: UserInfo,
    val infoType: MessageInfoType?
)

fun MessageData.toMessage(context: Context): Message {
    return Message(
        id = this.id,
        text = this.text,
        type = this.type,
        time = if (this.type == MessageType.TEXT) context.formatMessageTime(this.time) else context.formatMessageInfo(this.time),
        user = this.user,
        infoType = this.infoType,
        isOwner = false,
        isParent = false,
        status = MessageStatus.SEND
    )
}