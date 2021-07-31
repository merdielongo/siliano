package com.anywhere.campasiliano.menu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.adapters.FeedAdapter;
import com.anywhere.campasiliano.common.HTTPDataHandler;
import com.anywhere.campasiliano.databinding.FragmentNewsBinding;
import com.anywhere.campasiliano.models.posts.RssObject;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;
    private RssObject rssObject;
    private final String RSS_LINK = "https://www.france24.com/fr/afrique/rss";
    private final String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // post or news recyclerView
        binding.recyclerViewPost.setNestedScrollingEnabled(false);
        binding.recyclerViewPost.setHasFixedSize(true);
        binding.recyclerViewPost.setLayoutManager(new LinearLayoutManager(requireActivity()));
        this.postsFeed();
    }

    private void postsFeed() {
        AsyncTask<String, String, String> loadRSSAsync = new AsyncTask<String, String, String>() {

            ProgressDialog dialog = new ProgressDialog(requireActivity());

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Please wait ...");
                dialog.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                String result;
                HTTPDataHandler httpDataHandler = new HTTPDataHandler();
                result = httpDataHandler.GetHTTPData(strings[0]);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                dialog.dismiss();
                rssObject = new Gson().fromJson(s, RssObject.class);
                FeedAdapter feedAdapter = new FeedAdapter(rssObject, requireContext());
                binding.recyclerViewPost.setAdapter(feedAdapter);
                feedAdapter.notifyDataSetChanged();
            }
        };
        StringBuilder builder = new StringBuilder(RSS_to_Json_API);
        builder.append(RSS_LINK);
        loadRSSAsync.execute(builder.toString());
    }
}