package com.fifthfeat.dependency_injection

import com.fifthfeat.data.repository.FireBaseAuthenticationImpl
import com.fifthfeat.domain.repository.FireBaseAuthentication
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindsFireBaseAuthentication(
        fireBaseAuthenticationImpl: FireBaseAuthenticationImpl
    ) : FireBaseAuthentication

}