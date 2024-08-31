package jp.ac.it_college.std.s23001.catapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    interface CatApiService {
        @GET("images/search")
        fun getCatImage(): Call<List<CatImage>>
    }

    data class CatImage(val url: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val catImageView = findViewById<ImageView>(R.id.catImageView)
        val loadCatButton = findViewById<Button>(R.id.loadCatButton)

        // Retrofitのセットアップ
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val catApiService = retrofit.create(CatApiService::class.java)

        // ボタンがクリックされたときに猫の画像をロード
        loadCatButton.setOnClickListener {
            catApiService.getCatImage().enqueue(object : retrofit2.Callback<List<CatImage>> {
                override fun onResponse(call: Call<List<CatImage>>, response: retrofit2.Response<List<CatImage>>) {
                    val catImage = response.body()?.get(0)?.url
                    if (catImage != null) {
                        Picasso.get().load(catImage).into(catImageView)
                    }
                }

                override fun onFailure(call: Call<List<CatImage>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }
}
