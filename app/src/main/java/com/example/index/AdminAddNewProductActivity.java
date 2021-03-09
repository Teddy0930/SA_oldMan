package com.example.index;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String Quantity, Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice, InputProductQuantity;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product  Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        InputProductQuantity = (EditText) findViewById(R.id.product_quantity);
        loadingBar = new ProgressDialog(this);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // OpenGallery();
                CropImage.activity(ImageUri)
                        .setAspectRatio(1, 1)
                        .start(AdminAddNewProductActivity.this);
            }
        });


        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }
        });


    }

    private void ValidateProductData() {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();
        Quantity = InputProductQuantity.getText().toString();

        if (ImageUri == null) {
            Toast.makeText(this, "請選擇圖片", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "請輸入商品描述", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "請輸入商品價格", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Pname)) {
            Toast.makeText(this, "請輸入商品名稱", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Quantity)) {
            Toast.makeText(this, "請輸入商品數量", Toast.LENGTH_SHORT).show();
        } else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

        loadingBar.setTitle("新增商品");
        loadingBar.setMessage("請稍後,正在新增商品");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM  dd,   yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss  a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "錯誤:  " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminAddNewProductActivity.this, "照片圖片上傳成功", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(AdminAddNewProductActivity.this, "獲取圖片網址成功.", Toast.LENGTH_SHORT).show();


                            SaveProductInfoToDarabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDarabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey); // 獨一無二的金鑰
        productMap.put("admin", "Admins" + Prevalent.currentOnlineAdmin.getAccount());
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("quantity", Integer.parseInt(Quantity));
        productMap.put("price", Integer.parseInt(Price));
        productMap.put("pname", Pname);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "商品新增成功.", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "錯誤:  " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

   /* private void OpenGallery() {
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
            InputProductImage.setImageURI(ImageUri);
        }*/
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            ImageUri = result.getUri();

            InputProductImage.setImageURI(ImageUri);
        } else {
            Toast.makeText(this, "請再嘗試一次", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminAddNewProductActivity.this,AdminAddNewProductActivity.class));
            finish();
        }
    }
}
