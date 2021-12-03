package com.sarftec.cristianoronaldo.view.fragment_wallpaper

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.sarftec.cristianoronaldo.databinding.FragmentCategoryBinding
import com.sarftec.cristianoronaldo.domain.repository.ImageRepository
import com.sarftec.cristianoronaldo.view.adapter.CategoryAdapter
import com.sarftec.cristianoronaldo.view.listener.CategoryFragmentListener
import com.sarftec.cristianoronaldo.view.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    @Inject
    lateinit var imageRepository: ImageRepository

    private lateinit var layoutBinding: FragmentCategoryBinding

    private val viewModel by viewModels<CategoryViewModel>()

    private var fragmentListener: CategoryFragmentListener? = null

    private val categoryAdapter by lazy {
        CategoryAdapter(lifecycleScope, viewModel) {
            fragmentListener?.navigateToDetailCategory(it)
        }
    }

    override fun onAttach(context: Context) {
        if(context is CategoryFragmentListener) fragmentListener = context
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layoutBinding = FragmentCategoryBinding.inflate(
            layoutInflater,
            container,
            false
        )
        viewModel.loadCategories()
        setUpRecyclerView()
        viewModel.categories.observe(viewLifecycleOwner) { resources ->
            if (resources.isSuccess()) resources.data?.let {
                categoryAdapter.submitData(it)
                showLayout(true)
            }
            if(resources.isLoading()) showLayout(false)
            if (resources.isError()) Log.v("TAG", "${resources.message}")
        }
        return layoutBinding.root
    }

    private fun showLayout(isShown: Boolean) {
        layoutBinding.recyclerView.visibility = if(isShown) View.VISIBLE else View.GONE
        layoutBinding.circularProgress.visibility = if(isShown) View.GONE else View.VISIBLE
    }

    private fun setUpRecyclerView() {
        layoutBinding.recyclerView.apply {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)/*
            StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            */
        }
    }
}