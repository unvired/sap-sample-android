package com.unvired.sapsample.util;

import com.unvired.exception.ApplicationException;
import com.unvired.logger.Logger;
import com.unvired.sapsample.be.PERSON_HEADER;
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
            Logger.e(e.getMessage());
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }

    public static void getPerson(PERSON_HEADER header, ISyncAppCallback callback) {

        try {
            SyncEngine.getInstance().submitInSyncMode(SyncConstants.MESSAGE_REQUEST_TYPE.PULL, header, "", Constants.PA_GET_PERSON, false, callback);
        } catch (ApplicationException e) {
            Logger.e(e.getMessage());
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }
}
