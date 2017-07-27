package com.unvired.sample.sap.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
import com.unvired.utils.Context;

import java.util.List;
import java.util.Vector;

public class PersonDetailActivity extends AppCompatActivity {

    private TextView name;
    private TextView number;
    private TextView gender;
    private TextView profession;

    private TextView dob;
    private TextView height;
    private TextView weight;

    private PERSON_HEADER personHeader;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        personHeader = (PERSON_HEADER) Context.getInstance().get("PERSON_HEADER");

        name = (TextView) findViewById(R.id.name);
        number = (TextView) findViewById(R.id.number);
        gender = (TextView) findViewById(R.id.gender);
        profession = (TextView) findViewById(R.id.profession);
        dob = (TextView) findViewById(R.id.dob);
        height = (TextView) findViewById(R.id.height);
        weight = (TextView) findViewById(R.id.weight);

        recyclerView = (RecyclerView) findViewById(R.id.emailIdRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        loadInfo();
    }

    private void loadInfo() {

        if (personHeader == null) {
            return;
        }

        name.setText(Utils.getPersonName(personHeader));

        if (personHeader.getPERSNUMBER() != null) {
            number.setText(personHeader.getPERSNUMBER().toString());
        }

        if (personHeader.getSEX() != null) {
            gender.setText(personHeader.getSEX());
        }

        if (personHeader.getPROFESSION() != null) {
            profession.setText(personHeader.getPROFESSION());
        }

        if (personHeader.getBIRTHDAY() != null) {
            dob.setText(personHeader.getBIRTHDAY());
        }

        if (personHeader.getHEIGHT() != null) {
            int value = personHeader.getHEIGHT().intValue();
            height.setText(value + " Cm");
        }

        if (personHeader.getWEIGHT() != null) {
            int value = personHeader.getWEIGHT().intValue();
            weight.setText(value + " Kg");
        }

        List<E_MAIL> eMails = DBHelper.getInstance().getEMails(personHeader);
        recyclerView.setAdapter(new EmailAdapter(eMails));
    }

}
