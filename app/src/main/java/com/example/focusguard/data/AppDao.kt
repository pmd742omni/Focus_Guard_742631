package com.example.focusguard.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM blocked_apps")
    fun getAllBlockedApps(): Flow<List<BlockedApp>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockedApp(app: BlockedApp)

    @Delete
    suspend fun deleteBlockedApp(app: BlockedApp)

    @Query("SELECT * FROM permission_requests ORDER BY timestamp DESC")
    fun getAllRequests(): Flow<List<PermissionRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: PermissionRequest)

    @Update
    suspend fun updateRequest(request: PermissionRequest)
    
    @Query("SELECT COUNT(*) FROM blocked_apps WHERE isBlocked = 1")
    fun getBlockedAppsCount(): Flow<Int>
}
