package com.anywhere.campasiliano.notifications;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class FirebaseService {

    private Context context;

    public FirebaseService(Context context) {
        this.context = context;
    }

    public void uploadImageToFireBaseStorage(Uri uri, OnCallBack onCallBack) {
        StorageReference mountainsRef = FirebaseStorage.getInstance()
                .getReference()
                .child("imagesChats/"+System.currentTimeMillis()+ "." +getFileExtention(uri));
        mountainsRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadUrl = uriTask.getResult();
                    String download_url = String.valueOf(downloadUrl);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("imageProfile", download_url);
                    onCallBack.onUploadSuccess(download_url);
                })
                .addOnFailureListener(onCallBack::onUploadFailed);
    }

    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public interface OnCallBack {
        void onUploadSuccess(String imageUrl);
        void onUploadFailed(Exception e);
    }
}
