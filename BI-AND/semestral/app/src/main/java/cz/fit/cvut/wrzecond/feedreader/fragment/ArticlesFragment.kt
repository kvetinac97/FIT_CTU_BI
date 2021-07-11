package cz.fit.cvut.wrzecond.feedreader.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.fit.cvut.wrzecond.feedreader.activity.ArticleDetailActivity
import cz.fit.cvut.wrzecond.feedreader.R
import cz.fit.cvut.wrzecond.feedreader.adapter.ArticleAdapter
import cz.fit.cvut.wrzecond.feedreader.room.AppRepository
import cz.fit.cvut.wrzecond.feedreader.viewmodel.ArticlesViewModel

class ArticlesFragment : Fragment() {

    private lateinit var articlesViewModel: ArticlesViewModel
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articlesViewModel = ViewModelProvider(this).get(ArticlesViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View
        = inflater.inflate(R.layout.fragment_articles, container, false)

    override fun onViewCreated (view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.container)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        articleAdapter = ArticleAdapter(LayoutInflater.from(activity)) {
            val bundle = Bundle()
            bundle.putLong(ArticleDetailFragment.ARG_ARTICLE_ID, it.id ?: 0)

            // === 3. kontrolni bod
            val detailFragment = requireActivity().findViewById<FrameLayout>(R.id.article_detail_fragment)
            if (detailFragment != null) {
                val fragment = FragmentFactory.loadFragmentClass(requireActivity().classLoader, ArticleDetailFragment::class.java.name)
                    .getConstructor().newInstance()
                fragment.arguments = bundle

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.article_detail_fragment, fragment)
                transaction.commit()
                return@ArticleAdapter
            }
            // === 3. kontrolni bod

            val intent = Intent(activity, ArticleDetailActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        recyclerView.adapter = articleAdapter

        articlesViewModel.articles.observe(viewLifecycleOwner) {
            articleAdapter.articleList = it
            view.findViewById<TextView>(R.id.container_empty).text = if (it.isEmpty()) getString(R.string.article_list_empty) else ""
        }

        articlesViewModel.downloadPhase.observe(viewLifecycleOwner) { phase ->
            when (phase) {
                AppRepository.PHASE.IDLE -> {} // nothing (3. kontrolni bod)
                else -> updateRefreshItem(phase)
            }
        }
    }

    private var refreshItem: MenuItem? = null
    private fun updateRefreshItem (phase: AppRepository.PHASE? = articlesViewModel.downloadPhase.value) {
        if (phase == AppRepository.PHASE.DOWNLOADING) refreshItem?.setActionView(R.layout.view_progress_bar)
        else refreshItem?.actionView = null
    }

    override fun onCreateOptionsMenu (menu: Menu, inflater: MenuInflater) {
        refreshItem = menu.findItem(R.id.action_refresh)
        updateRefreshItem()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected (item: MenuItem) = when(item.itemId) {
        R.id.action_refresh -> {
            Log.d("FeedReader", "Performing refresh")
            articlesViewModel.startDownload() // (3. kontrolni bod)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}