package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAPI()

    }

    private fun requestAPI() {

        CoroutineScope(IO).launch {

            var date = async { fetchData() }  .await()

            if(date.isNotEmpty()){

                updateUI(date)
            }
    }
}

    private suspend fun  updateUI(result: String) {

        withContext(Main){

            var jsonArray = JSONObject(result)

            var city = jsonArray.getString("name")
            address.text = city
            var jsonTemp = jsonArray.getJSONObject("main").getString("temp").toDouble().toInt()

            temp.text = "${jsonTemp - 273}°C"

            var jsonTempMax = jsonArray.getJSONObject("main").getString("temp_max").toDouble().toInt()

            temp_max.text = "High: ${jsonTempMax - 273}°C"

            var jsonTempmin = jsonArray.getJSONObject("main").getString("temp_min").toDouble().toInt()

            temp_min.text = "Low: ${jsonTempmin - 273}°C"
            currentDate()

            var jsonStatus  = jsonArray.getJSONArray("weather").getJSONObject(0).getString("main")
            status.text = jsonStatus

            var jsonSunrise = jsonArray.getJSONObject("sys").getLong("sunrise")
               sunrise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(jsonSunrise*1000))


            var jsonSunset = jsonArray.getJSONObject("sys").getLong("sunset")
            sunset.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(jsonSunset*1000))

            val jsonWindSpeed = jsonArray.getJSONObject("wind").getString("speed")
            wind.text = jsonWindSpeed

            val jsonPressure = jsonArray.getJSONObject("main").getString("pressure")
            pressure.text = jsonPressure

            val jsonHumidity = jsonArray.getJSONObject("main").getString("humidity")
            humidity.text = jsonHumidity


        }

    }

    private fun currentDate() {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        updated_at.text = currentDate    }

    private fun fetchData() : String{

        var resopnse = ""

        try{
                resopnse = URL("https://api.openweathermap.org/data/2.5/weather?zip=98101,&appid=8b84a88936d0fcb97094aa603882ebc5").readText()
        }catch (e : Exception){
            Log.d("MAIN","Isuuse $e")
        }
        return resopnse
    }



    }
