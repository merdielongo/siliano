package com.anywhere.campasiliano.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.adapters.ChatListAdapter;
import com.anywhere.campasiliano.databinding.FragmentConversationBinding;
import com.anywhere.campasiliano.models.chats.ChatList;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConversationFragment extends Fragment {

    private FragmentConversationBinding binding;
    private List<ChatList> list;
    private ArrayList<String> allUserId;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private static final String TAG = "ChatsFragment";
    private Handler handler = new Handler();
    private ChatListAdapter adapter;

    public ConversationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConversationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Anywhere.activity = requireActivity();
        list = new ArrayList<>();
        allUserId = new ArrayList<>();
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ChatListAdapter(list, requireContext());
        binding.recyclerView.setAdapter(adapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        getChatList();
    }

    private void getChatList() {
        reference.child("chatList").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                allUserId.clear();
                for(DataSnapshot data : snapshot.getChildren()) {
                    String userID = Objects.requireNonNull(data.child("chatId").getValue()).toString();
                    Log.d(TAG, "onDataChange: "+userID);
                    allUserId.add(userID);
                }
                getUserInfo();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }

    private void getUserInfo() {
        handler.post(() -> {
            for(String userId : allUserId) {
                reference.child("users").child(userId).get()
                        .addOnSuccessListener(dataSnapshot -> {
                            ChatList chatList = dataSnapshot.getValue(ChatList.class);
                            list.add(chatList);
                            if(adapter != null) {
                                adapter.notifyItemInserted(0);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(Throwable::printStackTrace);
            }
        });
    }
}