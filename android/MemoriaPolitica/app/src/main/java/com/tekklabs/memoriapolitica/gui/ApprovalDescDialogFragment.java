package com.tekklabs.memoriapolitica.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.tekklabs.memoriapolitica.R;
import com.tekklabs.memoriapolitica.domain.Constants;
import com.tekklabs.memoriapolitica.domain.PoliticianClassification;
import com.tekklabs.memoriapolitica.gui.notebook.Approval;

/**
 * Created by taciosd on 2/2/16.
 */
public class ApprovalDescDialogFragment extends DialogFragment {

    private PoliticianClassification polClassification;
    private Approval approval;
    private NotifyDialogFragmentClicked mListener;
    private EditText reasonEdit;

    public interface NotifyDialogFragmentClicked {
        void onDialogClick(Approval approval, PoliticianClassification polClassification);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        polClassification = (PoliticianClassification) getArguments().getSerializable(Constants.POLITICIAN_KEY);
        approval = (Approval) getArguments().getSerializable(Constants.APPROVAL_KEY);

        View view = View.inflate(getActivity(), R.layout.politician_approval_description, null);
        reasonEdit = (EditText) view.findViewById(R.id.dlg_politician_approval_reason);
        reasonEdit.setText(polClassification.getReason());

        String approvalStrKey;
        if (approval.equals(Approval.APPROVED)) {
            approvalStrKey = "Aprovar";
        }
        else if (approval.equals(Approval.REPROVED)) {
            approvalStrKey = "Reprovar";
        }
        else {
            approvalStrKey = "";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle("Confirmação de opinião")
                .setMessage("Se quiser informe o motivo para você " + approvalStrKey.toLowerCase() + " esse político.")
                .setPositiveButton(approvalStrKey, onConfirm);
        Dialog dlg = builder.create();
        setCancelable(false);

        return dlg;
    }

    private DialogInterface.OnClickListener onConfirm = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            polClassification.setApproval(approval);
            polClassification.setApprovalReason(reasonEdit.getText().toString());
            mListener.onDialogClick(approval, polClassification);
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mListener = (NotifyDialogFragmentClicked) activity;
    }
}
