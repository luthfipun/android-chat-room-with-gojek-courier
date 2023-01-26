package github.luthfipun.chatroom.presenter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.luthfipun.chatroom.R
import github.luthfipun.chatroom.domain.data.Message
import github.luthfipun.chatroom.domain.data.UserInfo
import github.luthfipun.chatroom.domain.model.MessageData
import github.luthfipun.chatroom.domain.model.toMessage
import github.luthfipun.chatroom.domain.util.MessageInfoType
import github.luthfipun.chatroom.domain.util.MessageStatus
import github.luthfipun.chatroom.domain.util.MessageType
import github.luthfipun.chatroom.domain.util.messageBodyType
import github.luthfipun.chatroom.repository.MainRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    init {
        connect()
        subscribe()
    }

    private val _localUser = MutableStateFlow<UserInfo?>(null)
    val localUser = _localUser.asStateFlow()

    private val _localMessage = MutableStateFlow<List<Message>>(emptyList())
    val localMessage = _localMessage.map { messageBodyType(it) }

    private val avatars = listOf(
        R.drawable.person1,
        R.drawable.person2,
        R.drawable.person3,
        R.drawable.person4,
        R.drawable.person5,
        R.drawable.person6,
        R.drawable.person7,
        R.drawable.person8,
    )

    fun setLocalUser(userInfo: UserInfo?){
        viewModelScope.launch {
            userInfo?.let {
                val localUser = it.copy(avatar = avatars[it.avatar])
                _localUser.emit(localUser)
            }
        }
    }

    private fun subscribe() {
        viewModelScope.launch {
            mainRepository.subscribe()
                .onEach { message ->
                    val updateMessage = message.copy(isOwner = message.user.id == localUser.value?.id)
                    if (message.type == MessageType.TEXT && message.user.id == localUser.value?.id){
                        _localMessage.update {
                            it.map { msg ->
                                if (msg.id == message.id){
                                    updateMessage
                                }else {
                                    msg
                                }
                            }
                        }
                    }else {
                        _localMessage.update { it.plus(updateMessage) }
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    fun sendMessage(context: Context, message: String){
        val messageData = MessageData(
            text = message,
            user = localUser.value!!,
            infoType = null
        )
        val currentMessage = messageData.toMessage(context = context)
            .copy(status = MessageStatus.WAITING, isOwner = messageData.user.id == localUser.value?.id)
        _localMessage.update { it.plus(currentMessage) }
        viewModelScope.launch {
            mainRepository.publish(message = messageData)
        }
    }

    fun joinOrLeaveRoom(messageInfoType: MessageInfoType){
        val messageData = MessageData(
            user = localUser.value!!,
            type = MessageType.INFO,
            infoType = messageInfoType
        )
        viewModelScope.launch {
            mainRepository.publish(message = messageData)
        }
    }

    fun clearState(){
        viewModelScope.launch {
            _localMessage.emit(emptyList())
            _localUser.emit(null)
        }
    }

    private fun connect(){
        viewModelScope.launch {
            mainRepository.connect()
        }
    }
}