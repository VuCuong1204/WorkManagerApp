package vn.example.workmanager.paging

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import vn.example.workmanager.R

class HomeActivity : AppCompatActivity() {

    private lateinit var rvHome: RecyclerView
    private val userAdapter by lazy { UserAdapter(UserComparator) }

    private val viewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        rvHome = findViewById(R.id.rvHome)

        rvHome.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
            adapter = userAdapter
        }

        lifecycleScope.launchWhenCreated {
//            viewModel.users.collectLatest {
//                userAdapter.submitData(it)
//            }
        }
    }
}
