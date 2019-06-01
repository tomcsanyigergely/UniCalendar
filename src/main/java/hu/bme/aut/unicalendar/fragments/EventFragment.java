package hu.bme.aut.unicalendar.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import hu.bme.aut.unicalendar.MainActivity;
import hu.bme.aut.unicalendar.R;
import hu.bme.aut.unicalendar.adapter.EventAdapter;
import hu.bme.aut.unicalendar.adapter.SimpleFragmentPagerAdapter;
import hu.bme.aut.unicalendar.data.Event;

public class EventFragment extends Fragment implements
        MainActivity.ActionListener,
        AddEventDialogFragment.AddItemDialogListener,
        EventAdapter.ItemClickListener,
        SimpleFragmentPagerAdapter.Invalidatable,
        EditEventDialogFragment.EditItemDialogListener, SelectDateDialogFragment.SelectDateDialogListener {

    EventAdapter adapter;
    Calendar filterDate = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);
        adapter = new EventAdapter(this);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return layout;
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Event>>() {

            @Override
            protected List<Event> doInBackground(Void... voids) {
                List<Event> rows = ((MainActivity)getActivity()).getDatabase().eventDao().getAll();

                Iterator<Event> iterator;
                if (filterDate != null) {
                    iterator = rows.iterator();
                    while (iterator.hasNext()) {
                        Event item = iterator.next();
                        Calendar eventDate = Calendar.getInstance();
                        eventDate.setTime(item.date);
                        if (       filterDate.get(Calendar.YEAR)   < eventDate.get(Calendar.YEAR)
                                || filterDate.get(Calendar.YEAR)  == eventDate.get(Calendar.YEAR)
                                && filterDate.get(Calendar.MONTH)  < eventDate.get(Calendar.MONTH)
                                || filterDate.get(Calendar.YEAR)  == eventDate.get(Calendar.YEAR)
                                && filterDate.get(Calendar.MONTH) == eventDate.get(Calendar.MONTH)
                                && filterDate.get(Calendar.DAY_OF_MONTH) < eventDate.get(Calendar.DAY_OF_MONTH))
                        {
                            iterator.remove();
                        }
                    }
                }

                iterator = rows.iterator();
                while (iterator.hasNext()) {
                    Event item = iterator.next();
                    if (!((MainActivity)getActivity()).getDatabase().subjectDao().getVisibility(item.subjectId)
                            || !((MainActivity)getActivity()).getDatabase().requirementDao().getVisibility(item.requirementId))
                    {
                        iterator.remove();
                    }
                }

                for(Event event : rows) {
                    event.requirement = ((MainActivity)getActivity()).getDatabase().requirementDao().getRequirementById(event.requirementId).get(0).name;
                    event.subject = ((MainActivity)getActivity()).getDatabase().subjectDao().getSubjectById(event.subjectId).get(0).name;
                }



                return rows;
            }

            @Override
            protected void onPostExecute(List<Event> rows)
            {
                adapter.update(rows);
            }
        }.execute();
    }

    @Override
    public void handleFabClick() {
        new AddEventDialogFragment().setListener(this).show(getActivity().getSupportFragmentManager(), AddEventDialogFragment.class.getSimpleName());
    }

    @Override
    public void handleClearAllClick() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                ((MainActivity)getActivity()).getDatabase().eventDao().nukeTable();
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                ((MainActivity)getActivity()).updatePages();
            }
        }.execute();
    }

    @Override
    public void handleSelectAllClick() {
        filterDate = null;
        loadItemsInBackground();
    }

    public void handleSelectDateClick() {
        new SelectDateDialogFragment().setListener(this).show(getActivity().getSupportFragmentManager(), SelectDateDialogFragment.class.getSimpleName());
    }

    @Override
    public void onItemAdded(final Event item) {
        new AsyncTask<Void, Void, Event>() {

            @Override
            protected Event doInBackground(Void... voids) {
                item.subjectId = ((MainActivity)getActivity()).getDatabase().subjectDao().getSubjectByName(item.subject).get(0).id;
                item.requirementId = ((MainActivity)getActivity()).getDatabase().requirementDao().getRequirementByName(item.requirement).get(0).id;
                item.id = ((MainActivity)getActivity()).getDatabase().eventDao().insert(item);
                return item;
            }

            @Override
            protected void onPostExecute(Event event) {
                adapter.addItem(event);
            }
        }.execute();
    }

    @Override
    public void onItemDeleted(final Event item) {
        new AsyncTask<Void, Void, Event>() {

            @Override
            protected Event doInBackground(Void... voids) {
                ((MainActivity)getActivity()).getDatabase().eventDao().delete(item);
                return item;
            }

            @Override
            protected void onPostExecute(Event event) {
                adapter.delete(event);
            }
        }.execute();
    }

    @Override
    public void onItemClicked(Event item) {
        new EditEventDialogFragment().setListener(this).setItem(item).show(getActivity().getSupportFragmentManager(), EditEventDialogFragment.class.getSimpleName());
    }

    @Override
    public void invalidate() {
        loadItemsInBackground();
    }

    @Override
    public void onItemEdited(final Event item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                item.subjectId = ((MainActivity)getActivity()).getDatabase().subjectDao().getSubjectByName(item.subject).get(0).id;
                item.requirementId = ((MainActivity)getActivity()).getDatabase().requirementDao().getRequirementByName(item.requirement).get(0).id;
                ((MainActivity)getActivity()).getDatabase().eventDao().update(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                ((MainActivity)getActivity()).updatePages();
            }
        }.execute();
    }

    @Override
    public void onDateSelected(Date date) {
        filterDate = Calendar.getInstance();
        filterDate.setTime(date);
        loadItemsInBackground();
    }
}