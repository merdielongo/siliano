package com.anywhere.campasiliano.views.activities.users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.databinding.ActivityContactBinding;
import com.anywhere.campasiliano.databinding.ActivityContactsBinding;
import com.anywhere.campasiliano.views.activities.MainActivity;
import com.anywhere.campasiliano.views.fragments.ConversationFragment;
import com.anywhere.campasiliano.views.fragments.LessonFragment;
import com.anywhere.campasiliano.views.fragments.ProfileFragment;
import com.anywhere.campasiliano.views.fragments.UsersFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private ActivityContactsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ContactsActivity.ViewPageAdapter pageAdapter = new ContactsActivity.ViewPageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(new UsersFragment(), "Mes colleges");
        binding.viewPager.setAdapter(pageAdapter);
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
}