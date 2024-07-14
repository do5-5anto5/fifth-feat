package com.fifthfeat.domain.repository

interface FireBaseAuthentication {

    suspend fun login(email: String, password: String)

    suspend fun register(email: String, password: String)

    suspend fun forgot(email: String)
}