package cz.cvut.fit.viewmodellivedata.networking

import cz.cvut.fit.viewmodellivedata.networking.entity.IssueEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {

    @GET("repos/{owner}/{repo}/issues")
    fun getIssues (@Path("owner") owner: String, @Path("repo") repo: String)
        : Call<List<IssueEntity>>

}