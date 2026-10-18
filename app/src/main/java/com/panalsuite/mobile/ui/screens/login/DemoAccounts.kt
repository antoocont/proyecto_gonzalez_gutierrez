package com.panalsuite.mobile.ui.screens.login

import com.panalsuite.mobile.domain.model.Role

data class DemoAccount(val role: Role, val email: String)

object DemoAccounts {
    const val PASSWORD = "Demo1234"

    val all = listOf(
        DemoAccount(Role.REQUESTER, "camila.rojas@panalsuite.test"),
        DemoAccount(Role.RESOLVER, "diego.fuentes@panalsuite.test"),
        DemoAccount(Role.SUPERVISOR, "matias.pizarro@panalsuite.test")
    )
}