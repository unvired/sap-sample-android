package com.unvired.sample.sap.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unvired.sample.sap.R;
import com.unvired.sample.sap.be.E_MAIL;

import java.util.List;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {

    private List<E_MAIL> e_mailList;

    public EmailAdapter(List<E_MAIL> contact_headers) {
        this.e_mailList = contact_headers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final E_MAIL e_mail = e_mailList.get(position);

        if (e_mail != null) {
            viewHolder.emailId.setText(e_mail.getE_ADDR());
            viewHolder.emailDesc.setText(e_mail.getE_ADDR_TEXT());
        }
    }

    @Override
    public int getItemCount() {
        if (e_mailList != null && e_mailList.size() > 0) {
            return e_mailList.size();
        } else
            return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView emailId;
        public TextView emailDesc;

        ViewHolder(View v) {
            super(v);
            emailId = (TextView) v.findViewById(R.id.emailId);
            emailDesc = (TextView) v.findViewById(R.id.emailDesc);
        }
    }
}