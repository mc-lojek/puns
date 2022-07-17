package pl.edu.pg.eti.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.edu.pg.eti.domain.repository.LoginRepository
import javax.inject.Inject

@HiltViewModel
class ForgotPassViewModel @Inject constructor(
    val repo: LoginRepository
): ViewModel() {

    // no i tutaj w sumie trzymamy istotne rzeczy
    // polecam livedata, flow, coroutines sobie obczaic

}