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
import pl.edu.pg.eti.domain.model.Tokens
import pl.edu.pg.eti.domain.repository.LoginRepository
import javax.inject.Inject

@HiltViewModel
class ForgotPassViewModel @Inject constructor(
    val repo: LoginRepository
) : ViewModel() {

    private val _resetPasswordLiveData: MutableLiveData<Resource<String>> = MutableLiveData()
    val resetPasswordLiveData: LiveData<Resource<String>> = _resetPasswordLiveData

    fun loginUser(email: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _resetPasswordLiveData.postValue(Resource.Loading())
            val response = repo.resetPassword(email)
            when (response) {
                is Resource.Success -> {
                    _resetPasswordLiveData.postValue(response)
                }
                is Resource.Error -> {
                    _resetPasswordLiveData.postValue(Resource.Error("Login failed"))
                }
            }
        }
    }

}