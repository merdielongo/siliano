package com.anywhere.campasiliano.menu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anywhere.campasiliano.adapters.CoordinationAdapter;
import com.anywhere.campasiliano.adapters.ResponsibleAdapter;
import com.anywhere.campasiliano.databinding.FragmentHomeBinding;
import com.anywhere.campasiliano.models.etablishment.Coordination;
import com.anywhere.campasiliano.models.etablishment.Responsibles;
import com.anywhere.campasiliano.models.users.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private ResponsibleAdapter responsibleAdapter;
    private CoordinationAdapter coordinationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        responsiblesList = new ArrayList<>();
        coordinationList = new ArrayList<>();

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
                }
            }
        }).addOnFailureListener(Throwable::printStackTrace);;
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

}