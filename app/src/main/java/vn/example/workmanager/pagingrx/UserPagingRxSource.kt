package vn.example.workmanager.pagingrx

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.core.Single
import vn.example.workmanager.paging.User

class UserPagingRxSource : RxPagingSource<Int, User>() {
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, User>> {
        TODO("Not yet implemented")
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return 1
    }
}
