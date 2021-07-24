package com.anywhere.campasiliano.views.activities.chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.adapters.MessageAdapter;
import com.anywhere.campasiliano.common.APIService;
import com.anywhere.campasiliano.databinding.ActivityMessageBinding;
import com.anywhere.campasiliano.models.chats.Chat;
import com.anywhere.campasiliano.models.users.User;
import com.anywhere.campasiliano.notifications.Client;
import com.anywhere.campasiliano.notifications.Data;
import com.anywhere.campasiliano.notifications.MyResponse;
import com.anywhere.campasiliano.notifications.Sender;
import com.anywhere.campasiliano.notifications.Token;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.utils.anywhere.AnywhereZone;
import com.anywhere.campasiliano.views.activities.MainActivity;
import com.anywhere.campasiliano.views.activities.startup.WelcomeScreenActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    private ActivityMessageBinding binding;
    //private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private Intent intent;
    private FirebaseDatabase database;
    private DatabaseReference dataReference;
    private MessageAdapter messageAdapter;
    private List<Chat> mChat;
    private ValueEventListener seenList;
    private String userid;
    private APIService apiService;
    private boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Anywhere.activity = this;

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(MessageActivity.this, WelcomeScreenActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        intent = getIntent();
        userid = intent.getStringExtra("uid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dataReference = database.getReference("users").child(userid);
        dataReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot document = task.getResult();
                if (document.exists()) {
                    User user = document.getValue(User.class);
                    binding.username.setText(user.getFirst_name() + " " + user.getName());
                    if (user.getImageUrl().equals("default")) {
                        binding.profileImage.setImageResource(R.drawable.logo1);
                    } else {
                        Glide.with(this).load(user.getImageUrl()).into(binding.profileImage);
                    }
                    readMessages(firebaseUser.getUid(), userid, user.getImageUrl());
                    //Anywhere.message("DocumentSnapshot data: " + document.getChildren().toString());
                }else {
                    Anywhere.message("get failed with "+task.getException());
                }
            }
        });

        seeMessage(userid);

        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        binding.btnSend.setOnClickListener(v -> {
            notify = true;
            String message = AnywhereZone.text(binding.inputMessage);
            if(!TextUtils.isEmpty(message)) {
                sendMessage(firebaseUser.getUid(), userid, message);
            }else {
                Anywhere.message("Ecriver votre message");
            }
            binding.inputMessage.getEditText().setText("");
        });

    }

    private void seeMessage(String uid) {
        dataReference = database.getReference("chats");
        seenList = dataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(firebaseUser)) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("isseen", true);
                        dataSnapshot.getRef().updateChildren(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        dataReference = database.getReference("chats");

        dataReference.push().setValue(hashMap);

        final DatabaseReference chatRef = database.getReference("chatList")
                .child(firebaseUser.getUid())
                .child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        final String msg = message;
        dataReference = database.getReference("users").child(firebaseUser.getUid());
        dataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String username = user.getFirst_name()+" "+user.getName();
//                if (notify) {
//                    sendNotification(receiver, username, msg);
//                }
//                notify = false;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(String receiver, String username, String msg) {
        dataReference = database.getReference("tokens");
        Query query = dataReference.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Token token = dataSnapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.drawable.logo1, username+": "+msg, "New message", userid);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success == 1) {
                                    Anywhere.message("Une erreur c'est produit");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void readMessages(String uid, String userId, String imageUrl) {
        mChat = new ArrayList<>();
        dataReference = database.getReference("chats");
        dataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mChat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(uid) && chat.getSender().equals(userId) || chat.getReceiver().equals(userId) && chat.getSender().equals(uid)) {
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(Anywhere.activity, mChat, imageUrl);
                    binding.recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
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
        dataReference.removeEventListener(seenList);
        status("offline");
    }
}