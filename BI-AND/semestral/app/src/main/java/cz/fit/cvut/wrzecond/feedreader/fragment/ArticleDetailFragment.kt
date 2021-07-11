package cz.fit.cvut.wrzecond.feedreader.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.fit.cvut.wrzecond.feedreader.R
import cz.fit.cvut.wrzecond.feedreader.databinding.FragmentArticleDetailBinding
import cz.fit.cvut.wrzecond.feedreader.viewmodel.ArticleDetailViewModel
import java.text.SimpleDateFormat
import java.util.*

class ArticleDetailFragment : Fragment() {

    private lateinit var articleDetailViewModel: ArticleDetailViewModel
    private lateinit var binding: FragmentArticleDetailBinding

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val id = arguments?.getLong(ARG_ARTICLE_ID)!!
        articleDetailViewModel = ViewModelProvider(this).get(ArticleDetailViewModel::class.java)
        articleDetailViewModel.setArticle(id)
    }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        binding = FragmentArticleDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated (view: View, savedInstanceState: Bundle?) {
        articleDetailViewModel.article.observe(viewLifecycleOwner) { article ->
            binding.articleTitle.text = article.title
            binding.articleDescription.text = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(article.date)
            binding.articleLink.text = getString(R.string.action_full_article)
            binding.articleLink.setOnClickListener {
                val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                startActivity(urlIntent)
            }
            @Suppress("deprecation")
            binding.articleText.text = Html.fromHtml(article.text)
            binding.articleDetailLoading.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu (menu: Menu, inflater: MenuInflater)
        = inflater.inflate(R.menu.menu_detail, menu)

    override fun onPrepareOptionsMenu (menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_share).isVisible = isVisible
    }

    override fun onOptionsItemSelected (item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.article_share))
                    .putExtra(Intent.EXTRA_TITLE  , articleDetailViewModel.article.value?.title )
                startActivity(Intent.createChooser(shareIntent, getString(R.string.action_share)))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val ARG_ARTICLE_ID = "ARTICLE_ID"

        fun newInstance (articleId: Long) : ArticleDetailFragment {
            val bundle = Bundle()
            bundle.putLong(ARG_ARTICLE_ID, articleId)

            val fragment = ArticleDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}