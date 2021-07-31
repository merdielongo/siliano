package com.anywhere.campasiliano.menu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.anywhere.campasiliano.adapters.CoordinationAdapter;
import com.anywhere.campasiliano.adapters.EventAdapter;
import com.anywhere.campasiliano.adapters.FeedAdapter;
import com.anywhere.campasiliano.adapters.GroupAdapter;
import com.anywhere.campasiliano.adapters.LeaderAdapter;
import com.anywhere.campasiliano.adapters.ResponsibleAdapter;
import com.anywhere.campasiliano.common.HTTPDataHandler;
import com.anywhere.campasiliano.databinding.FragmentHomeBinding;
import com.anywhere.campasiliano.models.chats.Group;
import com.anywhere.campasiliano.models.etablishment.Coordination;
import com.anywhere.campasiliano.models.etablishment.Responsibles;
import com.anywhere.campasiliano.models.events.Event;
import com.anywhere.campasiliano.models.posts.RssObject;
import com.anywhere.campasiliano.models.users.Leader;
import com.anywhere.campasiliano.models.users.Student;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference dataReference;
    private FirebaseUser firebaseUser;

    private List<Responsibles> responsiblesList;
    private List<Coordination> coordinationList;
    private List<Leader> leaderList;
    private List<Group> groupList;
    private List<Event> eventList;

    private ResponsibleAdapter responsibleAdapter;
    private CoordinationAdapter coordinationAdapter;
    private LeaderAdapter leaderAdapter;
    private GroupAdapter groupAdapter;
    private EventAdapter eventAdapter;

    private RssObject rssObject;
    private final String RSS_LINK = "https://www.france24.com/fr/afrique/rss";
    private final String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        Anywhere.activity = requireActivity();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        responsiblesList = new ArrayList<>();
        coordinationList = new ArrayList<>();
        leaderList = new ArrayList<>();
        groupList = new ArrayList<>();
        eventList = new ArrayList<>();

        binding.recyclerViewResponsible.setNestedScrollingEnabled(false);
        binding.recyclerViewResponsible.setHasFixedSize(true);
        binding.recyclerViewResponsible.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        // coordination recyclerView
        binding.recyclerViewCoordination.setNestedScrollingEnabled(false);
        binding.recyclerViewCoordination.setHasFixedSize(true);
        binding.recyclerViewCoordination.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        // group recyclerView
        binding.recyclerViewGroup.setNestedScrollingEnabled(false);
        binding.recyclerViewGroup.setHasFixedSize(true);
        binding.recyclerViewGroup.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        // post or news recyclerView
        binding.recyclerViewPost.setNestedScrollingEnabled(false);
        binding.recyclerViewPost.setHasFixedSize(true);
        binding.recyclerViewPost.setLayoutManager(new LinearLayoutManager(requireActivity()));
        // event recyclerView
        binding.recyclerViewEvent.setNestedScrollingEnabled(false);
        binding.recyclerViewEvent.setHasFixedSize(true);
        binding.recyclerViewEvent.setLayoutManager(new GridLayoutManager(requireActivity(), 2));

        function();
        binding.swipeHome.setOnRefreshListener(this::function);

    }

    private void function() {
        getResponsibles();
        postsFeed();
        events();
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

    private void getResponsibles() {
        binding.swipeHome.setRefreshing(true);
        dataReference = database.getReference("students").child(firebaseUser.getUid());
        dataReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    Student student = snapshot.getValue(Student.class);
                    assert student != null;
                    authorities(student);
                    coordination(student);
                    leaders(student);
                    groups(student);
                }
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    private void authorities(Student student) {
        dataReference = database.getReference("establishments").child(student.getEstablishment());
        dataReference.child("responsibles").get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                responsiblesList.clear();
                DataSnapshot snapshot = task1.getResult();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Responsibles responsibles = dataSnapshot.getValue(Responsibles.class);
                        responsiblesList.add(responsibles);
                    }
                    responsibleAdapter = new ResponsibleAdapter(requireActivity(), responsiblesList);
                    binding.recyclerViewResponsible.setAdapter(responsibleAdapter);
                    binding.swipeHome.setRefreshing(false);
                }
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    private void coordination(Student student) {
        binding.swipeHome.setRefreshing(true);
        dataReference = database.getReference("establishments").child(student.getEstablishment()).child("coordinations");
        dataReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                coordinationList.clear();
                DataSnapshot snapshot = task.getResult();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Coordination coordination = dataSnapshot.getValue(Coordination.class);
                    coordinationList.add(coordination);
                }
                coordinationAdapter = new CoordinationAdapter(requireActivity(), coordinationList);
                binding.recyclerViewCoordination.setAdapter(coordinationAdapter);
                binding.swipeHome.setRefreshing(false);
            }
        });
    }

    private void leaders(Student student) {
        binding.swipeHome.setRefreshing(true);
        dataReference = database.getReference("establishments").child(student.getEstablishment()).child("leaders");
        dataReference.get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               leaderList.clear();
               DataSnapshot dataSnapshot = task.getResult();
               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   Leader leader = snapshot.getValue(Leader.class);
                   leaderList.add(leader);
               }
               leaderAdapter = new LeaderAdapter(requireActivity(), leaderList);
               binding.leaderViewPager.setAdapter(leaderAdapter);
               binding.leaderViewPager.setClipToPadding(false);
               binding.leaderViewPager.setClipChildren(false);
               binding.leaderViewPager.setOffscreenPageLimit(3);
               binding.leaderViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

               CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
               compositePageTransformer.addTransformer(new MarginPageTransformer(40));
               compositePageTransformer.addTransformer((page, position) -> {
                   float r = 1 - Math.abs(position);
                   page.setScaleY(0.85f + r * 0.15f);
               });
               binding.leaderViewPager.setPageTransformer(compositePageTransformer);
               binding.swipeHome.setRefreshing(false);
           }
        });
    }

    private void groups(Student student) {
        binding.swipeHome.setRefreshing(true);
        dataReference = database.getReference("groups");
        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    groupList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Group group = dataSnapshot.getValue(Group.class);
                        assert group != null;
                        if (student.getEstablishment().equals(group.getEstablishment())) {
                            groupList.add(group);
                        }
                        groupAdapter = new GroupAdapter(requireActivity(), groupList);
                        binding.recyclerViewGroup.setAdapter(groupAdapter);
                        binding.swipeHome.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                error.toException().printStackTrace();
                binding.swipeHome.setRefreshing(false);
            }
        });
    }

    private void events() {
        binding.swipeHome.setRefreshing(true);
        dataReference = database.getReference("events");
        dataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    eventList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Event event = dataSnapshot.getValue(Event.class);
                        assert event != null;
                        eventList.add(event);
                    }
                    eventAdapter = new EventAdapter(requireActivity(), eventList);
                    binding.recyclerViewEvent.setAdapter(eventAdapter);
                    binding.swipeHome.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                error.toException().printStackTrace();
                binding.swipeHome.setRefreshing(false);
            }
        });
    }

}