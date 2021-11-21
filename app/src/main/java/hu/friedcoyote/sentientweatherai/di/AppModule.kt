package hu.friedcoyote.sentientweatherai.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.friedcoyote.sentientweatherai.common.Constants
import hu.friedcoyote.sentientweatherai.data.remote.OpenWeatherApi
import hu.friedcoyote.sentientweatherai.data.repository.WeatherRepositoryImpl
import hu.friedcoyote.sentientweatherai.domain.repository.WeatherRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideWeatherRepository(api: OpenWeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }
}