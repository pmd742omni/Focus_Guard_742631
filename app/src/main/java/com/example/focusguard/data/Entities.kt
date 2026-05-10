package com.example.focusguard.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocked_apps")
data class BlockedApp(
    @PrimaryKey val packageName: String,
    val appName: String,
    val isBlocked: Boolean = true,
    val category: String = "Other"
)

@Entity(tableName = "permission_requests")
data class PermissionRequest(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packageName: String,
    val appName: String,
    val durationMinutes: Int,
    val status: RequestStatus = RequestStatus.PENDING,
    val timestamp: Long = System.currentTimeMillis()
)

enum class RequestStatus {
    PENDING, APPROVED, DENIED
}
