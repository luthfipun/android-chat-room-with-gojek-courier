package github.luthfipun.chatroom

import github.luthfipun.chatroom.domain.data.Message
import github.luthfipun.chatroom.domain.data.UserInfo
import github.luthfipun.chatroom.domain.util.MessageType
import github.luthfipun.chatroom.domain.util.messageBodyType
import org.junit.Assert
import org.junit.Test

class MessageBodyTypeTest {

    @Test
    fun `test message body type returns valid types`(){
        val fakeUser = UserInfo(
            id = 1L,
            name = "test",
            avatar = 1
        )

        val fakeMessages = mutableListOf<Message>()
        repeat(4){
            fakeMessages.add(
                Message(
                    id = System.currentTimeMillis(),
                    text = "text $it",
                    type = MessageType.TEXT,
                    time = System.currentTimeMillis().toString(),
                    user = fakeUser,
                    infoType = null,
                    isOwner = true
                )
            )
        }

        val messageBodyType = messageBodyType(fakeMessages)
        Assert.assertArrayEquals(
            arrayOf(false, false, false, true),
            messageBodyType.map { it.isParent }.toTypedArray()
        )
    }

    @Test
    fun `test message body type with message info returns valid types`(){
        val fakeUser = UserInfo(
            id = 1L,
            name = "test",
            avatar = 1
        )

        val fakeMessages = mutableListOf<Message>()
        repeat(4){
            if (it == 1 || it == 2){
                fakeMessages.add(
                    Message(
                        id = System.currentTimeMillis(),
                        text = "text $it",
                        type = MessageType.TEXT,
                        time = System.currentTimeMillis().toString(),
                        user = fakeUser,
                        infoType = null,
                        isOwner = true
                    )
                )
            }else {
                fakeMessages.add(
                    Message(
                        id = System.currentTimeMillis(),
                        text = "",
                        type = MessageType.INFO,
                        time = System.currentTimeMillis().toString(),
                        user = fakeUser,
                        infoType = null,
                        isOwner = true
                    )
                )
            }
        }

        val type = messageBodyType(fakeMessages)
        Assert.assertArrayEquals(
            arrayOf(false, false, true, true),
            type.map { it.isParent }.toTypedArray()
        )
    }
}