package hu.bme.aut.unicalendar.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import hu.bme.aut.unicalendar.R;

public class SelectDateDialogFragment extends DialogFragment {

    private DatePicker datePicker;

    public interface SelectDateDialogListener {
        void onDateSelected(Date date);
    }

    SelectDateDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.select_date)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, datePicker.getYear());
                        calendar.set(Calendar.MONTH, datePicker.getMonth());
                        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                        listener.onDateSelected(calendar.getTime());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    public DialogFragment setListener(SelectDateDialogListener listener) {
        this.listener = listener;
        return this;
    }

    private View getContentView() {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.fragment_select_date_dialog, null);
        datePicker = layout.findViewById(R.id.SelectDatePicker);
        return layout;
    }
}
