package com.unvired.sapsample.activities;

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
import android.view.View;
import android.widget.Toast;

import com.unvired.database.DBException;
import com.unvired.database.IDataStructure;
import com.unvired.model.InfoMessage;
import com.unvired.sapsample.R;
import com.unvired.sapsample.adapter.PersonAdapter;
import com.unvired.sapsample.be.E_MAIL;
import com.unvired.sapsample.be.PERSON_HEADER;
import com.unvired.sapsample.util.Constants;
import com.unvired.sapsample.util.DBHelper;
import com.unvired.sapsample.util.PAHelper;
import com.unvired.sync.out.ISyncAppCallback;
import com.unvired.sync.response.ISyncResponse;
import com.unvired.sync.response.SyncBEResponse;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;


public class GetPersonActivity extends AppCompatActivity {

    private TextInputEditText personNum;

    private RecyclerView recyclerView;
    private PersonAdapter adapter;

    private String responseCode;
    private String responseText;

    private List<PERSON_HEADER> personHeaders = null;
    private List<E_MAIL> personEmails = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        personNum = (TextInputEditText) findViewById(R.id.number);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        AppCompatButton createButton = (AppCompatButton) findViewById(R.id.searchButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    PERSON_HEADER header = new PERSON_HEADER();

                    if (personNum.getText() == null || personNum.getText().toString().isEmpty()) {
//                        ((TextInputLayout) personNum.getParentForAccessibility()).setError("Please provide Person Number");
                        Toast.makeText(getApplicationContext(),"Please provide Person Number",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    header.setPERSNUMBER(Long.parseLong(personNum.getText().toString()));

                    downloadPerson(header);

                } catch (DBException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (personHeaders != null && !personHeaders.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage("Do you want to save results?")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for (PERSON_HEADER header : personHeaders) {
                                DBHelper.getInstance().insertOrUpdatePerson(header);
                            }

                            for (E_MAIL item : personEmails) {
                                DBHelper.getInstance().insertOrUpdateEmail(item);
                            }

                            finish();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .create().show();
        } else {
            finish();
        }

    }

    private void loadList() {
        adapter = new PersonAdapter(this, personHeaders, Constants.MODE_GET);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void downloadPerson(final PERSON_HEADER header) {
        personHeaders = new ArrayList<>();
        personEmails = new ArrayList<>();

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
                                }

                                if (responseText == null || responseText.trim().isEmpty()) {
                                    responseText = getResources().getString(R.string.personDownloadSuccess);
                                }

                                if (responseCode.equals(Constants.RESPONSE_CODE_SUCCESSFUL)) {
                                    Hashtable<String, Hashtable<IDataStructure, Vector<IDataStructure>>> dataBEs = syncBEResponse.getDataBEs();
                                    Hashtable<IDataStructure, Vector<IDataStructure>> tempCollectionOfHeaderAndItems = null;

                                    if (!dataBEs.isEmpty()) {

                                        Enumeration<String> beKeys = dataBEs.keys();

                                        if (beKeys.hasMoreElements()) {
                                            String customerBEName = beKeys.nextElement();
                                            tempCollectionOfHeaderAndItems = dataBEs.get(customerBEName);

                                            Enumeration<IDataStructure> contactHeaderKeys = tempCollectionOfHeaderAndItems.keys();

                                            while (contactHeaderKeys.hasMoreElements()) {
                                                PERSON_HEADER contactHeader = (PERSON_HEADER) contactHeaderKeys.nextElement();
                                                personHeaders.add(contactHeader);

                                                Vector<IDataStructure> emailVector = tempCollectionOfHeaderAndItems.get(contactHeader);

                                                if (emailVector != null && !emailVector.isEmpty()) {
                                                    for (IDataStructure structure : emailVector) {
                                                        personEmails.add((E_MAIL) structure);
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
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
                                showInfo(responseText);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showInfo(responseText);
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
                PAHelper.getPerson(header, callback);
            }
        }).start();
    }

    private void showInfo(String msg) {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loadList();
                    }
                }).create().show();
    }
}
