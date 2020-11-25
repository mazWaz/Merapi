package live.mazwaz.merapi.utils

import com.orhanobut.hawk.Hawk
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class Constants {
    companion object {
        const val NAME_ALIAS = "DASHBOARD"
        const val TOKEN = "live.mazwazstorage.token"
        const val API_URL = "live.mazwazstorage.api_url"
        const val URL_BLG = "https://merapi.bgl.esdm.go.id/"

        val BASE_URL: String = Hawk.get<String>(API_URL) + "/api/"

        fun onFailStatus(fail: String): String {
            return if (fail.contains("Temporary Redirect")) {
                "Silahkan Hubungi IT \nError: Site Redirect $fail"
            } else if (fail.contains("Unable to resolve host")) {
                "Silahkan Cek Internet Anda"
            } else if (fail.contains("timeout")) {
                "Silahkan Hubungi IT \nError: Can't reach Api $fail"
            } else if (fail.contains("Defect not found!")) {
                "Silahkan Hubungi IT \nError: $fail"
            } else if (fail.contains("400")) {
                "Input Salah, Cek Input"
            } else if (fail.contains("null")) {
                "Data Tidak Ditemukan"
            } else {
                "Terjadi Error \nError: $fail"
            }
        }
        fun DistanceTo(lat1: Double, lon1: Double, lat2: Double, lon2: Double, unit: String): Double {
            val rlat1 = Math.PI * lat1 / 180.0f
            val rlat2 = Math.PI * lat2 / 180.0f
            val rlon1 = Math.PI * lon1 / 180.0f
            val rlon2 = Math.PI * lon2 / 180.0f
            val theta = lon1 - lon2
            val rtheta = Math.PI * theta / 180.0f
            var dist = sin(rlat1) * sin(rlat2) + cos(rlat1) * cos(rlat2) * cos(rtheta)
            dist = acos(dist)
            dist = dist * 180.0f / Math.PI
            dist *= 60.0f * 1.1515f
            if (unit === "K") {
                dist *= 1.609344f
            }
            if (unit === "M") {
                dist *= 1.609344 * 1000f
            }
            if (unit === "N") {
                dist *= 0.8684f
            }
            return dist
        }

        fun String.toMD5(): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
        }
    }
}