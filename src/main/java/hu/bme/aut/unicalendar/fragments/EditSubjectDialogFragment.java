package hu.bme.aut.unicalendar.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import hu.bme.aut.unicalendar.R;
import hu.bme.aut.unicalendar.data.Subject;

public class EditSubjectDialogFragment extends DialogFragment {

    private Subject item;

    private EditText etSubjectName;

    public interface EditItemDialogListener {
        void onItemEdited(Subject item);
    }

    private EditItemDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.edit_subject)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onItemEdited(getItem());
                        }
                    }

                    private boolean isValid() {
                        return etSubjectName.getText().length() > 0;
                    }

                    private Subject getItem() {
                        item.name = etSubjectName.getText().toString();
                        return item;
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_subject_dialog, null);
        etSubjectName = contentView.findViewById(R.id.etSubjectName);
        etSubjectName.setText(item.name);
        return contentView;
    }

    public EditSubjectDialogFragment setListener(EditItemDialogListener listener) {
        this.listener = listener;
        return this;
    }

    public EditSubjectDialogFragment setItem(Subject item) {
        this.item = item;
        return this;
    }
}
