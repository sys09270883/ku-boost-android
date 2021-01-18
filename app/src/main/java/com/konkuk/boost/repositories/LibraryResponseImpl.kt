package com.konkuk.boost.repositories

import com.konkuk.boost.api.LibraryService
import com.konkuk.boost.data.library.LoginRequest
import com.konkuk.boost.data.library.LoginResponse
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
        } catch (e: Exception) {
            return UseCase.error("${e.message}")
        }

        return UseCase.success(loginResponse)
    }
}