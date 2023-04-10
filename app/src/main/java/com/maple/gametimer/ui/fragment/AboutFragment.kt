package com.maple.gameTimer.ui.fragment

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.maple.gameTimer.databinding.FragmentAboutBinding
import com.maple.gameTimer.ui.viewModel.AboutViewModel


class AboutFragment : Fragment() {
    private val TAG: String = "AboutFragment"

    private var _binding: FragmentAboutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val aboutViewModel =
            ViewModelProvider(this)[AboutViewModel::class.java]

        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init(binding, aboutViewModel)

        return root
    }

    @Suppress("UNUSED_PARAMETER")
    private fun init(binding: FragmentAboutBinding, aboutViewModel: AboutViewModel) {
        binding.aboutFragmentAppVersion.text = getVersionName()

        val appVersionComponent = binding.aboutFragmentAppVersionComponent
        val licenceComponent = binding.aboutFragmentLicenceComponent
        val privacyAgreementComponent = binding.aboutFragmentPrivacyAgreementComponent
        val authorComponent = binding.aboutAuthorComponent

        //TODO 设置对应点击事件
        appVersionComponent.setOnClickListener {
            Toast.makeText(context, "appVersionComponent", Toast.LENGTH_SHORT).show()
        }
        licenceComponent.setOnClickListener {
            Toast.makeText(context, "licenceComponent", Toast.LENGTH_SHORT).show()
        }
        privacyAgreementComponent.setOnClickListener {
            Toast.makeText(context, "privacyAgreementComponent", Toast.LENGTH_SHORT).show()
        }
        authorComponent.setOnClickListener {
            Toast.makeText(context, "authorComponent", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getVersionName(): String? {
        val packageManager = requireActivity().packageManager
        val packInfo = packageManager.getPackageInfoCompat(
            requireActivity().packageName, 0
        )
        return packInfo.versionName
    }

    private fun PackageManager.getPackageInfoCompat(
        packageName: String,
        flags: Int = 0
    ): PackageInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val GITHUB_URI = Uri.parse("https://github.com/fengluo2/GameTimer")
        private val PRIVACY_POLICY_URI =
            Uri.parse("https://github.com/fengluo2/GameTimer/blob/master/PRIVACY.md")
        private val AUTHOR_GITHUB_URI = Uri.parse("https://github.com/fengluo2")
    }
}