package com.aspark.carebuddynurse.di

import com.aspark.carebuddynurse.api.Api
import com.aspark.carebuddynurse.repository.Repository
import com.aspark.carebuddynurse.retrofit.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    fun provideRepository(api: Api, connection: XMPPTCPConnection): Repository {
        return Repository(api, connection)
    }

    @Provides
    fun provideApi(): Api {

        return RetrofitService
            .retrofit.create(Api::class.java)
    }

    @Provides
    fun provideXMPPTCPConnectionConfiguration(): XMPPTCPConnectionConfiguration {

        return XMPPTCPConnectionConfiguration.builder()
            //.setUsernameAndPassword("user1", "user1")
            .setXmppDomain("aspark-care-buddy.ap-south-1.elasticbeanstalk.com")
            .setHost("192.168.1.6")
            .setConnectTimeout(5000)
            .setPort(5222)
            .setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible)
            .build()
    }

    @Singleton
    @Provides
    fun provideXMPPTCPConnection(
        configuration: XMPPTCPConnectionConfiguration
    ): XMPPTCPConnection {

        return XMPPTCPConnection(configuration)
    }

}