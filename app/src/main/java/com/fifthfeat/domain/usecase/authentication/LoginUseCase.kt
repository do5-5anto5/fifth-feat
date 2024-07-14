package com.fifthfeat.domain.usecase.authentication

import com.fifthfeat.domain.repository.FireBaseAuthentication
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val fireBaseAuthentication: FireBaseAuthentication
) {

    suspend operator fun invoke(email: String, password: String) =
        fireBaseAuthentication.login(email, password)

}