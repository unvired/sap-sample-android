package com.unvired.sample.sap.util;

import com.unvired.database.DBException;
import com.unvired.database.IDataStructure;
import com.unvired.exception.ApplicationException;
import com.unvired.sample.sap.be.PERSON_HEADER;
import com.unvired.sync.SyncConstants;
import com.unvired.sync.SyncEngine;
import com.unvired.sync.out.ISyncAppCallback;

/**
 * Created by nishchith on 21/07/17.
 */

/*
* Process Agent(PA) Helper
*/

public class PAHelper {

    public static void createPerson(PERSON_HEADER header, ISyncAppCallback callback) {

        try {
            SyncEngine.getInstance().submitInSyncMode(SyncConstants.MESSAGE_REQUEST_TYPE.RQST, header, "", Constants.PA_CREATE_PERSON, true, callback);
        } catch (ApplicationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getPerson(PERSON_HEADER header, ISyncAppCallback callback) {

        try {
            SyncEngine.getInstance().submitInSyncMode(SyncConstants.MESSAGE_REQUEST_TYPE.PULL, header, "", Constants.PA_GET_PERSON, false, callback);
        } catch (ApplicationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}