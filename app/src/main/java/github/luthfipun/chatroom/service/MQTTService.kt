package github.luthfipun.chatroom.service

import com.gojek.courier.QoS
import com.gojek.courier.annotation.*
import github.luthfipun.chatroom.domain.model.MessageData
import kotlinx.coroutines.flow.Flow

interface MQTTService {
    @Send(topic = "{topic}", qos = QoS.ONE)
    fun publish(@Path("topic") topic: String, @Data message: MessageData)

    @Subscribe(topic = "{topic}")
    fun subscribe(@Path("topic") topic: String): Flow<MessageData>

    @Unsubscribe(topics = ["{topic}"])
    fun unsubscribe(@Path("topic") topic: String)
}