package vn.example.workmanager.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

class HomeViewModel : ViewModel() {
    val users: Flow<PagingData<User>> = Pager(PagingConfig(20)) {
        UserPagingSource()
    }.flow.cachedIn(viewModelScope)
}
