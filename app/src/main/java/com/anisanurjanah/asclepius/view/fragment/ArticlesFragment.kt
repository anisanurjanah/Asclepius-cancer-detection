package com.anisanurjanah.asclepius.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.anisanurjanah.asclepius.R
import com.anisanurjanah.asclepius.data.remote.response.ArticlesItem
import com.anisanurjanah.asclepius.databinding.FragmentArticlesBinding
import com.anisanurjanah.asclepius.helper.ViewModelFactory
import com.anisanurjanah.asclepius.model.ArticleViewModel
import com.anisanurjanah.asclepius.view.adapter.ArticleAdapter

class ArticlesFragment : Fragment() {
    private var _binding: FragmentArticlesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireActivity())
        )[ArticleViewModel::class.java]

        articleViewModel.articles.observe(viewLifecycleOwner) {
            setArticles(it)
        }
        articleViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setArticles(articles: List<ArticlesItem>) {
//        val layoutManager = GridLayoutManager(requireActivity(), 2)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvArticles.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvArticles.addItemDecoration(itemDecoration)

        val filteredArticles = articles.filter { !it.title!!.contains("Removed",true) }
        val articleAdapter = ArticleAdapter(filteredArticles)

        if (filteredArticles.isEmpty()) {
            binding.rvArticles.visibility = View.GONE
            binding.articleNotAvailable.visibility = View.VISIBLE
        } else {
            binding.rvArticles.visibility = View.VISIBLE
            binding.articleNotAvailable.visibility = View.GONE

            binding.rvArticles.adapter = articleAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setToolbar() {
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.topAppBar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.title = getString(R.string.article)
        toolbar.setTitleTextAppearance(requireContext(), R.style.ToolbarTitleText)
    }

    override fun onResume() {
        super.onResume()
        setToolbar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}