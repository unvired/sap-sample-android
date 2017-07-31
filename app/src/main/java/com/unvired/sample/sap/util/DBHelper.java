package com.unvired.sample.sap.util;

import com.unvired.core.ApplicationManager;
import com.unvired.database.DBException;
import com.unvired.database.IDataManager;
import com.unvired.database.IDataStructure;
import com.unvired.logger.Logger;
import com.unvired.sample.sap.be.E_MAIL;
import com.unvired.sample.sap.be.PERSON_HEADER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nishchith on 21/07/17.
 */

public class DBHelper {

    private IDataManager iDataManager = null;
    private static DBHelper dbHelper = null;

    DBHelper() {
        try {
            iDataManager = ApplicationManager.getInstance().getDataManager();
        } catch (DBException e) {
            Logger.e(e.getMessage());
        }
    }

    public static DBHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new DBHelper();
        }

        return dbHelper;
    }

    public void insertOrUpdatePerson(PERSON_HEADER header) {
        try {
            iDataManager.insertOrUpdateBasedOnGID(header);
        } catch (DBException e) {
            Logger.e(e.getMessage());
        }
    }

    public void insertOrUpdateEmail(E_MAIL header) {
        try {
            iDataManager.insertOrUpdateBasedOnGID(header);
        } catch (DBException e) {
            Logger.e(e.getMessage());
        }
    }

    public List<PERSON_HEADER> getPersons() {

        List<PERSON_HEADER> contactHeaders = new ArrayList<>();

        try {
            IDataStructure[] structures = iDataManager.get(PERSON_HEADER.TABLE_NAME);

            if (structures != null && structures.length > 0) {
                for (IDataStructure structure : structures) {
                    contactHeaders.add((PERSON_HEADER) structure);
                }
            }
        } catch (DBException e) {
            Logger.e(e.getMessage());
            return null;
        }

        Collections.sort(contactHeaders, new Comparator<PERSON_HEADER>() {
            @Override
            public int compare(PERSON_HEADER top, PERSON_HEADER next) {
                return Utils.getPersonName(top).compareToIgnoreCase(Utils.getPersonName(next));
            }
        });

        return contactHeaders;
    }

    public List<E_MAIL> getEMails(PERSON_HEADER personHeader) {

        List<E_MAIL> emails = new ArrayList<>();

        try {
            IDataStructure[] structures = iDataManager.getChildren(E_MAIL.TABLE_NAME, personHeader);

            if (structures != null && structures.length > 0) {
                for (IDataStructure structure : structures) {
                    emails.add((E_MAIL) structure);
                }
            }
        } catch (DBException e) {
            Logger.e(e.getMessage());
            return null;
        }

        return emails;
    }
}
