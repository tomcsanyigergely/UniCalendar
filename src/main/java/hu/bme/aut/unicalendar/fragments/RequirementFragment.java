package hu.bme.aut.unicalendar.fragments;

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
import hu.bme.aut.unicalendar.adapter.RequirementAdapter;
import hu.bme.aut.unicalendar.adapter.SimpleFragmentPagerAdapter;
import hu.bme.aut.unicalendar.data.Requirement;

public class RequirementFragment extends Fragment implements
        MainActivity.ActionListener,
        AddRequirementDialogFragment.AddItemDialogListener,
        RequirementAdapter.ItemClickListener,
        SimpleFragmentPagerAdapter.Invalidatable,
        EditRequirementDialogFragment.EditItemDialogListener    {

    RequirementAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);
        adapter = new RequirementAdapter(this);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return layout;
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Requirement>>() {

            @Override
            protected List<Requirement> doInBackground(Void... voids) {
                return ((MainActivity)getActivity()).getDatabase().requirementDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Requirement> rows) {
                adapter.update(rows);
            }
        }.execute();
    }

    @Override
    public void handleFabClick() {
        new AddRequirementDialogFragment().setListener(this).show(getActivity().getSupportFragmentManager(), AddRequirementDialogFragment.class.getSimpleName());
    }

    @Override
    public void handleClearAllClick() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                ((MainActivity)getActivity()).getDatabase().requirementDao().nukeTable();
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
                ((MainActivity)getActivity()).getDatabase().requirementDao().updateVisibility(visibility);
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
    public void onItemAdded(final Requirement item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                if (((MainActivity)getActivity()).getDatabase().requirementDao().getRequirementByName(item.name).size() == 0) {
                    item.id = ((MainActivity)getActivity()).getDatabase().requirementDao().insert(item);
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
    public void onItemDeleted(final Requirement item) {
        new AsyncTask<Void, Void, Requirement>() {

            @Override
            protected Requirement doInBackground(Void... voids) {
                ((MainActivity)getActivity()).getDatabase().requirementDao().delete(item);
                return item;
            }

            @Override
            protected void onPostExecute(Requirement requirement) {
                adapter.delete(requirement);
                ((MainActivity)getActivity()).updatePages();
            }
        }.execute();
    }

    @Override
    public void onItemClicked(Requirement item) {
        new EditRequirementDialogFragment().setListener(this).setItem(item).show(getActivity().getSupportFragmentManager(), EditRequirementDialogFragment.class.getSimpleName());
    }

    @Override
    public void onItemVisibilityChanged(final Requirement item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                ((MainActivity)getActivity()).getDatabase().requirementDao().update(item);
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
    public void onItemEdited(final Requirement item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                if (((MainActivity)getActivity()).getDatabase().requirementDao().getRequirementByName(item.name).size() == 0)
                {
                    ((MainActivity)getActivity()).getDatabase().requirementDao().update(item);
                    return true;
                }
                else {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                if (isSuccessful) {
                    ((MainActivity) getActivity()).updatePages();
                }
            }
        }.execute();
    }
}
