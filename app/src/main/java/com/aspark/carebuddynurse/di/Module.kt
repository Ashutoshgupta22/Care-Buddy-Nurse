package com.aspark.carebuddynurse.di

import com.aspark.carebuddynurse.api.Api
import com.aspark.carebuddynurse.repository.Repository
import com.aspark.carebuddynurse.retrofit.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    fun provideRepository(api: Api): Repository {
        return Repository(api)
    }

    @Provides
    fun provideApi(): Api {

        return RetrofitService
            .retrofit.create(Api::class.java)
    }

}