package com.anywhere.campasiliano.views.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.databinding.ActivityMainBinding;
import com.anywhere.campasiliano.models.users.User;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.views.activities.startup.WelcomeScreenActivity;
import com.anywhere.campasiliano.views.fragments.ChatsFragment;
import com.anywhere.campasiliano.views.fragments.LessonFragment;
import com.anywhere.campasiliano.views.fragments.ProfileFragment;
import com.anywhere.campasiliano.views.fragments.UsersFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference dataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Anywhere.activity = this;

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");



        ViewPageAdapter pageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(new ChatsFragment(), "Chats");
        pageAdapter.addFragment(new UsersFragment(), "Mes colleges");
        pageAdapter.addFragment(new LessonFragment(), "Mes cours");
        pageAdapter.addFragment(new ProfileFragment(), "Profile");
        binding.viewPager.setAdapter(pageAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dataReference = database.getReference("users").child(firebaseUser.getUid());
        dataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                binding.username.setText(user.getFirst_name()+" "+user.getName());
                if (user.getImageUrl().equals("default")) {
                    binding.profileImage.setImageResource(R.drawable.logo1);
                }else {
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(binding.profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
//                Anywhere.message("test");
                FirebaseAuth.getInstance().signOut();
                SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userPref.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                startActivity(new Intent(MainActivity.this, WelcomeScreenActivity.class));
                finish();
                return true;
        }
        return false;
    }

    class ViewPageAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> title;

        public ViewPageAdapter(@NonNull @NotNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.title = new ArrayList<>();
        }

        @NonNull
        @NotNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            this.fragments.add(fragment);
            this.title.add(title);
        }

        @Nullable
        @org.jetbrains.annotations.Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return this.title.get(position);
        }
    }

    private void status(String status) {
        dataReference = database.getReference("users").child(firebaseUser.getUid());
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", status);
        dataReference.updateChildren(map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}