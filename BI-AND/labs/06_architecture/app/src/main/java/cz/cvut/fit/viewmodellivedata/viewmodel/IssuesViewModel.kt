package cz.cvut.fit.viewmodellivedata.viewmodel

import android.app.Application

import cz.cvut.fit.viewmodellivedata.networking.entity.IssueEntity

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import cz.cvut.fit.viewmodellivedata.networking.entity.Resource
import cz.cvut.fit.viewmodellivedata.repository.IssueRepository

class IssuesViewModel(application: Application) : AndroidViewModel(application) {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    val mObservableIssues = MediatorLiveData<Resource<List<IssueEntity>>>().apply { value = null }
    private val mRepository = IssueRepository()

    private lateinit var mOwner: String
    private lateinit var mRepo: String

    /**
     * Initialize view model and start loading particular repository.
     * @param owner Github repository owner
     * @param repo Github repository name
     */
    fun init(owner: String, repo: String) {
        mOwner = owner
        mRepo  = repo
        load(owner, repo)
    }

    private fun load(owner: String, repo: String) {
        val issues = mRepository.getIssueList(owner, repo)
        mObservableIssues.addSource(issues) { mObservableIssues.setValue(it) }
    }

    fun retryClicked() = load(mOwner, mRepo)
}
