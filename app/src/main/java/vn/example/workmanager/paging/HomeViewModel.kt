package vn.example.workmanager.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import io.reactivex.rxjava3.core.Flowable
import vn.example.workmanager.pagingrx.UserPagingRxSource

class HomeViewModel : ViewModel() {
    val users: Flowable<PagingData<User>> = Pager(PagingConfig(20)) {
        UserPagingRxSource()
    }.flowable.cachedIn(viewModelScope)
}
