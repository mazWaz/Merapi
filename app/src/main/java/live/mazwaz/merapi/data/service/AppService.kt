package live.mazwaz.merapi.data.service

import live.mazwaz.merapi.model.VolcanoResponses
import live.mazwaz.merapi.model.VolcanosResponses
import retrofit2.http.GET
import retrofit2.http.Query

interface AppService {
    @GET("var.php")
    suspend fun getVolcanos(): VolcanosResponses

    @GET("get_var.php")
    suspend fun getVolcano(
        @Query("no") no: String
    ): VolcanoResponses


}