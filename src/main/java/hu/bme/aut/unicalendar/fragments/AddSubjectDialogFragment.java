package hu.bme.aut.unicalendar.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import hu.bme.aut.unicalendar.R;
import hu.bme.aut.unicalendar.data.Subject;

public class AddSubjectDialogFragment extends DialogFragment {

    private EditText etSubjectName;

    public interface AddItemDialogListener {
        void onItemAdded(Subject item);
    }

    private AddItemDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.add_subject)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onItemAdded(getItem());
                        }
                    }

                    private boolean isValid() {
                        return etSubjectName.getText().length() > 0;
                    }

                    private Subject getItem() {
                        Subject item = new Subject();
                        item.name = etSubjectName.getText().toString();
                        item.visible = true;
                        return item;
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_subject_dialog, null);
        etSubjectName = contentView.findViewById(R.id.etSubjectName);
        return contentView;
    }

    public AddSubjectDialogFragment setListener(AddItemDialogListener listener) {
        this.listener = listener;
        return this;
    }
}
