package com.fifthfeat.domain.usecase.authentication

import com.fifthfeat.data.repository.FireBaseAuthenticationImpl
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val firebaseAuthentication : FireBaseAuthenticationImpl
) {

    suspend operator fun invoke(email: String, password: String) {
        firebaseAuthentication.register(email, password)
    }

}