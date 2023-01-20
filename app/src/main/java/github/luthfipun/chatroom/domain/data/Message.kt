package github.luthfipun.chatroom.domain.data

import github.luthfipun.chatroom.domain.util.MessageInfoType
import github.luthfipun.chatroom.domain.util.MessageType

data class Message(
    val id: Long,
    val text: String?,
    val type: MessageType,
    val time: String,
    val user: UserInfo,
    val infoType: MessageInfoType?,
    val isOwner: Boolean = false,
    val isParent: Boolean = true
)