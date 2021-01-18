package com.konkuk.boost.repositories

import com.konkuk.boost.data.library.LoginResponse
import com.konkuk.boost.data.library.QRResponse
import com.konkuk.boost.utils.UseCase

interface LibraryRepository {
    suspend fun makeLoginRequest(): UseCase<LoginResponse>

    suspend fun makeMobileQRCodeRequest(): UseCase<QRResponse>
}