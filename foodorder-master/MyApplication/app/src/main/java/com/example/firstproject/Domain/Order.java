package com.example.firstproject.Domain;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private String id; // Mã đơn hàng
    private String name;
    private String phone;
    private String address;
    private String total;
    private Map<String, String> foodNames; // Lưu tên món ăn
    private Map<String, Double> foodPrices; // Lưu giá món ăn
    private Map<String, Integer> foodQuantities; // Lưu số lượng món ăn

    public Order() {
        // Constructor trống để Firebase sử dụng
        foodNames = new HashMap<>();
        foodPrices = new HashMap<>();
        foodQuantities = new HashMap<>();
    }

    public Order(String id, String name, String phone, String address, String total,
                 Map<String, String> foodNames, Map<String, Double> foodPrices, Map<String, Integer> foodQuantities) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.foodNames = foodNames;
        this.foodPrices = foodPrices;
        this.foodQuantities = foodQuantities;
    }

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Map<String, String> getFoodNames() {
        return foodNames;
    }

    public void setFoodNames(Map<String, String> foodNames) {
        this.foodNames = foodNames;
    }

    public Map<String, Double> getFoodPrices() {
        return foodPrices;
    }

    public void setFoodPrices(Map<String, Double> foodPrices) {
        this.foodPrices = foodPrices;
    }

    public Map<String, Integer> getFoodQuantities() {
        return foodQuantities;
    }

    public void setFoodQuantities(Map<String, Integer> foodQuantities) {
        this.foodQuantities = foodQuantities;
    }
}