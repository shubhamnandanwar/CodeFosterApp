package com.clabs.codefosterapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle("Events");
        }

        fragmentManager = getSupportFragmentManager();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Blog");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<EventPost, PostViewHolder> eventPostRecyclerAdapter = new FirebaseRecyclerAdapter<EventPost, PostViewHolder>(
                EventPost.class,
                R.layout.post_row,
                PostViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, EventPost model, int position) {

                viewHolder.setDesc(model.getPostDesc());
                viewHolder.setHeading(model.getPostHeading());
                viewHolder.setImage(getApplicationContext(), model.getDownloadUrl());
                viewHolder.setEventPost(model);
            }
        };
        recyclerView.setAdapter(eventPostRecyclerAdapter);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mView;
        EventPost eventPost;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemView.setOnClickListener(this);
        }

        public void setHeading(String heading) {
            TextView postHeading = (TextView) mView.findViewById(R.id.post_heading);
            postHeading.setText(heading);
        }

        public void setDesc(String desc) {
            TextView postDesc = (TextView) mView.findViewById(R.id.post_desc);
            postDesc.setText(desc);
        }

        public void setImage(Context context, String image) {
            ImageView postImage = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(context).load(image).into(postImage);
        }

        public void setEventPost(EventPost eventPost) {
            this.eventPost = eventPost;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(PostFragment.POST_DESC, eventPost.getPostDesc());
            bundle.putString(PostFragment.POST_HEADING, eventPost.getPostHeading());
            bundle.putString(PostFragment.POST_IMAGE, eventPost.getDownloadUrl());
            PostFragment postFragment = new PostFragment();
            postFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .addToBackStack("post_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.container, postFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_event) {
            Intent intent = new Intent(this, PostEventActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int temp = fragmentManager.getBackStackEntryCount();
        if (temp == 0) {
            finish();
        } else
            fragmentManager.popBackStackImmediate();
    }
}
