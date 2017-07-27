package com.unvired.sample.sap.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.unvired.database.DBException;
import com.unvired.model.InfoMessage;
import com.unvired.sample.sap.R;
import com.unvired.sample.sap.adapter.EmailAdapter;
import com.unvired.sample.sap.be.E_MAIL;
import com.unvired.sample.sap.be.PERSON_HEADER;
import com.unvired.sample.sap.util.Constants;
import com.unvired.sample.sap.util.DBHelper;
import com.unvired.sample.sap.util.PAHelper;
import com.unvired.sample.sap.util.Utils;
import com.unvired.sync.out.ISyncAppCallback;
import com.unvired.sync.response.ISyncResponse;
import com.unvired.sync.response.SyncBEResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CreatePersonActivity extends AppCompatActivity {

    private TextInputEditText fName;
    private TextInputEditText lName;
    private TextInputEditText gender;
    private TextInputEditText profession;
    private TextInputEditText dob;
    private TextInputEditText height;
    private TextInputEditText weight;

    private String responseCode;
    private String responseText;

    private List<E_MAIL> e_mailList = new ArrayList<>();

    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private EmailAdapter emailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fName = (TextInputEditText) findViewById(R.id.fName);
        lName = (TextInputEditText) findViewById(R.id.lName);
        gender = (TextInputEditText) findViewById(R.id.gender);
        profession = (TextInputEditText) findViewById(R.id.profession);
        dob = (TextInputEditText) findViewById(R.id.dob);
        height = (TextInputEditText) findViewById(R.id.height);
        weight = (TextInputEditText) findViewById(R.id.weight);

        recyclerView = (RecyclerView) findViewById(R.id.emailIdRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        emailAdapter = new EmailAdapter(e_mailList);
        recyclerView.setAdapter(emailAdapter);

        AppCompatButton createButton = (AppCompatButton) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isValid()) {
                    return;
                }

                createPerson();
            }
        });

        AppCompatButton addEmailButton = (AppCompatButton) findViewById(R.id.addEmailButton);
        addEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEmail();
            }
        });
    }

    private void inputEmail() {

        if (e_mailList == null) {
            e_mailList = new ArrayList<>();
        }

        LayoutInflater mInflater = (LayoutInflater) getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View root = mInflater.inflate(R.layout.email_input_layout, null);
        final TextInputEditText emailIdEditText = (TextInputEditText) root.findViewById(R.id.emailId);

        new AlertDialog.Builder(this)
                .setView(root)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (emailIdEditText.getText() == null || emailIdEditText.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please provide Email Id", Toast.LENGTH_LONG).show();
                            return;
                        }

                        try {
                            E_MAIL e_mail = new E_MAIL();
                            e_mail.setMANDT("800");
                            e_mail.setE_ADDR(emailIdEditText.getText().toString());

                            e_mailList.add(e_mail);
                            emailAdapter.notifyDataSetChanged();

                        } catch (DBException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }

    private void createPerson() {
        try {
            PERSON_HEADER header = new PERSON_HEADER();
            if (fName.getText() != null && !fName.getText().toString().isEmpty()) {
                header.setFIRST_NAME(fName.getText().toString());
            }

            if (lName.getText() != null && !lName.getText().toString().isEmpty()) {
                header.setLAST_NAME(lName.getText().toString());
            }

            if (gender.getText() != null && !gender.getText().toString().isEmpty()) {
                header.setSEX(gender.getText().toString());
            }

            if (dob.getText() != null && !dob.getText().toString().isEmpty()) {
                header.setBIRTHDAY(dob.getText().toString());
            }

            if (profession.getText() != null && !profession.getText().toString().isEmpty()) {
                header.setPROFESSION(profession.getText().toString());
            }

            if (height.getText() != null && !height.getText().toString().isEmpty()) {
                header.setHEIGHT(Double.parseDouble(height.getText().toString()));
            }

            if (weight.getText() != null && !weight.getText().toString().isEmpty()) {
                header.setWEIGHT(Double.parseDouble(weight.getText().toString()));
            }

            saveToDB(header);

            callCreate(header);

        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    private void callCreate(final PERSON_HEADER header) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait..");
        }

        progressDialog.show();

        final ISyncAppCallback callback = new ISyncAppCallback() {
            @Override
            public void onResponse(ISyncResponse iSyncResponse) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                SyncBEResponse syncBEResponse;
                responseText = null;

                if (iSyncResponse == null) {
                    responseCode = Constants.RESPONSE_CODE_ERROR;
                    responseText = getResources().getString(R.string.invalidResponse);
                } else {

                    switch (iSyncResponse.getResponseStatus()) {
                        case SUCCESS:

                            if (iSyncResponse instanceof SyncBEResponse) {

                                syncBEResponse = (SyncBEResponse) iSyncResponse;

                                responseCode = Constants.RESPONSE_CODE_SUCCESSFUL;
                                Vector<InfoMessage> infoMessages = syncBEResponse.getInfoMessages();

                                if (infoMessages != null && infoMessages.size() > 0) {
                                    StringBuilder infoMsgs = new StringBuilder();

                                    for (int i = 0; i < infoMessages.size(); i++) {
                                        responseCode = infoMessages.get(i).getCategory().equals(InfoMessage.CATEGORY_SUCCESS) ? Constants.RESPONSE_CODE_SUCCESSFUL : Constants.RESPONSE_CODE_ERROR;

                                        if (infoMessages.get(i).getMessage() != null && !infoMessages.get(i).getMessage().equals("")) {
                                            infoMsgs.append(infoMessages.get(i).getMessage() + "\n");
                                        }
                                    }

                                    responseText = infoMsgs.toString();

                                    header.setPERSNUMBER(Utils.getPersonNumber(responseText));
                                    DBHelper.getInstance().insertOrUpdatePerson(header);

                                    for (E_MAIL e_mail : e_mailList) {
                                        e_mail.setPERSNUMBER(header.getPERSNUMBER());
                                        DBHelper.getInstance().insertOrUpdateEmail(e_mail);
                                    }

                                }

                                if (responseText.trim().isEmpty())
                                    responseText = getResources().getString(R.string.personCreateSuccess);
                            }
                            break;

                        case FAILURE:
                            responseCode = Constants.RESPONSE_CODE_ERROR;
                            if (iSyncResponse instanceof SyncBEResponse) {
                                syncBEResponse = (SyncBEResponse) iSyncResponse;
                                responseText = syncBEResponse.getErrorMessage();

                                if (syncBEResponse.getErrorMessage().contains(getResources().getString(R.string.invalidResponse))) {
                                    responseText = getResources().getString(R.string.invalidResponse);
                                } else {
                                    responseText = syncBEResponse.getErrorMessage();
                                }

                                Vector<InfoMessage> infoMessages = syncBEResponse.getInfoMessages();

                                if (infoMessages != null && infoMessages.size() > 0) {
                                    StringBuilder infoMsgs = new StringBuilder();

                                    for (int i = 0; i < infoMessages.size(); i++) {
                                        if (infoMessages.get(i).getMessage() != null && !infoMessages.get(i).getMessage().equals("")) {
                                            infoMsgs.append(infoMessages.get(i).getMessage() + "\n");
                                        }
                                    }

                                    responseText = infoMsgs.toString();
                                }

                                if (responseText.trim().isEmpty())
                                    responseText = getResources().getString(R.string.invalidResponse);

                            } else {
                                responseText = getResources().getString(R.string.invalidResponse);
                            }
                            break;
                    }

                    if (responseCode != null && responseCode.equalsIgnoreCase(Constants.RESPONSE_CODE_ERROR)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showInfo(true, responseText);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showInfo(false, responseText);
                            }
                        });
                    }
                }
            }
        };

        /*
        * Always execute Process Agent(PA) in thread
        */
        new Thread(new Runnable() {
            @Override
            public void run() {
                PAHelper.createPerson(header, callback);
            }
        }).start();

    }

    private void showInfo(final boolean error, String msg) {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!error) {
                            finish();
                        }
                    }
                }).create().show();
    }

    private boolean isValid() {
        if (lName.getText() == null || lName.getText().toString().isEmpty()) {
            ((TextInputLayout) lName.getParentForAccessibility()).setError("Last name is mandatory");
            lName.findFocus();
            lName.requestFocus();
            return false;
        }

        if ((gender.getText() == null || gender.getText().toString().isEmpty())) {
            ((TextInputLayout) gender.getParentForAccessibility()).setError("Please specify gender");
            gender.findFocus();
            gender.requestFocus();
            return false;
        }
        return true;
    }

    private void saveToDB(PERSON_HEADER personHeader) {
        personHeader.setPERSNUMBER(0L);
        personHeader.setMANDT("800");
        DBHelper.getInstance().insertOrUpdatePerson(personHeader);

        for (int i = 0; i < e_mailList.size(); i++) {
            E_MAIL e_mail = e_mailList.get(i);
            e_mail.setPERSNUMBER(personHeader.getPERSNUMBER());
            e_mail.setSEQNO_E_MAIL(Long.valueOf(i));
            e_mail.setFid(personHeader.getLid());

            DBHelper.getInstance().insertOrUpdateEmail(e_mail);
        }
    }
}
