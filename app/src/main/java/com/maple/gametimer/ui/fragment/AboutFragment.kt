package com.maple.gameTimer.ui.fragment

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maple.gameTimer.databinding.FragmentAboutBinding

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

        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init(binding)

        return root
    }
    private fun init(binding: FragmentAboutBinding) {
        binding.aboutFragmentAppVersion.text = getVersionName()

        val appVersionLayout = binding.aboutFragmentAppVersionLayout
        val githubLayout = binding.aboutFragmentGithubLayout
        val licenceLayout = binding.aboutFragmentLicenceLayout
        val privacyAgreementLayout = binding.aboutFragmentPrivacyAgreementLayout
        val authorLayout = binding.aboutAuthorLayout

        appVersionLayout.setOnClickListener {
            //TODO 设置对应点击事件
        }
        githubLayout.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, GITHUB_URI))
        }
        licenceLayout.setOnClickListener {
            //TODO 设置对应点击事件
        }
        privacyAgreementLayout.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, PRIVACY_POLICY_URI))
        }
        authorLayout.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, AUTHOR_GITHUB_URI))
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