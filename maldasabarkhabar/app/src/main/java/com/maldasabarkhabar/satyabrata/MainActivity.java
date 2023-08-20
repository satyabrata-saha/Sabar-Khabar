package com.maldasabarkhabar.satyabrata;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAds.UnityAdsLoadError;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;
import com.maldasabarkhabar.satyabrata.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final String GAME_ID = "5314873";
    private final String BANNER_ID = "Banner_Android";
    private final String INTERSTITIAL_ID = "Interstitial_Android";
    private final boolean testMode = true;

    private LinearLayout bannerLayout;
    private BannerView bannerView;

    private ActivityMainBinding binding;

    private Handler handler;
    private long startTime;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NotificationScheduler.scheduleNotification(this);

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.video:
                    replaceFragment(new VideoFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsFragment());
                    break;
            }
            return true;
        });

        bannerLayout = findViewById(R.id.bannerAds);
        UnityAds.initialize(this, GAME_ID, testMode);

        UnityBannerSize bannerSize = new UnityBannerSize(400, 60);
        bannerView = new BannerView(this, BANNER_ID, bannerSize);
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

        // Schedule the hourly notifications
        scheduleHourlyNotifications();

        startTime = System.currentTimeMillis();

        // Show interstitial ad after opening the app for 12 seconds
        new Handler().postDelayed(this::showInterstitialAd, 12000);

        // Show interstitial ad every 2 minutes
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                if (currentTime - startTime >= 120000) {
                    showInterstitialAd();
                    startTime = currentTime;
                }

                // Repeat the process every 2 minutes
                handler.postDelayed(this, 120000);
            }
        }, 120000);
    }

    private void scheduleHourlyNotifications() {
        NotificationScheduler.scheduleNotification(this);
    }

    private void showInterstitialAd() {
        UnityAds.load(INTERSTITIAL_ID, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                UnityAds.show(MainActivity.this, placementId);
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAdsLoadError error, String message) {
                Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (fragment instanceof HomeFragment) {
            HomeFragment homeFragment = (HomeFragment) fragment;
            if (homeFragment.getView() != null && homeFragment.getView().findViewById(R.id.webView) != null) {
                WebView webView = homeFragment.getView().findViewById(R.id.webView);
                if (webView.canGoBack()) {
                    webView.goBack();
                    return;
                }
            }
        }

        super.onBackPressed();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
