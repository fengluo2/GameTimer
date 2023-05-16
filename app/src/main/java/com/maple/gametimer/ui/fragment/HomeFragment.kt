package com.maple.gameTimer.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maple.gameTimer.R
import com.maple.gameTimer.adapter.GameListAdapter
import com.maple.gameTimer.adapter.entity.Game
import com.maple.gameTimer.databinding.FragmentHomeBinding
import com.maple.gameTimer.ui.activity.config.ModeFTTPRConfig
import com.maple.gameTimer.ui.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init(binding, homeViewModel)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init(binding: FragmentHomeBinding, homeViewModel: HomeViewModel) {
        val recyclerView: RecyclerView = binding.homeFragmentList
        homeViewModel.listData.observe(viewLifecycleOwner) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val mainActivityListAdapter = GameListAdapter(requireContext(), it)
            mainActivityListAdapter.setOnItemClickListener(object :
                GameListAdapter.OnItemClickListener {
                override fun onNameClick(context: Context, view: View, item: Game, position: Int) {
                    startActivity(Intent().setClass(context, item.cls))
                }

                override fun onSettingClick(context: Context, view: View, item: Game, position: Int) {
                    val popupWindowView: View =
                        LayoutInflater.from(context).inflate(item.settingResource, null)
                    val popupWindow = PopupWindow(
                        popupWindowView,
                        resources.displayMetrics.widthPixels * 2 / 3,
                        resources.displayMetrics.heightPixels * 1 / 2,
                        true
                    )
                    popupWindow.isOutsideTouchable = true //设置点击外部区域可以取消popupWindow
                    popupWindow.animationStyle = android.R.style.Animation_Activity
                    popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.white))
                    popupWindow.showAtLocation(view.rootView, Gravity.CENTER, 0, 0)
                    popupWindow.setOnDismissListener { setAlpha(1.0f) }
                    setAlpha(0.5f)

                    when (item.uid) {
                        "FTTPR" -> {
                            ModeFTTPRConfig(
                                context
                            ).init(popupWindowView, item, popupWindow)
                        }
                    }
                }
            })
            recyclerView.adapter = mainActivityListAdapter
        }

    }

    private fun setAlpha(f: Float) {
        val lp = requireActivity().window.attributes
        lp.alpha = f
        requireActivity().window.attributes = lp
    }
}
