package com.karntrehan.starwars.extensions

abstract class EndlessScrollListener : androidx.recyclerview.widget.RecyclerView.OnScrollListener {
  // The minimum amount of items to have below your current scroll position
  // before loading more.
  private val visibleThreshold: Int
  // The current offset index of data you have loaded
  private var currentPage = 0
  // The total number of items in the dataset after the last load
  private var previousTotalItemCount = 0
  // True if we are still waiting for the last set of data to load.
  private var loading = true
  // Sets the starting page index
  private val startingPageIndex = 0

  private var mLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager

  constructor(
    layoutManager: androidx.recyclerview.widget.LinearLayoutManager,
    visibleThreshold: Int = 5
  ) {
    this.mLayoutManager = layoutManager
    this.visibleThreshold = visibleThreshold
  }

  constructor(
    layoutManager: androidx.recyclerview.widget.GridLayoutManager,
    visibleThreshold: Int = 5
  ) {
    this.mLayoutManager = layoutManager
    this.visibleThreshold = visibleThreshold * layoutManager.spanCount
  }

  constructor(
    layoutManager: androidx.recyclerview.widget.StaggeredGridLayoutManager,
    visibleThreshold: Int = 5
  ) {
    this.mLayoutManager = layoutManager
    this.visibleThreshold = visibleThreshold * layoutManager.spanCount
  }

  private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
    var maxSize = 0
    for (i in lastVisibleItemPositions.indices) {
      if (i == 0) {
        maxSize = lastVisibleItemPositions[i]
      } else if (lastVisibleItemPositions[i] > maxSize) {
        maxSize = lastVisibleItemPositions[i]
      }
    }
    return maxSize
  }

  // This happens many times a second during a scroll, so be wary of the code you place here.
  // We are given a few useful parameters to help us work out if we need to load some more data,
  // but first we check if we are waiting for the previous load to finish.
  override fun onScrolled(
    view: androidx.recyclerview.widget.RecyclerView,
    dx: Int,
    dy: Int
  ) {

    val totalItemCount = mLayoutManager.itemCount

    val lastVisibleItemPosition = when (mLayoutManager) {
      is androidx.recyclerview.widget.StaggeredGridLayoutManager -> {
        // get maximum element within the list
        getLastVisibleItem(
            (mLayoutManager as androidx.recyclerview.widget.StaggeredGridLayoutManager).findLastVisibleItemPositions(
                null
            )
        )
      }
      is androidx.recyclerview.widget.GridLayoutManager ->
        (mLayoutManager as androidx.recyclerview.widget.GridLayoutManager).findLastVisibleItemPosition()
      is androidx.recyclerview.widget.LinearLayoutManager ->
        (mLayoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
      else -> 0
    }

    // If the total item count is zero and the previous isn't, assume the
    // list is invalidated and should be reset back to initial state
    if (totalItemCount < previousTotalItemCount) {
      this.currentPage = this.startingPageIndex
      this.previousTotalItemCount = totalItemCount
      if (totalItemCount == 0) {
        this.loading = true
      }
    }
    // If it’s still loading, we check to see if the dataset count has
    // changed, if so we conclude it has finished loading and update the current page
    // number and total item count.
    if (loading && totalItemCount > previousTotalItemCount) {
      loading = false
      previousTotalItemCount = totalItemCount
    }

    // If it isn’t currently loading, we check to see if we have breached
    // the visibleThreshold and need to reload more data.
    // If we do need to reload some more data, we execute onLoadMore to fetch the data.
    // threshold should reflect how many total columns there are too
    if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
      currentPage++
      onLoadMore(currentPage, totalItemCount, view)
      loading = true
    }
  }

  // Call this method whenever performing new searches
  fun resetState() {
    this.currentPage = this.startingPageIndex
    this.previousTotalItemCount = 0
    this.loading = true
  }

  // Defines the process for actually loading more data based on page
  protected abstract fun onLoadMore(
    page: Int,
    totalItemsCount: Int,
    view: androidx.recyclerview.widget.RecyclerView?
  )

}