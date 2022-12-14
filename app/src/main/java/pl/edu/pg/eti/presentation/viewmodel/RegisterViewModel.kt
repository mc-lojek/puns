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
import pl.edu.pg.eti.domain.repository.RegisterRepository
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor( private val repository: RegisterRepository): ViewModel() {
    private val _registerLiveData: MutableLiveData<Resource<Void>> = MutableLiveData()
    val registerLiveData: LiveData<Resource<Void>> = _registerLiveData

    fun registerUser(nick: String, pass: String, mail: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val registered = repository.registerUser(nick, pass, mail)
                when(registered){
                    is Resource.Success -> {
                        _registerLiveData.postValue(registered)
                    }
                    is Resource.Error ->{
                        _registerLiveData.postValue(Resource.Error("Register failed"))
                    }
                }
            }
        }
    }
}
