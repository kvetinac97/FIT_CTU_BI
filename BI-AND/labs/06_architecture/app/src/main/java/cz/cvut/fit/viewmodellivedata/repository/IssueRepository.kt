package cz.cvut.fit.viewmodellivedata.repository

import cz.cvut.fit.viewmodellivedata.networking.GitHubApi
import cz.cvut.fit.viewmodellivedata.networking.entity.IssueEntity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.viewmodellivedata.networking.entity.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IssueRepository {

    fun getIssueList(owner: String, repo: String): LiveData<Resource<List<IssueEntity>>> {

        val data = MutableLiveData<Resource<List<IssueEntity>>>()
            .apply { value = Resource(Resource.LOADING_STATUS, null, null) }

        GitHubApi.service.getIssues(owner, repo).enqueue(object: Callback<List<IssueEntity>> {
            override fun onResponse (call: Call<List<IssueEntity>>, response: Response<List<IssueEntity>>) {
                data.value = Resource(Resource.SUCCESS_STATUS, response.body(), null)
            }

            override fun onFailure (call: Call<List<IssueEntity>>, t: Throwable) {
                data.value = Resource(Resource.ERROR_STATUS, null, t.message)
            }
        })

        return data
    }

}