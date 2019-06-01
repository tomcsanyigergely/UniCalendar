package hu.bme.aut.unicalendar.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hu.bme.aut.unicalendar.MainActivity;
import hu.bme.aut.unicalendar.R;
import hu.bme.aut.unicalendar.data.Event;

public class AddEventDialogFragment extends DialogFragment {
    private Spinner eventSubjectSpinner;
    private Spinner eventRequirementSpinner;
    private DatePicker eventDatePicker;
    private EditText etEventNotification;

    private AddItemDialogListener listener;

    public interface AddItemDialogListener {
        void onItemAdded(Event item);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.add_event)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onItemAdded(getItem());
                        }
                    }

                    private boolean isValid() {
                        return eventSubjectSpinner.getSelectedItem() != null &&
                                eventRequirementSpinner.getSelectedItem() != null &&
                                etEventNotification.getText().toString().length() > 0 &&
                                Long.parseLong(etEventNotification.getText().toString()) >= 0;
                    }

                    private Event getItem() {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, eventDatePicker.getYear());
                        calendar.set(Calendar.MONTH, eventDatePicker.getMonth());
                        calendar.set(Calendar.DAY_OF_MONTH, eventDatePicker.getDayOfMonth());
                        Event item = new Event();
                        item.subject = eventSubjectSpinner.getSelectedItem().toString();
                        item.requirement = eventRequirementSpinner.getSelectedItem().toString();
                        item.date = calendar.getTime();
                        item.notification = Long.parseLong(etEventNotification.getText().toString());
                        return item;
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    public DialogFragment setListener(AddItemDialogListener listener) {
        this.listener = listener;
        return this;
    }

    private View getContentView() {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_event_dialog, null);
        eventSubjectSpinner = layout.findViewById(R.id.EventSubjectSpinner);
        new AsyncTask<Void, Void, List<String>>() {

            @Override
            protected List<String> doInBackground(Void... voids) {
                return ((MainActivity)getActivity()).getDatabase().subjectDao().getSubjectNames();
            }

            @Override
            protected void onPostExecute(List<String> rows) {
                eventSubjectSpinner.setAdapter(new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        rows
                ));
            }
        }.execute();

        eventRequirementSpinner = layout.findViewById(R.id.EventRequirementSpinner);
        new AsyncTask<Void, Void, List<String>>() {

            @Override
            protected List<String> doInBackground(Void... voids) {
                return ((MainActivity)getActivity()).getDatabase().requirementDao().getRequirementNames();
            }

            @Override
            protected void onPostExecute(List<String> rows) {
                eventRequirementSpinner.setAdapter(new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        rows
                ));
            }
        }.execute();

        eventDatePicker = layout.findViewById(R.id.EventDatePicker);
        etEventNotification = layout.findViewById(R.id.etEventNotification);
        etEventNotification.setText("0");
        return layout;
    }
}
