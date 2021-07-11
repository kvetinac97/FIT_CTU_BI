package cz.cvut.fit.viewmodellivedata.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GitHubApi {

    private const val BASE_URL = "https://api.github.com/"

    val service: GitHubService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubService::class.java)
    }

}
