package com.example.easygo.Utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.easygo.BaseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

// TODO(1) extends this class in the desired activity
public class BaseUploadImageActivity extends BaseActivity {
    private static final String TAG = "BaseUploadImageActivity";

    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imagesRef;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageInterface onEventListener;


    // TODO(2) call this method in the activity and pass the context ( when the add image pressed)
    public void getImageUrl(ImageInterface callback) {
        this.onEventListener = callback;
        chooseImage();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        imagesRef = storageRef.child("images/" + UUID.randomUUID().toString());


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            final Loading loading = new Loading(BaseUploadImageActivity.this);
            loading.showLoading(0);
            filePath = data.getData();
            try {
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                if (bitmap != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 25, baos);
                    byte[] dataUp = baos.toByteArray();

                    UploadTask uploadTask = imagesRef.putBytes(dataUp);
                    // Listen for state changes, errors, and completion of the upload.
                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            Log.e(TAG, "onProgress: Upload is " + progress + "% done");
                            Toast.makeText(getApplicationContext(), "" + (int) progress + " %", Toast.LENGTH_SHORT).show();
                            loading.update((int) progress);


                        }
                    }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.e(TAG, "OnPaused: Upload is paused");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Log.e(TAG, "onFailure: ");
                            Toast.makeText(getApplicationContext(), "Error while uploading the image", Toast.LENGTH_LONG).show();
                            loading.dismissLoading();


                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Handle successful uploads on complete
                            // ...
                            if (taskSnapshot.getMetadata() != null) {
                                storageRef.child(taskSnapshot.getMetadata().getPath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Got the download URL for 'users/me/profile.png'
                                        //imgIv.setImageBitmap(bitmap);
                                        Log.e(TAG, "onSuccess:1 " + uri.getPath());
                                        Log.e(TAG, "onSuccess:2 " + uri.toString());

                                        onEventListener.get_Url(uri.toString());
                                        loading.dismissLoading();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        loading.dismissLoading();

                                    }
                                });


                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Error while uploading the image", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }


    // TODO(3) implement this interface in the activity
    public interface ImageInterface {
        void get_Url(String url);
    }


}
