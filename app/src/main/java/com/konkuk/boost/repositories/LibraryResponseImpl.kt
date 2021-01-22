package com.konkuk.boost.repositories

import com.konkuk.boost.api.LibraryService
import com.konkuk.boost.data.library.LoginRequest
import com.konkuk.boost.data.library.LoginResponse
import com.konkuk.boost.data.library.QRResponse
import com.konkuk.boost.persistence.PreferenceManager
import com.konkuk.boost.utils.UseCase

class LibraryResponseImpl(
    private val libraryService: LibraryService,
    private val preferenceManager: PreferenceManager
) : LibraryRepository {
    override suspend fun makeLoginRequest(): UseCase<LoginResponse> {
        val username = preferenceManager.username
        val password = preferenceManager.password

        val loginResponse: LoginResponse
        try {
            loginResponse = libraryService.login(LoginRequest(username, password))
            val token = loginResponse.data.accessToken
            preferenceManager.accessToken = token
        } catch (e: Exception) {
            return UseCase.error("${e.message}")
        }

        return UseCase.success(loginResponse)
    }

    override suspend fun makeMobileQRCodeRequest(): UseCase<QRResponse> {
        val accessToken = preferenceManager.accessToken

        val qrResponse: QRResponse
        try {
            qrResponse = libraryService.getMobileQRCode(accessToken)
        } catch (e: Exception) {
            return UseCase.error("${e.message}")
        }

        return UseCase.success(qrResponse)
    }
}