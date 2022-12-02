package pl.edu.pg.eti.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.domain.model.Tokens
import pl.edu.pg.eti.domain.model.User
import pl.edu.pg.eti.domain.repository.LoginRepository
import pl.edu.pg.eti.domain.repository.RegisterRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor( private val repository: LoginRepository): ViewModel() {
    private val _loginLiveData: MutableLiveData<Resource<Tokens>> = MutableLiveData()
    val loginLiveData: LiveData<Resource<Tokens>> = _loginLiveData

    fun loginUser(nick: String, pass: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val response = repository.loginUser(nick, pass)
                when (response){
                    is Resource.Success -> {
                        _loginLiveData.postValue(response)
                    }
                    is Resource.Error -> {
                        _loginLiveData.postValue(Resource.Error("Login failed"))
                    }
                }
            }
        }
    }
}
