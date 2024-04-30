package com.anisanurjanah.asclepius.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.anisanurjanah.asclepius.R
import com.anisanurjanah.asclepius.data.remote.response.ArticlesItem
import com.anisanurjanah.asclepius.databinding.FragmentHomeBinding
import com.anisanurjanah.asclepius.helper.ViewModelFactory
import com.anisanurjanah.asclepius.model.ArticleViewModel
import com.anisanurjanah.asclepius.view.adapter.HomeAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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

        binding.imgScreeningButton.setOnClickListener{
            showToast(getString(R.string.text_screening_button))
        }
        binding.articleButton.setOnClickListener{
            showToast(getString(R.string.text_article_button))
        }
    }

    private fun setArticles(articles: List<ArticlesItem>) {
        val layoutManager = GridLayoutManager(requireActivity(), 2)
//        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvArticles.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvArticles.addItemDecoration(itemDecoration)

        val filteredArticles = articles.filter { !it.title!!.contains("Removed",true) }
        val homeAdapter = HomeAdapter(filteredArticles)

        if (filteredArticles.isEmpty()) {
            binding.rvArticles.visibility = View.GONE
            binding.articleNotAvailable.visibility = View.VISIBLE
        } else {
            binding.rvArticles.visibility = View.VISIBLE
            binding.articleNotAvailable.visibility = View.GONE

            binding.rvArticles.adapter = homeAdapter
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setToolbar() {
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.topAppBar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.title = getString(R.string.app_name)
        toolbar.setTitleTextAppearance(requireContext(), R.style.ToolbarTitleTextHome)
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