package hu.friedcoyote.swai.di

import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.friedcoyote.swai.common.Constants
import hu.friedcoyote.swai.data.remote.OpenWeatherApi
import hu.friedcoyote.swai.data.repository.WeatherRepositoryImpl
import hu.friedcoyote.swai.domain.repository.WeatherRepository
import hu.friedcoyote.swai.domain.use_case.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideOpenWeatherApi(): OpenWeatherApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(sharedPreferences: SharedPreferences, api: OpenWeatherApi, geocoder: Geocoder): WeatherRepository {
        return WeatherRepositoryImpl(sharedPreferences, api, geocoder)
    }

    @Provides
    @Singleton
    fun provideGeocoder(@ApplicationContext appContext: Context): Geocoder {
        return Geocoder(appContext)
    }

    @Provides
    @Singleton
    fun provideWeatherUseCases(repository: WeatherRepository): WeatherUseCases {
        return WeatherUseCases(
            getWeatherByLocationUseCase = GetWeatherByLocationUseCase(repository),
            getWeatherByCityNameUseCase = GetWeatherByCityNameUseCase(repository),
            getCachedDayTypeUseCase = GetCachedDayTypeUseCase(repository),
            saveDayTypeUseCase = SaveDayTypeUseCase(repository)
        )
    }
}