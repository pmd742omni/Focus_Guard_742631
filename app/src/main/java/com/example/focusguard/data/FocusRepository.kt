package com.example.focusguard.data

import kotlinx.coroutines.flow.Flow

class FocusRepository(
    private val appDao: AppDao,
    private val preferencesManager: PreferencesManager
) {
    val allBlockedApps = appDao.getAllBlockedApps()
    val allRequests = appDao.getAllRequests()
    val blockedAppsCount = appDao.getBlockedAppsCount()
    
    val colorIndex = preferencesManager.colorIndex
    val adminPin = preferencesManager.adminPin
    val studyModeActive = preferencesManager.studyModeActive

    suspend fun insertBlockedApp(app: BlockedApp) = appDao.insertBlockedApp(app)
    suspend fun deleteBlockedApp(app: BlockedApp) = appDao.deleteBlockedApp(app)
    
    suspend fun insertRequest(request: PermissionRequest) = appDao.insertRequest(request)
    suspend fun updateRequest(request: PermissionRequest) = appDao.updateRequest(request)
    
    suspend fun setColorIndex(index: Int) = preferencesManager.setColorIndex(index)
    suspend fun setAdminPin(pin: String) = preferencesManager.setAdminPin(pin)
    suspend fun setStudyMode(active: Boolean) = preferencesManager.setStudyMode(active)
}
