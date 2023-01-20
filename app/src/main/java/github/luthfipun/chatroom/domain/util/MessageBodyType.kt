package github.luthfipun.chatroom.domain.util

import github.luthfipun.chatroom.domain.data.Message

fun messageBodyType(messages: List<Message>): List<Message> {
    return messages.mapIndexed { index, message ->
        val nextIndex = index+1
        val isNext =  nextIndex < messages.size
        var isParent = true
        if (isNext){
            isParent = if (messages[index+1].user.id != messages[index].user.id){
                true
            }else {
                messages[index+1].type == MessageType.INFO
            }
        }
        message.copy(isParent = isParent)
    }
}