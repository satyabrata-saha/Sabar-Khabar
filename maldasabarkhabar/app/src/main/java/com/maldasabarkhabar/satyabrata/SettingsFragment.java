package com.maldasabarkhabar.satyabrata;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;
import com.maldasabarkhabar.satyabrata.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private LinearLayout bannerLayout;
    private BannerView bannerView;
    private final String GAME_ID = "5314873";
    private final String BANNER_ID = "Banner_Android";
    private final String INTERSTITIAL_ID = "Interstitial_Android";
    private final boolean testMode = true;

    private IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            UnityAds.show(requireActivity(), INTERSTITIAL_ID);
        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
        }
    };

    private IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: " + placementId);
        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: " + placementId);
        }
    };

    private FragmentSettingsBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Button supportButton = binding.supportButton;
        supportButton.setOnClickListener(this::onSupportButtonClick);

        Button aboutUsButton = binding.aboutUsButton;
        aboutUsButton.setOnClickListener(this::onAboutUsButtonClick);

        Button interstitialButton = binding.interstitialButton;
        interstitialButton.setOnClickListener(this::onInterstitialButtonClick);

        bannerLayout = binding.bannerLayout;
        UnityAds.initialize(requireContext(), GAME_ID, testMode);
        bannerView = new BannerView((Activity) requireContext(), BANNER_ID, new UnityBannerSize(400, 90));
        bannerView.setListener(new BannerView.IListener() {
            @Override
            public void onBannerLoaded(BannerView bannerView) {
                Log.d("BannerAd", "Banner loaded successfully");
            }

            @Override
            public void onBannerClick(BannerView bannerView) {
                Log.d("BannerAd", "Banner clicked");
            }

            @Override
            public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
                Log.d("BannerAd", "Banner failed to load: " + bannerErrorInfo.errorMessage);
            }

            @Override
            public void onBannerLeftApplication(BannerView bannerView) {

            }
        });
        bannerView.load();
        bannerLayout.addView(bannerView);

        return view;
    }

    public void onSupportButtonClick(View view) {
        // Handle the support button click here
        // You can navigate to the support page or perform any other action
        Fragment supportFragment = new SupportFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, supportFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onAboutUsButtonClick(View view) {
        // Handle the about us button click here
        // You can navigate to the about us page or perform any other action
        Fragment aboutUsFragment = new AboutUsFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, aboutUsFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onInterstitialButtonClick(View view) {
        // Show interstitial ad
        UnityAds.load(INTERSTITIAL_ID, loadListener);
    }
}
