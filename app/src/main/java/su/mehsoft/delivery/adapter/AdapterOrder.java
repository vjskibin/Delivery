package su.mehsoft.delivery.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import su.mehsoft.delivery.R;
import su.mehsoft.delivery.api.model.Order;

public class AdapterOrder extends ArrayAdapter<Order> {

    private Activity activity;
    private ArrayList<Order> lOrder;
    private static LayoutInflater inflater = null;

    public AdapterOrder(Activity activity, int textViewResourceId, ArrayList<Order> lOrder) {
        super(activity, textViewResourceId, lOrder);
        this.activity = activity;
        this.lOrder = lOrder;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return lOrder.size();
    }

    public Order getItem(Order position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_name;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.order_item, null);
                holder = new ViewHolder();

                holder.display_name = (TextView) vi.findViewById(R.id.tvOrderName);

                vi.setTag(holder);
            }
            else {
                holder = (ViewHolder) vi.getTag();
            }

            String msg = "Name: " + lOrder.get(position).getName()+ "\n" +
                    "Description: " + lOrder.get(position).getDescription() + "\n" +
                    "Salary: " + lOrder.get(position).getSalary() + "\n" +
                    "Location: " + lOrder.get(position).getLocation() + "\n";
            holder.display_name.setText(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vi;
    }
}
