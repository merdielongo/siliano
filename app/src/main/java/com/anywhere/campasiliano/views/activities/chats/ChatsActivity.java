package com.anywhere.campasiliano.views.activities.chats;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.adapters.ChatsAdapter;
import com.anywhere.campasiliano.common.ChatService;
import com.anywhere.campasiliano.common.interfaces.OnReadChatCallBack;
import com.anywhere.campasiliano.databinding.ActivityChatsBinding;
import com.anywhere.campasiliano.models.chats.Chats;
import com.anywhere.campasiliano.notifications.FirebaseService;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.views.activities.users.UserProfileActivity;
import com.anywhere.campasiliano.views.modals.DialogReviewSendImage;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatsActivity extends AppCompatActivity {

    private ActivityChatsBinding binding;
    private String receiverId;
    private String imageProfile;
    private String userName;
    private ChatsAdapter adapter;
    private final List<Chats> list = new ArrayList<>();
    private boolean isAction = false;
    private ChatService chatService;
    private final int IMAGE_GALLERY_REQUEST = 111;
    private Uri imageUri;
    private static final String TAG = "ChatsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Anywhere.activity = this;

        initialize();
        iniClickButton();
        readChats();
    }

    private void initialize() {

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        receiverId = intent.getStringExtra("userId");
        imageProfile = intent.getStringExtra("imageProfile");

        chatService = new ChatService(this, receiverId);

        if(receiverId != null) {
            binding.tvUsername.setText(userName);

            if(imageProfile.equals("default")) {
                binding.imageProfile.setImageResource(R.drawable.logo1);
            }else {
                Glide.with(this).load(imageProfile).into(binding.imageProfile);
            }
        }

        Objects.requireNonNull(binding.edMessage.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(binding.edMessage.getEditText().toString())){
                    binding.btnSend.setImageDrawable(getDrawable(R.drawable.ic_voice));
                } else {
                    binding.btnSend.setImageDrawable(getDrawable(R.drawable.ic_send));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatsAdapter(list, this);
        binding.recyclerView.setAdapter(adapter);
    }

    private void readChats() {
        chatService.readChatData(new OnReadChatCallBack() {
            @Override
            public void onReadSuccess(List<Chats> list) {
                adapter.setList(list);
            }

            @Override
            public void onReadFailed() {
                Anywhere.message("Error");
            }
        });
    }

    private void iniClickButton() {
        binding.btnSend.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(Objects.requireNonNull(binding.edMessage.getEditText()).toString().trim())){
                chatService.sendTextMsg(binding.edMessage.getEditText().getText().toString());
                binding.edMessage.getEditText().setText("");
            }
        });
        binding.btnBack.setOnClickListener(v -> finish());
        binding.imageProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, UserProfileActivity.class)
                    .putExtra("userId", receiverId)
                    .putExtra("imageProfile", imageProfile)
                    .putExtra("userName", userName));
        });
        binding.btnFile.setOnClickListener(v -> {
            if(isAction) {
                binding.layoutActions.setVisibility(View.GONE);
                isAction = false;
            } else {
                binding.layoutActions.setVisibility(View.VISIBLE);
                isAction = true;
            }
        });
        binding.btnGallery.setOnClickListener(v -> {
            openGallery();
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "selectionner une image"), IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            //uploadToFirebase();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                reviewImage(bitmap);
            }catch (Exception e) {e.printStackTrace();}
        }
    }

    private void reviewImage(Bitmap bitmap) {
        new DialogReviewSendImage(this, bitmap).show(() -> {
            if(imageUri != null) {
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Sending image ...");
                progressDialog.show();
                binding.layoutActions.setVisibility(View.GONE);
                isAction = false;
                new FirebaseService(this).uploadImageToFireBaseStorage(imageUri, new FirebaseService.OnCallBack() {
                    @Override
                    public void onUploadSuccess(String imageUrl) {
                        chatService.sendImage(imageUrl);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onUploadFailed(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}