package com.maldasabarkhabar.satyabrata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class VideoFragment extends Fragment {

    private WebView webView;
    private ProgressBar progressBar;
    private Context context;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.holo_red_light), android.graphics.PorterDuff.Mode.SRC_IN);


        context = getActivity(); // Store the activity context

        webView = view.findViewById(R.id.webView);
        progressBar = view.findViewById(R.id.progressBar);

        // Check if the device is connected to the internet
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Device is connected to the internet, load the web page
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                    // Show the ProgressBar when the page starts loading
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // Hide the ProgressBar when the page finishes loading
                    progressBar.setVisibility(View.GONE);
                }
            });

            // Enable caching
            WebSettings webSettings = webView.getSettings();
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            webSettings.setDatabaseEnabled(true);
            webSettings.setDatabasePath(context.getCacheDir().getPath());
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            webView.loadUrl("https://www.sabarkhabar.in/app/yt/"); // Replace with the desired URL
        } else {
            // Device is not connected to the internet, show a Toast message
            Toast.makeText(context, "You are not connected to the internet", Toast.LENGTH_SHORT).show();
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
}
