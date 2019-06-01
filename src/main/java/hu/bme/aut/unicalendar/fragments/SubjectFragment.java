package hu.bme.aut.unicalendar.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hu.bme.aut.unicalendar.MainActivity;
import hu.bme.aut.unicalendar.R;
import hu.bme.aut.unicalendar.adapter.SimpleFragmentPagerAdapter;
import hu.bme.aut.unicalendar.adapter.SubjectAdapter;
import hu.bme.aut.unicalendar.data.Subject;

public class SubjectFragment extends Fragment implements
        MainActivity.ActionListener,
        AddSubjectDialogFragment.AddItemDialogListener,
        SubjectAdapter.ItemClickListener,
        SimpleFragmentPagerAdapter.Invalidatable,
        EditSubjectDialogFragment.EditItemDialogListener {

    SubjectAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);
        adapter = new SubjectAdapter(this);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return layout;
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Subject>>() {

            @Override
            protected List<Subject> doInBackground(Void... voids) {
                return ((MainActivity)getActivity()).getDatabase().subjectDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Subject> rows) {
                adapter.update(rows);
            }
        }.execute();
    }

    @Override
    public void handleFabClick() {
        new AddSubjectDialogFragment().setListener(this).show(getActivity().getSupportFragmentManager(), AddSubjectDialogFragment.class.getSimpleName());
    }

    @Override
    public void handleClearAllClick() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                ((MainActivity)getActivity()).getDatabase().subjectDao().nukeTable();
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
        final boolean visibility = adapter.checkAll();
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                ((MainActivity)getActivity()).getDatabase().subjectDao().updateVisibility(visibility);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                ((MainActivity)getActivity()).updateVisibility();
            }
        }.execute();
        ((MainActivity)getActivity()).updateVisibility();
    }

    @Override
    public void onItemAdded(final Subject item) {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                if (((MainActivity)getActivity()).getDatabase().subjectDao().getSubjectByName(item.name).size() == 0) {
                    item.id = ((MainActivity)getActivity()).getDatabase().subjectDao().insert(item);
                    return true;
                }
                else {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                if (isSuccessful) {
                    adapter.addItem(item);
                }
            }
        }.execute();
    }

    @Override
    public void onItemDeleted(final Subject item) {
        new AsyncTask<Void, Void, Subject>() {

            @Override
            protected Subject doInBackground(Void... voids) {
                ((MainActivity)getActivity()).getDatabase().subjectDao().delete(item);
                return item;
            }

            @Override
            protected void onPostExecute(Subject subject) {
                adapter.delete(subject);
                ((MainActivity)getActivity()).updatePages();
            }
        }.execute();
    }

    @Override
    public void onItemClicked(Subject item) {
        new EditSubjectDialogFragment().setListener(this).setItem(item).show(getActivity().getSupportFragmentManager(), EditSubjectDialogFragment.class.getSimpleName());
    }

    @Override
    public void onItemVisibilityChanged(final Subject item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                ((MainActivity)getActivity()).getDatabase().subjectDao().update(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                ((MainActivity)getActivity()).updateVisibility();
            }
        }.execute();
    }

    @Override
    public void invalidate() {
        loadItemsInBackground();
    }

    @Override
    public void onItemEdited(final Subject item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                if (((MainActivity)getActivity()).getDatabase().subjectDao().getSubjectByName(item.name).size() == 0) {
                    ((MainActivity) getActivity()).getDatabase().subjectDao().update(item);
                    return true;
                }
                else {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                if (isSuccessful) {
                ((MainActivity)getActivity()).updatePages();
                }
            }
        }.execute();
    }
}