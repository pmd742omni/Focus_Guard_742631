package com.example.focusguard.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.example.focusguard.data.AppDatabase
import com.example.focusguard.data.RequestStatus
import com.example.focusguard.ui.LockActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppBlockerService : AccessibilityService() {
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDatabase

    override fun onServiceConnected() {
        db = AppDatabase.getDatabase(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return
            
            if (packageName == this.packageName) return

            scope.launch {
                val blockedApps = db.appDao().getAllBlockedApps().first()
                if (blockedApps.any { it.packageName == packageName && it.isBlocked }) {
                    val requests = db.appDao().getAllRequests().first()
                    val hasPermission = requests.any { 
                        it.packageName == packageName && 
                        it.status == RequestStatus.APPROVED &&
                        (System.currentTimeMillis() - it.timestamp) < it.durationMinutes * 60 * 1000
                    }

                    if (!hasPermission) {
                        launchLockScreen(packageName)
                    }
                }
            }
        }
    }

    private fun launchLockScreen(packageName: String) {
        val intent = Intent(this, LockActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("PACKAGE_NAME", packageName)
        }
        startActivity(intent)
    }

    override fun onInterrupt() {}
}
