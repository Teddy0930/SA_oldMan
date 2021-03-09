package com.example.index;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.index.Model.Products;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.index.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName, productQuantity;
    private String productID = "", adminId = "", FinalProductName, BuyCurrentDate, BuyCurrentTime, BuyProductKey;
    private Button BuyProduct;
    private int FinalProductQuantity, FinalProductPrice,adminMoney;
    private ProgressDialog loadingBar;
    private DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private DatabaseReference AdminsRef = FirebaseDatabase.getInstance().getReference().child("Admins");
    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");
        adminId = getIntent().getStringExtra("adminid");

        loadingBar = new ProgressDialog(this);
        BuyProduct = (Button) findViewById(R.id.btn_buy);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        productQuantity = (TextView) findViewById(R.id.product_quantity_details);


        getProductDetails(productID);


        BuyProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (FinalProductQuantity != 0) {
                    if (Prevalent.currentOnlineUser.getMoney() >= FinalProductPrice * Integer.parseInt(numberButton.getNumber())) {

                        if (Integer.parseInt(numberButton.getNumber()) <= FinalProductQuantity) {

                            loadingBar.setTitle("兌換處理中");
                            loadingBar.setMessage("請稍號,正在兌換.");
                            loadingBar.setCanceledOnTouchOutside(false);
                            loadingBar.show();

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("MMM  dd,  yyyy");
                            BuyCurrentDate = currentDate.format(calForDate.getTime());

                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss  a");
                            BuyCurrentTime = currentTime.format(calForDate.getTime());
                            BuyProductKey = BuyCurrentTime + BuyCurrentDate;
                            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("buyProductList");


                            final HashMap<String, Object> buyProductListMap = new HashMap<>();
                            buyProductListMap.put("pid", productID);  //傳遞
                            buyProductListMap.put("bid", BuyProductKey);
                            buyProductListMap.put("pname", FinalProductName);
                            buyProductListMap.put("price", FinalProductPrice);
                            buyProductListMap.put("Totalprice", FinalProductPrice * Integer.parseInt(numberButton.getNumber()));
                            buyProductListMap.put("date", BuyCurrentDate);
                            buyProductListMap.put("time", BuyCurrentTime);
                            buyProductListMap.put("user", "Users"+Prevalent.currentOnlineUser.getAccount());
                            buyProductListMap.put("userName", Prevalent.currentOnlineUser.getName());
                            buyProductListMap.put("quantity", Integer.parseInt(numberButton.getNumber()));

                            final HashMap<String, Object> usersMap = new HashMap<>();
                            usersMap.put("money", Prevalent.currentOnlineUser.getMoney() - FinalProductPrice * Integer.parseInt(numberButton.getNumber()));  //傳遞
                            UsersRef.child("Users" + Prevalent.currentOnlineUser.getAccount()).updateChildren(usersMap);
                            Prevalent.currentOnlineUser.setMoney(Prevalent.currentOnlineUser.getMoney() - FinalProductPrice * Integer.parseInt(numberButton.getNumber()));

                          AdminsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                  adminMoney=Integer.parseInt(dataSnapshot.child(adminId).child("money").getValue().toString());
                                  final HashMap<String, Object> adminsMap = new HashMap<>();
                                  adminsMap.put("money",adminMoney + FinalProductPrice * Integer.parseInt(numberButton.getNumber()));  //傳遞
                                  AdminsRef.child(adminId).updateChildren(adminsMap);
                              }

                              @Override
                              public void onCancelled(@NonNull DatabaseError databaseError) {

                              }
                          });
                         /*   final HashMap<String, Object> adminsMap = new HashMap<>();
                            adminsMap.put("money", Prevalent.currentOnlineAdmin.getMoney() + FinalProductPrice * Integer.parseInt(numberButton.getNumber()));  //傳遞
                            AdminsRef.child("Admins"+adminId).updateChildren(adminsMap);
                            Prevalent.currentOnlineAdmin.setMoney(Prevalent.currentOnlineAdmin.getMoney() + FinalProductPrice * Integer.parseInt(numberButton.getNumber()));*/

                            cartListRef.child("User  View").child("Users" + Prevalent.currentOnlineUser.getAccount())
                                    .child("Products").child(BuyProductKey)
                                    .updateChildren(buyProductListMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                cartListRef.child("Admin  View").child(adminId)
                                                        .child("Products").child(BuyProductKey)
                                                        .updateChildren(buyProductListMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()) {
                                                                    productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            HashMap<String, Object> ProductsMap = new HashMap<>();
                                                                            ProductsMap.put("quantity", Integer.parseInt(dataSnapshot.child(productID).child("quantity").getValue().toString()) - Integer.parseInt(numberButton.getNumber()));
                                                                            productsRef.child(productID).updateChildren(ProductsMap);


                                                                            loadingBar.dismiss();
                                                                            Toast.makeText(ProductDetailsActivity.this, "兌換成功!", Toast.LENGTH_SHORT).show();
                                                                            Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                                            startActivity(intent);
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });


                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "抱歉! 商品剩餘數量不足!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ProductDetailsActivity.this, "抱歉! 錢不夠兌換喔!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    BuyProduct.setText("商品已經賣完了");
                    Toast.makeText(ProductDetailsActivity.this, "抱歉! 商品已賣完!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void getProductDetails(String productID) {


        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText("產品名稱: " + products.getPname());
                    productPrice.setText("產品價格: " + String.valueOf(products.getPrice()));
                    productDescription.setText("產品描述: " + products.getDescription());
                    productQuantity.setText("產品剩餘數量: " + String.valueOf(products.getQuantity()));
                    FinalProductName = products.getPname();
                    FinalProductPrice = products.getPrice();
                    FinalProductQuantity = products.getQuantity();

                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
