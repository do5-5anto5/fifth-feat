package com.fifthfeat.domain.usecase.authentication

import com.fifthfeat.domain.repository.FireBaseAuthentication
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val firebaseAuthentication : FireBaseAuthentication
) {

    suspend operator fun invoke(email: String, password: String) {
        firebaseAuthentication.register(email, password)
    }

}