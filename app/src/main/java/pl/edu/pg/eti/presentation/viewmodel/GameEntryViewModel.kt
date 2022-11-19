package pl.edu.pg.eti.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.domain.model.RoomJoin
import pl.edu.pg.eti.domain.repository.GameRepository
import javax.inject.Inject

@HiltViewModel
class GameEntryViewModel @Inject constructor(
    val repo: GameRepository
) : ViewModel() {
    private val _roomJoinLiveData: MutableLiveData<Resource<RoomJoin>> = MutableLiveData()
    val roomJoinLiveData: LiveData<Resource<RoomJoin>> = _roomJoinLiveData


    fun joinRoom(userId:Long, nickname:String, hash:String?=null) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _roomJoinLiveData.postValue(Resource.Loading())
                val result = repo.joinRoom(userId,nickname, hash)
                _roomJoinLiveData.postValue(result)
            }
        }
    }

    fun clearLiveData(){
        _roomJoinLiveData.postValue(Resource.Loading())
    }


}