package com.example.gamestore.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gamestore.models.CartItem;

import java.util.List;

@Dao
public interface CartDao {
    @Query("SELECT * FROM cart_items")
    List<CartItem> getAllCartItems();

    @Query("SELECT * FROM cart_items WHERE id = :id")
    CartItem getCartItemById(int id);

    @Query("SELECT * FROM cart_items WHERE name = :name AND type = :type")
    CartItem getCartItemByNameAndType(String name, String type);

    @Insert
    long insertCartItem(CartItem cartItem);

    @Update
    void updateCartItem(CartItem cartItem);

    @Delete
    void deleteCartItem(CartItem cartItem);

    @Query("DELETE FROM cart_items")
    void deleteAllCartItems();

    @Query("SELECT SUM(price * quantity) FROM cart_items")
    int getTotalCartValue();

    @Query("SELECT COUNT(*) FROM cart_items")
    int getCartItemCount();
}