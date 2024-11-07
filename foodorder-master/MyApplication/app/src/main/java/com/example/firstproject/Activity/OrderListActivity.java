package com.example.firstproject.Activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.firstproject.Adapter.OrderListAdapter;
import com.example.firstproject.Domain.Order;
import com.example.firstproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView orderRecyclerView;
    private OrderListAdapter orderListAdapter;
    private ArrayList<Order> orderList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);

        // Khởi tạo RecyclerView
        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        orderListAdapter = new OrderListAdapter(orderList);
        orderRecyclerView.setAdapter(orderListAdapter);

        // Khởi tạo Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("orders");

        // Lấy danh sách đơn hàng từ Firebase
        fetchOrderList();

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());
    }

    private void fetchOrderList() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear(); // Xóa danh sách cũ
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class); // Lấy thông tin đơn hàng
                    if (order != null) {
                        String orderId = snapshot.getKey(); // Lấy mã đơn hàng
                        order.setId(orderId); // Gán mã đơn hàng vào đối tượng Order
                        orderList.add(order); // Thêm vào danh sách
                    }
                }
                orderListAdapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi ở đây
            }
        });
    }
}