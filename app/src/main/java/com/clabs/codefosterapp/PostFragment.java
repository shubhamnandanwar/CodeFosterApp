package com.clabs.codefosterapp;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by shubh on 02-12-2016.
 * This fragment allows users to see full post.
 */
public class PostFragment extends Fragment {

    public static final String POST_DESC = "post_desc";
    public static final String POST_HEADING = "post_heading";
    public static final String POST_IMAGE = "post_image";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post,container,false);

        Bundle bundle  = getArguments();
        String postDesc = bundle.getString(POST_DESC);
        String postHeading = bundle.getString(POST_HEADING);
        String postImageUrl = bundle.getString(POST_IMAGE);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {

            Drawable upArrow = ContextCompat.getDrawable(getContext(), R.mipmap.ic_keyboard_backspace_white_24dp);
            upArrow.mutate().setColorFilter(ContextCompat.getColor(getContext(), R.color.primary), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(postHeading);
        }

        TextView descriptionTextView = (TextView)view.findViewById(R.id.description_text_view);
        TextView headingTextView = (TextView)view.findViewById(R.id.heading_text_view);
        ImageView postImageView = (ImageView)view.findViewById(R.id.post_image);
        descriptionTextView.setText(postDesc);
        headingTextView.setText(postHeading);
        Picasso.with(getContext()).load(postImageUrl).into(postImageView);
        return view;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
