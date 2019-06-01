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
import hu.bme.aut.unicalendar.data.Requirement;

public class EditRequirementDialogFragment extends DialogFragment {

    private Requirement item;

    private EditText etRequirementName;

    public interface EditItemDialogListener {
        void onItemEdited(Requirement item);
    }

    private EditItemDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.edit_requirement)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onItemEdited(getItem());
                        }
                    }

                    private boolean isValid() {
                        return etRequirementName.getText().length() > 0;
                    }

                    private Requirement getItem() {
                        item.name = etRequirementName.getText().toString();
                        return item;
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_requirement_dialog, null);
        etRequirementName = contentView.findViewById(R.id.etRequirementName);
        etRequirementName.setText(item.name);
        return contentView;
    }

    public EditRequirementDialogFragment setListener(EditItemDialogListener listener) {
        this.listener = listener;
        return this;
    }

    public EditRequirementDialogFragment setItem(Requirement item) {
        this.item = item;
        return this;
    }
}
