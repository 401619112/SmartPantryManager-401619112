package za.co.smartpantry;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.Holder> {
    public interface Listener { void onEdit(PantryItem item); void onDelete(PantryItem item); }
    private final List<PantryItem> items; private final Listener listener;
    public PantryAdapter(List<PantryItem> items, Listener listener) { this.items = items; this.listener = listener; }
    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pantry_item, parent, false));
    }
    @Override public void onBindViewHolder(@NonNull Holder h, int position) {
        PantryItem item = items.get(position); h.name.setText(item.name);
        h.info.setText("Quantity: " + item.quantity + " " + item.unit + (item.expiryDate.isEmpty() ? "" : "\nExpires: " + item.expiryDate));
        h.edit.setOnClickListener(v -> listener.onEdit(item));
        h.delete.setOnClickListener(v -> new AlertDialog.Builder(v.getContext()).setTitle("Delete ingredient?")
            .setMessage("Remove " + item.name + " from your pantry?").setNegativeButton("Cancel", null)
            .setPositiveButton("Delete", (d, w) -> listener.onDelete(item)).show());
    }
    @Override public int getItemCount() { return items.size(); }
    static class Holder extends RecyclerView.ViewHolder {
        TextView name, info; Button edit, delete;
        Holder(View v) { super(v); name=v.findViewById(R.id.txtItemName); info=v.findViewById(R.id.txtItemInfo); edit=v.findViewById(R.id.btnEditItem); delete=v.findViewById(R.id.btnDeleteItem); }
    }
}
