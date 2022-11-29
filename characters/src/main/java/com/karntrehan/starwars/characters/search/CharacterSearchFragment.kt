package com.karntrehan.starwars.characters.search

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karntrehan.starwars.architecture.BaseFragment
import com.karntrehan.starwars.characters.CharacterDH
import com.karntrehan.starwars.characters.R
import com.karntrehan.starwars.characters.search.models.CharacterSearchModel
import com.karntrehan.starwars.extensions.EndlessScrollListener
import com.karntrehan.starwars.extensions.gone
import com.karntrehan.starwars.extensions.visible
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.actionbar_toolbar.*
import kotlinx.android.synthetic.main.fragment_search_character.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CharacterSearchFragment : BaseFragment(), CharacterSearchAdapter.Interaction {

    override val layout = R.layout.fragment_search_character

    override val vmClass = CharacterSearchVM::class.java

    @Inject
    lateinit var characterSearchVMF: CharacterSearchVMF

    val viewModel: CharacterSearchVM by lazy { baseVM as CharacterSearchVM }

    private val adapter: CharacterSearchAdapter by lazy { CharacterSearchAdapter(this) }

    //Search
    private var searchView: SearchView? = null
    private val searchListener = PublishSubject.create<String>()

    //Pagination
    private lateinit var endlessScrollListener: EndlessScrollListener

    //Navigator
    private var navigator: CharacterNavigator? = null

    companion object {
        const val TAG = "CharacterSearchFragment"
        fun newInstance() = CharacterSearchFragment()
    }

    override fun provideViewModelFactory() = characterSearchVMF

    override fun onAttach(context: Context) {
        super.onAttach(context)
        CharacterDH.searchComponent.inject(this)
        if (context is CharacterNavigator)
            navigator = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar(toolbar)

        rvCharacters.setHasFixedSize(true)
        rvCharacters.adapter = adapter

        srlCharacters.setOnRefreshListener {
            refreshCharacters()
        }

        btnShowAllCharacters.setOnClickListener {
            refreshCharacters()
        }

        startListeningToPaginationLoadingState()

        startListeningToCharacters()

        initSearchBehaviour()

        viewModel.initialLoad()

        endlessScrollListener = initEndlessScroll()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val myActionMenuItem = menu.findItem(R.id.item_search)
        searchView = myActionMenuItem.actionView as SearchView
        searchView?.queryHint = getString(R.string.enter_character_name)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchListener.onNext(newText)
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        rvCharacters.addOnScrollListener(endlessScrollListener)
    }

    private fun refreshCharacters() {
        llNoData.gone()
        searchView?.setQuery("", false)
        searchView?.clearFocus()
        viewModel.refreshCharacters()
    }

    private fun startListeningToPaginationLoadingState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.paginationLoading.collect {
                    if (it) {
                        pbLoading.visible()
                    } else {
                        pbLoading.gone()
                    }
                }
            }
        }
    }

    private fun initSearchBehaviour() {
        //A listener to get user's query and manipulate it before going to vm
        searchListener
            //To ensure queries are run when the user pauses typing
            .debounce(300, TimeUnit.MILLISECONDS)
            .subscribe {
                //Reset the pagination state
                endlessScrollListener.resetState()
                viewModel.searchCharacter(it)
            }
        //.addTo(viewModel.disposable)
    }

    private fun startListeningToCharacters() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.characters.collect { characters ->
                    if (characters.isEmpty()) {
                        llNoData.visible()
                        rvCharacters.gone()
                    } else {
                        adapter.swapData(characters)
                        llNoData.gone()
                        rvCharacters.visible()
                    }
                }
            }
        }
    }

    private fun initEndlessScroll() = object : EndlessScrollListener(
        layoutManager = rvCharacters.layoutManager as LinearLayoutManager,
        visibleThreshold = 2
    ) {
        //This will be called each time the user scrolls
        // and only 2 elements are left in the recyclerview items.
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            viewModel.loadNextPage()
        }
    }

    override fun hideLoading() {
        srlCharacters.isRefreshing = false
    }

    override fun showLoading() {
        srlCharacters.isRefreshing = true
    }

    interface CharacterNavigator {
        fun showCharacterDetails(character: CharacterSearchModel)
    }

    //Adapter interactions
    override fun characterClicked(character: CharacterSearchModel) {
        searchView?.clearFocus()
        navigator?.showCharacterDetails(character)
    }

}
