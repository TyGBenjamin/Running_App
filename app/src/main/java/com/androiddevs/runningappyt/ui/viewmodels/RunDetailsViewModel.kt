package com.androiddevs.runningappyt.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Run details view model.
 *
 * @property mainRepository
 * @constructor Create empty Run details view model
 */
@HiltViewModel
class RunDetailsViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val _run = MutableStateFlow<Run?>(null)
    val run get() = _run.asStateFlow()

    fun getRunById(id: Int) {
        viewModelScope.launch {
            _run.value = mainRepository.getRunById(id)
        }
    }

    fun deleteRun(run: Run) = viewModelScope.launch {
        mainRepository.deleteRun(run)
    }
}
