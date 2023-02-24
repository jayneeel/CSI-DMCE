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

enum class EmailKind(val kind: String) {
    EMAIL_VERIFICATION("email_verification"),
    RESET_PASSWORD_VERIFICATION("password_reset_verification");

    companion object {
        // kind assumes two values:
        // "email_verification" and "reset_password_verification"
        fun fromKind(kind: String): EmailKind {
            return valueOf(kind)
        }
    }
}