package com.example.gamestore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamestore.adapters.CartAdapter;
import com.example.gamestore.models.CartItem;
import com.example.gamestore.repository.CartRepository;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView tvTotalAmount, tvEmptyCart;
    private Button btnCheckout, btnClearCart;
    private ProgressBar progressBar;

    private CartRepository cartRepository;
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewCart);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnClearCart = findViewById(R.id.btnClearCart);
        progressBar = findViewById(R.id.progressBar);

        // Initialize repository
        cartRepository = new CartRepository(getApplication());

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this, cartItems, this);
        recyclerView.setAdapter(cartAdapter);

        // Load cart items
        loadCartItems();

        // Button listeners
        btnCheckout.setOnClickListener(v -> performCheckout());
        btnClearCart.setOnClickListener(v -> clearCart());
    }

    private void loadCartItems() {
        showProgress(true);

        // Multi-threaded loading
        cartRepository.getAllCartItems(new CartRepository.DataCallback<List<CartItem>>() {
            @Override
            public void onComplete(List<CartItem> result) {
                cartItems.clear();
                cartItems.addAll(result);
                cartAdapter.updateData(cartItems);

                updateUI();
                calculateTotal();
                showProgress(false);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(CartActivity.this, "Error loading cart: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }

    private void calculateTotal() {
        cartRepository.getTotalCartValue(new CartRepository.DataCallback<Integer>() {
            @Override
            public void onComplete(Integer result) {
                tvTotalAmount.setText("Total: $" + (result != null ? result : 0));
            }

            @Override
            public void onError(Exception e) {
                tvTotalAmount.setText("Total: $0");
            }
        });
    }

    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        showProgress(true);
        item.setQuantity(newQuantity);

        // Multi-threaded update
        cartRepository.updateCartItem(item, new CartRepository.DataCallback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                loadCartItems();
                Toast.makeText(CartActivity.this, "Quantity updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(CartActivity.this, "Error updating quantity", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }

    @Override
    public void onDeleteClick(CartItem item) {
        showProgress(true);

        // Multi-threaded delete
        cartRepository.deleteCartItem(item, new CartRepository.DataCallback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                loadCartItems();
                Toast.makeText(CartActivity.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(CartActivity.this, "Error removing item", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }

    private void clearCart() {
        showProgress(true);

        cartRepository.clearCart(new CartRepository.DataCallback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                loadCartItems();
                Toast.makeText(CartActivity.this, "Cart cleared", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(CartActivity.this, "Error clearing cart", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }

    private void performCheckout() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Implement checkout logic here
        Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();
        clearCart();
    }

    private void updateUI() {
        if (cartItems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyCart.setVisibility(View.VISIBLE);
            btnCheckout.setEnabled(false);
            btnClearCart.setEnabled(false);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyCart.setVisibility(View.GONE);
            btnCheckout.setEnabled(true);
            btnClearCart.setEnabled(true);
        }
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}