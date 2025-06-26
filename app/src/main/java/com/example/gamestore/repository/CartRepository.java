package com.example.gamestore.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.example.gamestore.database.CartDao;
import com.example.gamestore.database.CartDatabase;
import com.example.gamestore.models.CartItem;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CartRepository {
    private CartDao cartDao;
    private CartDatabase db;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public CartRepository(Application application) {
        db = CartDatabase.getDatabase(application);
        cartDao = db.cartDao();
    }

    // Interface for callbacks
    public interface DataCallback<T> {
        void onComplete(T result);
        void onError(Exception e);
    }

    // Load all cart items (Multi-threaded)
    public void getAllCartItems(DataCallback<List<CartItem>> callback) {
        CartDatabase.databaseWriteExecutor.execute(() -> {
            try {
                List<CartItem> items = cartDao.getAllCartItems();
                mainHandler.post(() -> callback.onComplete(items));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    // Add item to cart (Multi-threaded)
    public void addToCart(CartItem item, DataCallback<Boolean> callback) {
        CartDatabase.databaseWriteExecutor.execute(() -> {
            try {
                // Check if item already exists
                CartItem existingItem = cartDao.getCartItemByNameAndType(item.getName(), item.getType());

                if (existingItem != null) {
                    // Update quantity if exists
                    existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                    cartDao.updateCartItem(existingItem);
                } else {
                    // Insert new item
                    cartDao.insertCartItem(item);
                }

                mainHandler.post(() -> callback.onComplete(true));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    // Update cart item (Multi-threaded)
    public void updateCartItem(CartItem item, DataCallback<Boolean> callback) {
        CartDatabase.databaseWriteExecutor.execute(() -> {
            try {
                cartDao.updateCartItem(item);
                mainHandler.post(() -> callback.onComplete(true));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    // Delete cart item (Multi-threaded)
    public void deleteCartItem(CartItem item, DataCallback<Boolean> callback) {
        CartDatabase.databaseWriteExecutor.execute(() -> {
            try {
                cartDao.deleteCartItem(item);
                mainHandler.post(() -> callback.onComplete(true));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    // Get total cart value (Multi-threaded)
    public void getTotalCartValue(DataCallback<Integer> callback) {
        CartDatabase.databaseWriteExecutor.execute(() -> {
            try {
                int total = cartDao.getTotalCartValue();
                mainHandler.post(() -> callback.onComplete(total));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    // Clear cart (Multi-threaded)
    public void clearCart(DataCallback<Boolean> callback) {
        CartDatabase.databaseWriteExecutor.execute(() -> {
            try {
                cartDao.deleteAllCartItems();
                mainHandler.post(() -> callback.onComplete(true));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
}