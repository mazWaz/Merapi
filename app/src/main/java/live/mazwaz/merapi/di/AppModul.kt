package live.mazwaz.merapi.di

import live.mazwaz.merapi.data.provideApiService
import live.mazwaz.merapi.data.provideApiService2
import live.mazwaz.merapi.data.retrofit
import live.mazwaz.merapi.data.retrofit2
import org.koin.core.qualifier.named
import org.koin.dsl.module


val appModule = module {
    factory(named("BPPD")) { retrofit() }
    factory(named("server")) { retrofit2() }
    single(named("BPPD")) { provideApiService(get(named("BPPD"))) }
    single(named("retrofit2")) { provideApiService2(get(named("BPPD"))) }
}