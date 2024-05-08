package com.databridge.mybridge.ui.createpost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.databridge.mybridge.R;


public class TagPeopleFragment extends Fragment implements View.OnClickListener {


    private AppCompatImageView ivClose;
    private AppCompatTextView tvTitle, tvPost;
    private RecyclerView rv_Tag;

    public TagPeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tag_people, container, false);
      //  View includedLayout = rootView.findViewById(R.id.top);
        ivClose = rootView.findViewById(R.id.ivClose);
        tvTitle = rootView.findViewById(R.id.tvTitle);
        tvPost = rootView.findViewById(R.id.tvPost);

        tvTitle.setOnClickListener(this);
        tvPost.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == tvTitle) {
            tvTitle.setText("Tag People");
        }
        if (v == tvPost) {
            tvTitle.setText("Done");
        } if (v == ivClose) {
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }
}