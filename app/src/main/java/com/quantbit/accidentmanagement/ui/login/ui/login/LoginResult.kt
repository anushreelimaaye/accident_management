package com.quantbit.accidentmanagement.ui.login.ui.login

import com.quantbit.accidentmanagement.ui.login.data.model.LoggedInUser

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUser? = null,
    val error: Int? = null
)