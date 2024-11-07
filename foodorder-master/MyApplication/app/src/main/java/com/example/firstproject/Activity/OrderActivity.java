package com.example.firstproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstproject.Adapter.OrderAdapter;
import com.example.firstproject.Domain.Foods;
import com.example.firstproject.Helper.ManagmentCart;
import com.example.firstproject.R;
import com.example.firstproject.databinding.ActivityOrderBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {
    private ActivityOrderBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        managmentCart = new ManagmentCart(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("orders");

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("EXTRA_NAME");
        String phone = intent.getStringExtra("EXTRA_PHONE");
        String address = intent.getStringExtra("EXTRA_ADDRESS");

        // Hiển thị dữ liệu
        TextView nameTextView = findViewById(R.id.nameTxt);
        TextView phoneTextView = findViewById(R.id.sdtTxt);
        TextView addressTextView = findViewById(R.id.locTxt);

        nameTextView.setText(name);
        phoneTextView.setText(phone);
        addressTextView.setText(address);

        setVariable();
        calulateCart();
        initList();

        Button saveOrderButton = findViewById(R.id.saveOrderButton);
        saveOrderButton.setOnClickListener(v -> saveOrderToFirebase(name, phone, address));
    }

    private void initList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.scrollviewOrder.setVisibility(View.GONE);
        } else {
            binding.scrollviewOrder.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.orderView.setLayoutManager(linearLayoutManager);
        adapter = new OrderAdapter(managmentCart.getListCart(), this, () -> calulateCart());
        binding.orderView.setAdapter(adapter);
    }

    private void calulateCart() {
        double percentTax = 0.02;
        double delivery = 10;

        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.00) / 100;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;

        binding.totalTxt.setText(total + "00đ");
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void saveOrderToFirebase(String name, String phone, String address) {
        // Tính toán tổng tiền
        double total = Double.parseDouble(binding.totalTxt.getText().toString().replace("00đ", ""));

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận thanh toán")
                .setMessage("Tổng số tiền: " + total + "00đ\nBạn có chắc chắn muốn đặt hàng?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Nếu người dùng xác nhận, lưu đơn hàng
                    String orderId = databaseReference.push().getKey();
                    Map<String, Object> orderData = new HashMap<>();
                    orderData.put("name", name);
                    orderData.put("phone", phone);
                    orderData.put("address", address);
                    orderData.put("total", binding.totalTxt.getText().toString());

                    ArrayList<Foods> foodList = managmentCart.getListCart();
                    if (!foodList.isEmpty()) {
                        for (Foods food : foodList) {
                            orderData.put("food_id_" + food.getId(), food.getId());
                            orderData.put("food_name_" + food.getId(), food.getTitle());
                            orderData.put("food_price_" + food.getId(), food.getPrice());
                            orderData.put("food_quantity_" + food.getId(), food.getNumberInCart());
                        }
                    }

                    if (orderId != null) {
                        databaseReference.child(orderId).setValue(orderData)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(this, "Đơn đặt thành công", Toast.LENGTH_SHORT).show();
                                        clearCartAndRefresh(); // Làm mới giỏ hàng
                                    } else {
                                        Toast.makeText(this, "Đặt đơn không thành công", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void clearCartAndRefresh() {
        managmentCart.clearCart();
        initList();
    }
}