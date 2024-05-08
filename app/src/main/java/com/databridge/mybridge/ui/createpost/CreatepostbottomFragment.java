package com.databridge.mybridge.ui.createpost;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.databridge.mybridge.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class CreatepostbottomFragment extends BottomSheetDialogFragment implements View.OnClickListener {


    private AppCompatTextView txt_Camera, txt_Vedio, txt_tag, txt_gallery,txt_Gif;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int REQUEST_VIDEO_PICK = 3;
    private static final int REQUEST_GIF_PICK = 4;



    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_createpostbottom, container, false);
        txt_Camera = rootView.findViewById(R.id.txt_Camera);
        txt_Vedio = rootView.findViewById(R.id.txt_Vedio);
        txt_tag = rootView.findViewById(R.id.txt_tag);
        txt_gallery = rootView.findViewById(R.id.txt_gallery);
        txt_Gif = rootView.findViewById(R.id.txt_Gif);
        txt_Camera.setOnClickListener(this);
        txt_Vedio.setOnClickListener(this);
        txt_tag.setOnClickListener(this);
        txt_gallery.setOnClickListener(this);
        txt_Gif.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == txt_Camera) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Start the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(getActivity(), "Camera app not found", Toast.LENGTH_SHORT).show();
            }
        }
        if (v == txt_Vedio) {
            // Create an intent to pick a video from the gallery
            Intent pickVideoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            pickVideoIntent.setType("video/*");
            startActivityForResult(pickVideoIntent, REQUEST_VIDEO_PICK);
        }
        if (v == txt_tag) {
            fragment = new TagPeopleFragment();
            replaceFragment(fragment);
            dismiss();
        }
        if (v == txt_gallery) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
        }  if (v == txt_Gif) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/gif");
            startActivityForResult(intent, REQUEST_GIF_PICK);
        }

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data != null) {
            // Camera image captured
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Use the captured image as needed
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            // Image selected from gallery
            Uri selectedImage = data.getData();
            // Use the selected image URI as needed
        } else if (requestCode == REQUEST_VIDEO_PICK && resultCode == Activity.RESULT_OK && data != null) {
            // Video selected from gallery
            Uri selectedVideoUri = data.getData();
            // Use the selected video URI as needed
        } else if (requestCode == REQUEST_GIF_PICK && resultCode == Activity.RESULT_OK && data != null) {
            // GIF selected from gallery
            Uri selectedGifUri = data.getData();
            // Use the selected GIF URI as needed
        }
    }


}
