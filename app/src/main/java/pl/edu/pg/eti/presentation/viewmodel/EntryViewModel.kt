package pl.edu.pg.eti.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import pl.edu.pg.eti.domain.model.GuestData
import pl.edu.pg.eti.domain.repository.LoginRepository
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor(private val repository: LoginRepository): ViewModel() {
    private val _guestLiveData: MutableLiveData<Response<GuestData>> =
        MutableLiveData()
    val guestLiveData: LiveData<Response<GuestData>> = _guestLiveData

    fun loginGuest() {
        viewModelScope.launch {
            (Dispatchers.IO){
                val response = repository.loginGuest()
                _guestLiveData.postValue(response)
            }
        }
    }
}
