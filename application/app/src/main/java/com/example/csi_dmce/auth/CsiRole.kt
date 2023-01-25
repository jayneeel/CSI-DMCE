package com.example.csi_dmce.auth

enum class CSIRole (val role: String) {
    USER("user"), VOLUNTEER("volunteer"), ADMIN("admin");

    fun isAdmin(): Boolean = this == ADMIN
}

fun tokenRoleToCsiRole(tokenRole: String): CSIRole {
    return when (tokenRole) {
        CSIRole.USER.role -> CSIRole.USER
        CSIRole.VOLUNTEER.role -> CSIRole.VOLUNTEER
        CSIRole.ADMIN.role -> CSIRole.ADMIN
        else -> CSIRole.USER
    }
}
