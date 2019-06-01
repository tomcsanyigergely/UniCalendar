package hu.bme.aut.unicalendar.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hu.bme.aut.unicalendar.R;
import hu.bme.aut.unicalendar.data.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    private final List<Event> items;

    private ItemClickListener listener;

    public EventAdapter(ItemClickListener listener)
    {
        this.listener = listener;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event item = items.get(position);
        holder.eventItemRequirement.setText(item.requirement);
        holder.eventItemSubject.setText(item.subject);
        DateFormat dateFormat = new SimpleDateFormat("yyyy. MM. dd.");
        holder.eventItemDate.setText(dateFormat.format(item.date));
        holder.item = item;
    }

    public void addItem(Event item) {
        items.add(item);
        Collections.sort(items);
        notifyDataSetChanged();
    }

    public void update(List<Event> newItems) {
        items.clear();
        items.addAll(newItems);
        Collections.sort(items);
        notifyDataSetChanged();
    }

    public void delete(Event item) {
        int idx = items.indexOf(item);
        items.remove(item);
        notifyItemRemoved(idx);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface ItemClickListener {
        void onItemDeleted(Event item);
        void onItemClicked(Event item);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventItemRequirement;
        TextView eventItemSubject;
        TextView eventItemDate;
        ImageButton removeButton;
        RelativeLayout eventItemLayout;

        Event item;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventItemRequirement = itemView.findViewById(R.id.EventItemRequirement);
            eventItemSubject = itemView.findViewById(R.id.EventItemSubject);
            eventItemDate = itemView.findViewById(R.id.EventItemDate);
            eventItemLayout = itemView.findViewById(R.id.EventItemLayout);
            removeButton = itemView.findViewById(R.id.EventItemRemoveButton);

            eventItemLayout.setOnClickListener(new View.OnClickListener() {
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
