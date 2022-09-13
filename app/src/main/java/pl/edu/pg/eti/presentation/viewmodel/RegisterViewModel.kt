package pl.edu.pg.eti.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import pl.edu.pg.eti.domain.model.User
import pl.edu.pg.eti.domain.repository.RegisterRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor( private val repository: RegisterRepository): ViewModel() {
    private val _registerLiveData: MutableLiveData<Response<Void>> =
        MutableLiveData()
    val registerLiveData: LiveData<Response<Void>> = _registerLiveData

    fun registerUser(nick: String, pass: String, mail: String) {
        viewModelScope.launch {
            (Dispatchers.IO){
                val registered = repository.registerUser(nick, pass, mail)
                _registerLiveData.postValue(registered)
            }
        }
    }
}
