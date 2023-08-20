package com.maldasabarkhabar.satyabrata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private WebView webView;
    private ProgressBar progressBar;
    private final List<String> externalDomains = Arrays.asList("www.facebook.com", "twitter.com", "www.youtube.com", "www.instagram.com", "api.whatsapp.com", "whatsapp.com", "hfiber.in"); // external domains URL

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.holo_red_light), android.graphics.PorterDuff.Mode.SRC_IN);

        webView = view.findViewById(R.id.webView);
        progressBar = view.findViewById(R.id.progressBar);

        // Set the custom WebViewClient for opening links in external apps
        webView.setWebViewClient(new ExternalAppWebViewClient(externalDomains));

        // Check if the device is connected to the internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Device is connected to the internet, load the web page
            webView.getSettings().setJavaScriptEnabled(true);

            // Enable caching
            WebSettings webSettings = webView.getSettings();
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            webSettings.setDatabaseEnabled(true);
            webSettings.setDatabasePath(getActivity().getCacheDir().getPath());
            webSettings.setDatabaseEnabled(true);
            webSettings.setDatabasePath(getActivity().getCacheDir().getPath());
            webSettings.setDomStorageEnabled(true);

            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if (progress < 100) {
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

            webView.loadUrl("https://www.sabarkhabar.in/app/"); // App Site URL
        } else {
            // Device is not connected to the internet, show a Toast message
            Toast.makeText(getActivity(), "You are not connected to the internet", Toast.LENGTH_SHORT).show();
        }

        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.destroy();
    }

    private static class ExternalAppWebViewClient extends WebViewClient {

        private final List<String> externalDomains;

        public ExternalAppWebViewClient(List<String> externalDomains) {
            this.externalDomains = externalDomains;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                // Check if the URL's domain is in the list of external domains
                Uri uri = Uri.parse(url);
                String domain = uri.getHost();
                if (domain != null && externalDomains.contains(domain)) {
                    // Open URL in external app
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                    return true;
                }
            }
            // Let the WebView handle the URL
            return false;
        }
    }
}
