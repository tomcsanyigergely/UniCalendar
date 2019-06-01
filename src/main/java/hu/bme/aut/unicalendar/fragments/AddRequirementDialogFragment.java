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

public class AddRequirementDialogFragment extends DialogFragment {

    private EditText etRequirementName;

    public interface AddItemDialogListener {
        void onItemAdded(Requirement item);
    }

    private AddItemDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.add_requirement)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onItemAdded(getItem());
                        }
                    }

                    private boolean isValid() {
                        return etRequirementName.getText().length() > 0;
                    }

                    private Requirement getItem() {
                        Requirement item = new Requirement();
                        item.name = etRequirementName.getText().toString();
                        item.visible = true;
                        return item;
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_requirement_dialog, null);
        etRequirementName = contentView.findViewById(R.id.etRequirementName);
        return contentView;
    }

    public AddRequirementDialogFragment setListener(AddItemDialogListener listener) {
        this.listener = listener;
        return this;
    }
}
