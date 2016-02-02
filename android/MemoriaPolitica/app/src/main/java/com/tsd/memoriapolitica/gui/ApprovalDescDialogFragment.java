package com.tsd.memoriapolitica.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.tsd.memoriapolitica.R;
import com.tsd.memoriapolitica.domain.CitizenNotebook;
import com.tsd.memoriapolitica.domain.Constants;
import com.tsd.memoriapolitica.domain.PoliticianClass;
import com.tsd.memoriapolitica.domain.PoliticianClassification;
import com.tsd.memoriapolitica.gui.notebook.Approval;

/**
 * Created by taciosd on 2/2/16.
 */
public class ApprovalDescDialogFragment extends DialogFragment {

    private PoliticianClassification polClassification;
    private Approval approval;
    private NotifyDialogFragmentClicked mListener;

    public interface NotifyDialogFragmentClicked {
        void onDialogPositiveClick(PoliticianClassification polClassification);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        polClassification = (PoliticianClassification) getArguments().getSerializable(Constants.POLITICIAN_KEY);
        approval = (Approval) getArguments().getSerializable(Constants.APPROVAL_KEY);

        String approvalStrKey;
        if (polClassification.getApproval().equals(Approval.APPROVED)) {
            approvalStrKey = "Aprovar";
        }
        else {
            approvalStrKey = "Reprovar";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.politician_approval_description)
                .setTitle("Confirmação de opinião")
                .setMessage("Se quiser informe o motivo para você " + approvalStrKey.toLowerCase() + " esse político.")
                .setPositiveButton(approvalStrKey, onConfirm)
                .setNegativeButton("Cancelar", null);

        return builder.create();
    }

    private DialogInterface.OnClickListener onConfirm = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            polClassification.setApproval(approval);
            mListener.onDialogPositiveClick(polClassification);
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mListener = (NotifyDialogFragmentClicked) activity;
    }
}
