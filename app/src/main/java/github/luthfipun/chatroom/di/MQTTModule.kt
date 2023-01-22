package github.luthfipun.chatroom.di

import android.content.Context
import com.gojek.courier.Courier
import com.gojek.courier.messageadapter.gson.GsonMessageAdapterFactory
import com.gojek.courier.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import com.gojek.mqtt.auth.Authenticator
import com.gojek.mqtt.client.MqttClient
import com.gojek.mqtt.client.config.v3.MqttV3Configuration
import com.gojek.mqtt.client.factory.MqttClientFactory
import com.gojek.mqtt.model.KeepAlive
import com.gojek.mqtt.model.MqttConnectOptions
import com.gojek.mqtt.model.ServerUri
import com.gojek.workmanager.pingsender.WorkManagerPingSenderConfig
import com.gojek.workmanager.pingsender.WorkPingSenderFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import github.luthfipun.chatroom.domain.util.Constant.BROKER_HOST
import github.luthfipun.chatroom.domain.util.Constant.BROKER_PASSWORD
import github.luthfipun.chatroom.domain.util.Constant.BROKER_PORT
import github.luthfipun.chatroom.domain.util.Constant.BROKER_USERNAME
import github.luthfipun.chatroom.service.MQTTService
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MQTTModule {
    @Singleton
    @Provides
    fun provideMQTT(@ApplicationContext context: Context): MqttClient{
        val config = MqttV3Configuration(
            authenticator = object : Authenticator {
                override fun authenticate(
                    connectOptions: MqttConnectOptions,
                    forceRefresh: Boolean
                ): MqttConnectOptions {
                    return connectOptions.newBuilder().build()
                }
            },
            pingSender = WorkPingSenderFactory.createMqttPingSender(context, WorkManagerPingSenderConfig())
        )
        return MqttClientFactory.create(context, config)
    }

    @Singleton
    @Provides
    fun provideMQTTService(mqttClient: MqttClient): MQTTService{
        val configuration = Courier.Configuration(
            client = mqttClient,
            streamAdapterFactories = listOf(CoroutinesStreamAdapterFactory()),
            messageAdapterFactories = listOf(GsonMessageAdapterFactory())
        )
        return Courier(configuration).create()
    }

    @Singleton
    @Provides
    fun provideMQTTConnection(): MqttConnectOptions{
        return MqttConnectOptions.Builder()
            .serverUris(listOf(
                ServerUri(
                host = BROKER_HOST,
                port = BROKER_PORT,
                scheme = "ssl"
            )
            ))
            .clientId(UUID.randomUUID().toString())
            .userName(BROKER_USERNAME)
            .password(BROKER_PASSWORD)
            .cleanSession(false)
            .keepAlive(KeepAlive(timeSeconds = 60))
            .build()
    }
}