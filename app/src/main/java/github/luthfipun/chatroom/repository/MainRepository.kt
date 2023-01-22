package github.luthfipun.chatroom.repository

import github.luthfipun.chatroom.domain.data.Message
import github.luthfipun.chatroom.domain.model.MessageData
import github.luthfipun.chatroom.domain.util.Constant
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun connect()
    suspend fun disconnect()
    suspend fun subscribe(topic: String = Constant.BROKER_TOPIC): Flow<Message>
    suspend fun unsubscribe(topic: String = Constant.BROKER_TOPIC)
    suspend fun publish(topic: String = Constant.BROKER_TOPIC, message: MessageData)
}