package com.anywhere.campasiliano.views.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.databinding.ActivityStateHomeBinding;
import com.anywhere.campasiliano.menu.NewsFragment;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.views.activities.users.ContactsActivity;
import com.anywhere.campasiliano.views.fragments.ChatsFragment;
import com.anywhere.campasiliano.views.fragments.ConversationFragment;
import com.anywhere.campasiliano.views.fragments.NotificationFragment;

import java.util.ArrayList;
import java.util.List;

public class StateHomeActivity extends AppCompatActivity {

    private ActivityStateHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStateHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpWithViewPager(binding.viewPager);
        //setSupportActionBar(binding.toolbar);
        Anywhere.activity = this;
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //changeFabIcon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.fabAction.setOnClickListener(v ->{
            Anywhere.start(ContactsActivity.class);
        });
    }

    private void setUpWithViewPager(ViewPager viewPager) {
        StateHomeActivity.SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ConversationFragment(), "Conversations");
        adapter.addFragment(new NotificationFragment(), "Notifications");
        // we need 3 fragment
        viewPager.setAdapter(adapter);
    }

    private static class SectionsPageAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPageAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}