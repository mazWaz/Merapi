package live.mazwaz.merapi.model

import com.google.gson.annotations.SerializedName

data class VolcanosResponses(
    @SerializedName("var") val res : List<Volcanos>
)

data class Volcanos(
    val no : String,
    val ga_nama_gapi : String,
    val area : String,
    val cu_status : String,
    val var_data_date : String,
    val var_rekom : String,
    val ga_code : String,
    val ga_lat_gapi : String,
    val ga_lon_gapi : String
)

data class VolcanoResponses(
    val laporan : List<Volnaco>
)

data class Volnaco(
    val var_image: String,
    val periode: String,
    val var_data_date: String,
    val ga_nama_gapi: String,
    val cu_status: String,
    val var_source: String,
    val var_visibility: String,
    val var_cuaca: String,
    val var_curah_hujan: String,
    val var_suhumin: String,
    val var_suhumax: String,
    val var_kelembabanmin: String,
    val var_kelembabanmax: String,
    val var_tekananmin: String,
    val var_tekananmax: String,
    val var_kecangin: String,
    val var_arangin: String,
    val var_asap: String,
    val var_tasap_min: String,
    val var_tasap: String,
    val var_wasap: String,
    val var_intasap: String,
    val var_tekasap: String,
    val kesimpulan_id: String,
    val var_rekom: String,
    val ga_lon_gapi: String,
    val ga_lat_gapi: String
)

