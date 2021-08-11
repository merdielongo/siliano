package com.anywhere.campasiliano.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.anywhere.campasiliano.common.interfaces.OnReadChatCallBack;
import com.anywhere.campasiliano.models.chats.Chats;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatService {

    private Context context;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String receiverId;
    private View view;
    private List<Chats> list;

    public ChatService(Context context, String receiverId) {
        this.context = context;
        this.receiverId = receiverId;
        Anywhere.activity = (Activity) context;
    }


    public void readChatData(OnReadChatCallBack onCallBack) {
        reference.child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()){
                    Chats chats = data.getValue(Chats.class);
                    if(chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(receiverId)
                            || chats.getReceiver().equals(firebaseUser.getUid()) && chats.getSender().equals(receiverId)) {
                        list.add(chats);
                    }
                }
                onCallBack.onReadSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                onCallBack.onReadFailed();
            }
        });
    }

    public void sendTextMsg(String text) {
        Chats chats = new Chats();
        chats.setDateTime(getCurrentDate());
        chats.setType("TEXT");
        chats.setTextMessage(text);
        chats.setUrl("");
        chats.setSender(firebaseUser.getUid());
        chats.setReceiver(receiverId);

        reference.child("chats").push().setValue(chats)
                .addOnSuccessListener(unused -> {
                    Anywhere.message( "Envoyer");
                })
                .addOnFailureListener(e -> {
                    Anywhere.message("error");
                });

        // add to chatList
        DatabaseReference chatRef1 = FirebaseDatabase.getInstance()
                .getReference("chatList")
                .child(firebaseUser.getUid())
                .child(receiverId);
        chatRef1.child("chatId").setValue(receiverId);

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance()
                .getReference("chatList")
                .child(receiverId)
                .child(firebaseUser.getUid());
        chatRef2.child("chatId").setValue(firebaseUser.getUid());

    }

    public void sendImage(String imageUrl) {
        Chats chats = new Chats();
        chats.setTextMessage("");
        chats.setUrl(imageUrl);
        chats.setType("IMAGE");
        chats.setDateTime(getCurrentDate());
        chats.setSender(firebaseUser.getUid());
        chats.setReceiver(receiverId);

        reference.child("chats").push().setValue(chats)
                .addOnSuccessListener(unused -> {
                    Anywhere.message( "Envoyer");
                })
                .addOnFailureListener(e -> {
                    Anywhere.message("error");
                });

        // add to chatList
        DatabaseReference chatRef1 = FirebaseDatabase.getInstance()
                .getReference("chatList")
                .child(firebaseUser.getUid())
                .child(receiverId);
        chatRef1.child("chatId").setValue(receiverId);

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance()
                .getReference("chatList")
                .child(receiverId)
                .child(firebaseUser.getUid());
        chatRef2.child("chatId").setValue(firebaseUser.getUid());
    }

    public String getCurrentDate() {

        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String currentTime = dateFormat.format(currentDateTime.getTime());
        return today+", "+currentTime;
    }

}
