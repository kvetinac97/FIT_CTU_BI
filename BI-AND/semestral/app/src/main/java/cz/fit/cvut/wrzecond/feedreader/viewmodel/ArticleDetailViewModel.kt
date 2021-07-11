package cz.fit.cvut.wrzecond.feedreader.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import cz.fit.cvut.wrzecond.feedreader.App
import cz.fit.cvut.wrzecond.feedreader.room.entity.Article

class ArticleDetailViewModel (val app: Application) : AndroidViewModel(app) {

    private val repository = App.get(app).appRepository
    lateinit var article: LiveData<Article>
    private set

    fun setArticle (id: Long) {
        article = repository.getArticleByID(id)
    }

}