package vn.example.workmanager.paging

import android.util.Log

data class User(val id: Int, val name: String)

fun mockDataUser(page: Int, size: Int): List<User> {
    val list = mutableListOf<User>()
    repeat(100) {
        list.add(User(it, "User $it"))
    }

    val newList = list.subList(page * size, (page + 1) * size)

    Log.d("mockDataUser", "mockDataUser: ${page} $size ${newList.size} ${list.size}")

    return newList
}
