package com.lion.wandertrip

import com.lion.wandertrip.repository.ContentsRepository
import com.lion.wandertrip.repository.ContentsReviewRepository
import com.lion.wandertrip.repository.TripCommonItemRepository
import com.lion.wandertrip.repository.TripScheduleRepository
import com.lion.wandertrip.repository.UserRepository
import com.lion.wandertrip.retrofit_for_practice.TripCommonItemInterface
import com.lion.wandertrip.service.ContentsReviewService
import com.lion.wandertrip.service.ContentsService
import com.lion.wandertrip.service.TripCommonItemService
import com.lion.wandertrip.service.TripScheduleService
import com.lion.wandertrip.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// 모듈은 Hilt에게 객체를 어떻게 제공할지에 대한 규칙을 정의하며, 주로 @Provides 또는 @Binds 어노테이션을 사용하여 의존성을 생성합니다.
// @InstallIn(SingletonComponent::class)는 모듈이 SingletonComponent에 설치되어 해당 컴포넌트의 라이프사이클 동안 유효한 의존성으로 제공됨을 의미합니다.
@Module
@InstallIn(SingletonComponent ::class)
object TripAppModule {
    val BASE_URL = "http://apis.data.go.kr/B551011/KorService1/"


    @Provides
    @Singleton
    fun userRepositoryProvider() : UserRepository {
        return UserRepository()
    }

    @Provides
    @Singleton
    fun userServiceProvider(userRepository: UserRepository) : UserService {
        return UserService(userRepository)
    }

    // TripSchedule ////////////////////////////////////////////////////////////////////////////////
    @Provides
    @Singleton
    fun tripScheduleRepositoryProvider(): TripScheduleRepository {
        return TripScheduleRepository()
    }

    @Provides
    @Singleton
    fun tripScheduleServiceProvider(
        tripScheduleRepository: TripScheduleRepository
    ): TripScheduleService {
        return TripScheduleService(tripScheduleRepository)
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // TripCommonItem
    @Provides
    @Singleton
    fun retrofitProvider(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // Gson 사용
            .build()
    }

    @Provides
    @Singleton
    fun tripItemCommonInterfaceProvider(retrofit: Retrofit): TripCommonItemInterface {
        return retrofit.create(TripCommonItemInterface::class.java)
    }

    @Provides
    @Singleton
    fun tripCommonItemRepositoryProvider(api: TripCommonItemInterface): TripCommonItemRepository {
        return TripCommonItemRepository(api)
    }

    @Provides
    @Singleton
    fun tripCommonItemServiceProvider(repository: TripCommonItemRepository): TripCommonItemService {
        return TripCommonItemService(repository)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // contents

    @Provides
    @Singleton
    fun contentsRepositoryProvider(): ContentsRepository {
        return ContentsRepository()
    }

    @Provides
    @Singleton
    fun contentsServiceProvider(
        contentsRepository: ContentsRepository
    ): ContentsService {
        return ContentsService(contentsRepository)
    }

    //////////////////////////////////////////////////////////////////////////////////////


    // ContentsReview

    @Provides
    @Singleton
    fun contentsReviewRepositoryProvider(): ContentsReviewRepository {
        return ContentsReviewRepository()
    }

    @Provides
    @Singleton
    fun contentsReviewServiceProvider(
        contentsReviewRepository: ContentsReviewRepository
    ): ContentsReviewService {
        return ContentsReviewService(contentsReviewRepository)
    }





}