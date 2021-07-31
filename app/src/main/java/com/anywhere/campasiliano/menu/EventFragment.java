package com.anywhere.campasiliano.menu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.adapters.EventAdapter;
import com.anywhere.campasiliano.databinding.FragmentEventBinding;
import com.anywhere.campasiliano.models.events.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class EventFragment extends Fragment {

    private FragmentEventBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference dataReference;
    private FirebaseUser firebaseUser;
    private List<Event> eventList;
    private EventAdapter eventAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        eventList = new ArrayList<>();

        // event recyclerView
        binding.recyclerViewEvent.setNestedScrollingEnabled(false);
        binding.recyclerViewEvent.setHasFixedSize(true);
        binding.recyclerViewEvent.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        this.events();

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