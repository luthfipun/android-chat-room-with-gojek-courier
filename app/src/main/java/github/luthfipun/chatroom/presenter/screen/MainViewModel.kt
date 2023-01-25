package github.luthfipun.chatroom.presenter.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.luthfipun.chatroom.R
import github.luthfipun.chatroom.domain.data.Message
import github.luthfipun.chatroom.domain.data.UserInfo
import github.luthfipun.chatroom.domain.model.MessageData
import github.luthfipun.chatroom.domain.util.MessageInfoType
import github.luthfipun.chatroom.domain.util.MessageType
import github.luthfipun.chatroom.domain.util.messageBodyType
import github.luthfipun.chatroom.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    val localMessage = _localMessage.asStateFlow()

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

    fun subscribe() {
        viewModelScope.launch {
            mainRepository.subscribe()
                .onEach { message ->
                    val updateMessage = message.copy(isOwner = message.user.id == localUser.value?.id)
                    val updateMessages = messageBodyType(localMessage.value.plus(updateMessage))
                    _localMessage.emit(updateMessages)
                }
                .launchIn(viewModelScope)
        }
    }

    fun sendMessage(message: String){
        val messageData = MessageData(
            text = message,
            user = localUser.value!!,
            infoType = null
        )
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