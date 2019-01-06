package su.mehsoft.delivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import su.mehsoft.delivery.api.OrderAPI;
import su.mehsoft.delivery.api.model.Order;

public class OrdersActivity extends AppCompatActivity {

    private static OrderAPI orderApi;
    RecyclerView recyclerView;
    List<Order> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
    }
}
