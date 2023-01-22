package github.luthfipun.chatroom.repository

import android.content.Context
import com.gojek.mqtt.client.MqttClient
import com.gojek.mqtt.model.MqttConnectOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import github.luthfipun.chatroom.domain.data.Message
import github.luthfipun.chatroom.domain.model.MessageData
import github.luthfipun.chatroom.domain.model.toMessage
import github.luthfipun.chatroom.service.MQTTService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val mqttService: MQTTService,
    @ApplicationContext private val context: Context,
    private val mqttClient: MqttClient,
    private val mqttConnectOptions: MqttConnectOptions
) : MainRepository {

    override suspend fun connect() {
        mqttClient.connect(mqttConnectOptions)
    }

    override suspend fun disconnect() {
        mqttClient.disconnect()
    }

    override suspend fun subscribe(topic: String): Flow<Message> = mqttService.subscribe(topic = topic).map { it.toMessage(context) }

    override suspend fun unsubscribe(topic: String) {
        mqttService.unsubscribe(topic = topic)
    }

    override suspend fun publish(topic: String, message: MessageData) {
        mqttService.publish(topic = topic, message = message)
    }
}