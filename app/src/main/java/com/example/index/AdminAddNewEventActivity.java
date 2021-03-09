package com.example.index;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.index.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewEventActivity extends AppCompatActivity {

    private String eDate, eName, eMoney, eDescription, saveCurrentDate, saveCurrentTime, ePeople,ePlace;
    private Button AddNewEventButton;
    private ImageView InputEventImage;
    private EditText InputEventName, InputEventDescription, InputEventPrice, InputEventDate, InputEventPeople,InputEventPlace;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String eventRandomKey, downloadImageUrl;
    private StorageReference EventImagesRef;
    private DatabaseReference EventsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_event);

        EventImagesRef = FirebaseStorage.getInstance().getReference().child("Event Images");
        EventsRef = FirebaseDatabase.getInstance().getReference().child("Events");

        AddNewEventButton = (Button) findViewById(R.id.add_new_event);
        InputEventImage = (ImageView) findViewById(R.id.select_event_image);
        InputEventName = (EditText) findViewById(R.id.event_name);
        InputEventDescription = (EditText) findViewById(R.id.event_description);
        InputEventPrice = (EditText) findViewById(R.id.event_price);
        InputEventDate = (EditText) findViewById(R.id.event_date);
        InputEventPeople = (EditText) findViewById(R.id.event_people);
        InputEventPlace = (EditText) findViewById(R.id.event_place);
        InputEventPeople.setLayerType(View.LAYER_TYPE_HARDWARE,null);

        loadingBar = new ProgressDialog(this);
        InputEventDate.setInputType(InputType.TYPE_NULL);
        InputEventDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }

            }
        });
        InputEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        InputEventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity(ImageUri)
                        .setAspectRatio(1, 1)
                        .start(AdminAddNewEventActivity.this);
                //OpenGallery();
            }
        });


        AddNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }
        });


    }

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(AdminAddNewEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                InputEventDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void ValidateProductData() {
        eDescription = InputEventDescription.getText().toString();
        eMoney = InputEventPrice.getText().toString();
        eName = InputEventName.getText().toString();
        eDate = InputEventDate.getText().toString();
        ePeople = InputEventPeople.getText().toString();
        ePlace = InputEventPlace.getText().toString();

        if (ImageUri == null) {
            Toast.makeText(this, "請選擇圖片", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(eDescription)) {
            Toast.makeText(this, "請輸入活動描述", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(eMoney)) {
            Toast.makeText(this, "請輸入活動獎勵金額", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(eName)) {
            Toast.makeText(this, "請輸入活動名稱", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(eDate)) {
            Toast.makeText(this, "請輸入活動日期", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ePeople)) {
            Toast.makeText(this, "請輸入活動人數", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ePlace)) {
            Toast.makeText(this, "請輸入活動地點", Toast.LENGTH_SHORT).show();
        } else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

        loadingBar.setTitle("新增活動");
        loadingBar.setMessage("請稍後,正在新增活動");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM  dd,   yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss  a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        eventRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = EventImagesRef.child(ImageUri.getLastPathSegment() + eventRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewEventActivity.this, "錯誤:  " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminAddNewEventActivity.this, "照片圖片上傳成功", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {

                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddNewEventActivity.this, "獲取圖片網址成功.", Toast.LENGTH_SHORT).show();


                            SaveProductInfoToDarabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDarabase() {

      final  HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put("eid", eventRandomKey); // 獨一無二的金鑰
        eventMap.put("admin", "Admins" + Prevalent.currentOnlineAdmin.getAccount());
        eventMap.put("date", saveCurrentDate);
        eventMap.put("time", saveCurrentTime);
        eventMap.put("eDescription", eDescription);
        eventMap.put("ePlace",ePlace);
        eventMap.put("eImage", downloadImageUrl);
        eventMap.put("eDate", eDate);
        eventMap.put("ePeople", Integer.parseInt(ePeople));
        eventMap.put("eMoney", Integer.parseInt(eMoney));
        eventMap.put("eName", eName);

        EventsRef.child("User  View").child(eventRandomKey).updateChildren(eventMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            EventsRef.child("Admin  View").child("Admins"+Prevalent.currentOnlineAdmin.getAccount()).child(eventRandomKey).updateChildren(eventMap);
                            Intent intent = new Intent(AdminAddNewEventActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewEventActivity.this, "活動新增成功.", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewEventActivity.this, "錯誤:  " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    /*private void OpenGallery() {
        //圖片
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

       /* if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            InputEventImage.setImageURI(ImageUri);
        }*/
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            ImageUri = result.getUri();

            InputEventImage.setImageURI(ImageUri);
        } else {
            Toast.makeText(this, "請再嘗試一次", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminAddNewEventActivity.this,AdminAddNewEventActivity.class));
            finish();
        }
    }
}
