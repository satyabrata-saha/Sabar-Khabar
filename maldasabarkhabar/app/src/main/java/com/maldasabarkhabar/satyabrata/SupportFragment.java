package com.maldasabarkhabar.satyabrata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class SupportFragment extends Fragment {

    // ...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        Button instagramButton = view.findViewById(R.id.instagram);
        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Instagram app or website
                String instagramUrl = "https://www.instagram.com/satyabrata_saha_/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl));
                startActivity(intent);
            }
        });

        return view;
    }

    // ...
}
