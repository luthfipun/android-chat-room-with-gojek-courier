package github.luthfipun.chatroom.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.luthfipun.chatroom.R
import github.luthfipun.chatroom.domain.data.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _localUser = MutableStateFlow<UserInfo?>(null)
    val localUser = _localUser.asStateFlow()

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
}