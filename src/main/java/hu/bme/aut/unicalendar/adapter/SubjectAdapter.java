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

import hu.bme.aut.unicalendar.MainActivity;
import hu.bme.aut.unicalendar.R;
import hu.bme.aut.unicalendar.data.Subject;
import hu.bme.aut.unicalendar.fragments.EditSubjectDialogFragment;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private final List<Subject> items;

    private ItemClickListener listener;

    public SubjectAdapter(ItemClickListener listener)
    {
        this.listener = listener;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject item = items.get(position);
        holder.subjectNameTextView.setText(item.name);
        holder.isVisibleCheckBox.setOnCheckedChangeListener(null);
        holder.isVisibleCheckBox.setChecked(item.visible);
        holder.isVisibleCheckBox.setOnCheckedChangeListener(holder.getOnCheckedChangeListener());
        holder.item = item;
    }

    public void addItem(Subject item) {
        items.add(item);
        notifyItemInserted(items.size()-1);
    }

    public void update(List<Subject> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void delete(Subject item) {
        int idx = items.indexOf(item);
        items.remove(item);
        notifyItemRemoved(idx);
    }

    public boolean checkAll() {
        boolean isAllVisible = true;
        for(Subject item : items) {
            if (!item.visible) {
                isAllVisible = false;
                break;
            }
        }
        for(Subject item : items) {
            item.visible = !isAllVisible;
        }
        notifyDataSetChanged();
        return !isAllVisible;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface ItemClickListener {
        void onItemDeleted(Subject item);
        void onItemClicked(Subject item);
        void onItemVisibilityChanged(Subject item);
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder {

        CheckBox isVisibleCheckBox;
        TextView subjectNameTextView;
        ImageButton removeButton;
        RelativeLayout subjectItemLayout;

        Subject item;

        public CheckBox.OnCheckedChangeListener getOnCheckedChangeListener() {
            return new CheckBox.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.visible = isChecked;
                    listener.onItemVisibilityChanged(item);
                }
            };
        }

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            isVisibleCheckBox = itemView.findViewById(R.id.SubjectItemCheckBox);
            subjectNameTextView = itemView.findViewById(R.id.SubjectItemName);
            removeButton = itemView.findViewById(R.id.SubjectItemRemoveButton);
            subjectItemLayout = itemView.findViewById(R.id.SubjectItemLayout);

            isVisibleCheckBox.setOnCheckedChangeListener(getOnCheckedChangeListener());

            subjectItemLayout.setOnClickListener(new View.OnClickListener() {
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
