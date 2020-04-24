package com.unvired.sapsample.adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unvired.sapsample.R;
import com.unvired.sapsample.activities.PersonDetailActivity;
import com.unvired.sapsample.be.PERSON_HEADER;
import com.unvired.sapsample.util.Constants;
import com.unvired.sapsample.util.Utils;
import com.unvired.utils.Context;

import java.util.ArrayList;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private List<PERSON_HEADER> person_headers;
    private AppCompatActivity activity;
    private int MODE;

    private List<String> sectionHeaders;
    private List<Integer> headersPosition;


    public PersonAdapter(AppCompatActivity activity, List<PERSON_HEADER> contact_headers, int mode) {
        this.person_headers = contact_headers;
        this.activity = activity;
        MODE = mode;
        setHeaders();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final PERSON_HEADER personHeader = person_headers.get(position);

        if (personHeader != null) {
            viewHolder.name.setText(Utils.getPersonName(personHeader));
            viewHolder.number.setText(personHeader.getPERSNUMBER().toString());

            viewHolder.sectionHeader.setVisibility(View.GONE);

            if (MODE == Constants.MODE_HOME) {
                viewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context.getInstance().add("PERSON_HEADER", personHeader);
                        activity.startActivity(new Intent(activity, PersonDetailActivity.class));

                    }
                });

                if (viewHolder.sectionHeader != null) {
                    if (position == 0) {
                        viewHolder.sectionHeader.setText(sectionHeaders.get(0));
                        viewHolder.sectionHeader.setVisibility(View.VISIBLE);
                    } else if (headersPosition.contains(position)) {
                        viewHolder.sectionHeader.setText(sectionHeaders.get(position));
                        viewHolder.sectionHeader.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.sectionHeader.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (person_headers != null && person_headers.size() > 0) {
            return person_headers.size();
        } else
            return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView sectionHeader;
        public TextView name;
        public TextView number;
        public LinearLayout rootLayout;

        ViewHolder(View v) {
            super(v);
            sectionHeader = (TextView) v.findViewById(R.id.sectionHeader);
            name = (TextView) v.findViewById(R.id.name);
            number = (TextView) v.findViewById(R.id.number);
            rootLayout = (LinearLayout) v.findViewById(R.id.rootLayout);
        }
    }

    private void setHeaders() {
        sectionHeaders = new ArrayList<>();
        headersPosition = new ArrayList<>();
        sectionHeaders.clear();
        headersPosition.clear();

        if (person_headers != null && person_headers.size() > 0) {
            for (int i = 0; i < person_headers.size(); i++) {
                String current = "";

                if (!Utils.getPersonName(person_headers.get(i)).isEmpty()) {
                    current = String.valueOf(Utils.getPersonName(person_headers.get(i)).charAt(0));
                }

                if (i == 0) {
                    headersPosition.add(i);
                } else if (!current.equalsIgnoreCase(sectionHeaders.get(sectionHeaders.size() - 1))) {
                    headersPosition.add(i);
                }
                sectionHeaders.add(current.toUpperCase());
            }
        }
    }
}