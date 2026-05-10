package com.example.focusguard.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.focusguard.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FocusViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FocusRepository
    
    init {
        val db = AppDatabase.getDatabase(application)
        val preferencesManager = PreferencesManager(application)
        repository = FocusRepository(db.appDao(), preferencesManager)
    }

    val allBlockedApps = repository.allBlockedApps.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allRequests = repository.allRequests.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val blockedAppsCount = repository.blockedAppsCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val colorIndex = repository.colorIndex.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val adminPin = repository.adminPin.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val studyModeActive = repository.studyModeActive.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggleBlockApp(packageName: String, appName: String, isBlocked: Boolean) {
        viewModelScope.launch {
            if (isBlocked) {
                repository.insertBlockedApp(BlockedApp(packageName, appName))
            } else {
                repository.deleteBlockedApp(BlockedApp(packageName, appName))
            }
        }
    }

    fun updateRequestStatus(request: PermissionRequest, status: RequestStatus) {
        viewModelScope.launch {
            repository.updateRequest(request.copy(status = status))
        }
    }

    fun setColorIndex(index: Int) {
        viewModelScope.launch {
            repository.setColorIndex(index)
        }
    }

    fun setAdminPin(pin: String) {
        viewModelScope.launch {
            repository.setAdminPin(pin)
        }
    }

    fun setStudyMode(active: Boolean) {
        viewModelScope.launch {
            repository.setStudyMode(active)
        }
    }
}
