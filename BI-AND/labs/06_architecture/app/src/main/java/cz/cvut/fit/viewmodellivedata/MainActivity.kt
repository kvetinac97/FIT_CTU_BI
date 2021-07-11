package cz.cvut.fit.viewmodellivedata

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import cz.cvut.fit.viewmodellivedata.networking.entity.IssueEntity
import cz.cvut.fit.viewmodellivedata.networking.entity.Resource
import cz.cvut.fit.viewmodellivedata.view.IssueAdapter

import cz.cvut.fit.viewmodellivedata.databinding.ActivityMainBinding
import cz.cvut.fit.viewmodellivedata.viewmodel.IssuesViewModel
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mIssueAdapter: IssueAdapter
    private lateinit var mViewModel: IssuesViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mIssueAdapter = IssueAdapter { itemClicked(it) }
        mBinding.issueList.adapter = mIssueAdapter

        mViewModel = ViewModelProviders.of(this)
            .get(IssuesViewModel::class.java)
            .apply { init("square", "retrofit") }

        // Update the list when the data changes
        mViewModel.mObservableIssues.observe(this, { issuesResource ->
            if (issuesResource != null && issuesResource.status == Resource.SUCCESS_STATUS) {
                mIssueAdapter.setCarList(issuesResource.data!!)
            }

            mBinding.issueResource = issuesResource
            mBinding.executePendingBindings()
        })

        mBinding.viewmodel = mViewModel
    }

    private fun itemClicked (issue: IssueEntity)
        = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(issue.url)))

    companion object {
        fun start(gContext: Context) {
            val intent = Intent(gContext, MainActivity::class.java)
            gContext.startActivity(intent)
        }
    }

}
