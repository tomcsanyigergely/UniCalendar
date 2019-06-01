package hu.bme.aut.unicalendar.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.unicalendar.R;
import hu.bme.aut.unicalendar.data.Requirement;

public class RequirementAdapter extends RecyclerView.Adapter<RequirementAdapter.RequirementViewHolder> {

    private final List<Requirement> items;

    private ItemClickListener listener;

    public RequirementAdapter(ItemClickListener listener)
    {
        this.listener = listener;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public RequirementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_requirement, parent, false);
        return new RequirementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RequirementViewHolder holder, int position) {
        Requirement item = items.get(position);
        holder.requirementNameTextView.setText(item.name);
        holder.isVisibleCheckBox.setOnCheckedChangeListener(null);
        holder.isVisibleCheckBox.setChecked(item.visible);
        holder.isVisibleCheckBox.setOnCheckedChangeListener(holder.getOnCheckedChangeListener());
        holder.item = item;
    }

    public void addItem(Requirement item) {
        items.add(item);
        notifyItemInserted(items.size()-1);
    }

    public void update(List<Requirement> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void delete(Requirement item) {
        int idx = items.indexOf(item);
        items.remove(item);
        notifyItemRemoved(idx);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public boolean checkAll() {
        boolean isAllVisible = true;
        for(Requirement item : items) {
            if (!item.visible) {
                isAllVisible = false;
                break;
            }
        }
        for(Requirement item : items) {
            item.visible = !isAllVisible;
        }
        notifyDataSetChanged();
        return !isAllVisible;
    }

    public interface ItemClickListener {
        void onItemDeleted(Requirement item);
        void onItemClicked(Requirement item);
        void onItemVisibilityChanged(Requirement item);
    }

    public class RequirementViewHolder extends RecyclerView.ViewHolder {

        CheckBox isVisibleCheckBox;
        TextView requirementNameTextView;
        ImageButton removeButton;
        RelativeLayout requirementItemLayout;

        Requirement item;

        public CheckBox.OnCheckedChangeListener getOnCheckedChangeListener() {
            return new CheckBox.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.visible = isChecked;
                    listener.onItemVisibilityChanged(item);
                }
            };
        }

        public RequirementViewHolder(@NonNull View itemView) {
            super(itemView);
            isVisibleCheckBox = itemView.findViewById(R.id.RequirementItemCheckBox);
            requirementNameTextView = itemView.findViewById(R.id.RequirementItemName);
            removeButton = itemView.findViewById(R.id.RequirementItemRemoveButton);
            requirementItemLayout = itemView.findViewById(R.id.RequirementItemLayout);

            isVisibleCheckBox.setOnCheckedChangeListener(getOnCheckedChangeListener());

            requirementItemLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onItemClicked(item);
                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemDeleted(item);
                }
            });
        }
    }
}
