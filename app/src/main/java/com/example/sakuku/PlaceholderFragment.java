package com.example.sakuku;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlaceholderFragment extends Fragment {

    private ImageView capturedImageView;
    private TextView placeholderText;

    private ActivityResultLauncher<Intent> cameraLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        capturedImageView = view.findViewById(R.id.captured_image_view);
        placeholderText = view.findViewById(R.id.placeholder_text);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        if (imageBitmap != null) {
                            capturedImageView.setImageBitmap(imageBitmap);
                            capturedImageView.setVisibility(View.VISIBLE);
                            placeholderText.setVisibility(View.GONE);
                        }
                    }
                }
        );
    }

    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            cameraLauncher.launch(takePictureIntent);
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
        if (capturedImageView != null) {
            capturedImageView.setImageBitmap(bitmap);
            capturedImageView.setVisibility(View.VISIBLE);
            placeholderText.setVisibility(View.GONE);
        }
    }
}
