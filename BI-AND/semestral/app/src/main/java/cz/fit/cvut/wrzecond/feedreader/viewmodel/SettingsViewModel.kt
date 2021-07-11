package cz.fit.cvut.wrzecond.feedreader.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cz.fit.cvut.wrzecond.feedreader.App
import cz.fit.cvut.wrzecond.feedreader.room.entity.Feed
import kotlinx.coroutines.launch

class SettingsViewModel (app: Application) : AndroidViewModel(app) {

    private val repository = App.get(app).appRepository

    val feed = App.get(app).appRepository.feedList
    fun createFeed (url: String) = viewModelScope.launch { repository.insertFeed(Feed(url)) }
    fun deleteFeed (feedId: Long) = viewModelScope.launch { repository.deleteFeed(feedId) }

}
