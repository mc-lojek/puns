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
import pl.edu.pg.eti.domain.model.Guest
import pl.edu.pg.eti.domain.repository.LoginRepository
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {
    private val _guestLiveData: MutableLiveData<Resource<Guest>> = MutableLiveData()
    val guestLiveData: LiveData<Resource<Guest>> = _guestLiveData

    fun loginGuest() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _guestLiveData.postValue(Resource.Loading())
                val response = repository.loginGuest()
                when (response) {
                    is Resource.Success -> {
                        _guestLiveData.postValue(response)
                    }
                    is Resource.Error -> {
                        _guestLiveData.postValue(Resource.Error("Login as guest failed"))
                    }
                }
            }
        }
    }
}
