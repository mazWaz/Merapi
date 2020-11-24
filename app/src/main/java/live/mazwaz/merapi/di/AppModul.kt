package live.mazwaz.merapi.di

import live.mazwaz.merapi.data.provideApiService
import live.mazwaz.merapi.data.retrofit
import org.koin.dsl.module


val appModule = module {
    factory { retrofit() }
    single { provideApiService(get()) }
}