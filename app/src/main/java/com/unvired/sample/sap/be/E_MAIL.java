package com.unvired.sample.sap.be;

import com.unvired.database.DBException;
import com.unvired.model.DataStructure;

/*
This class is part of the BE "PERSON".
*/
public class E_MAIL extends DataStructure {

    public static final String TABLE_NAME = "E_MAIL";

    // Client
    public static final String FIELD_MANDT = "MANDT";

    // Person Number (Sample Application)
    public static final String FIELD_PERSNUMBER = "PERSNUMBER";

    // Seqeunce Number (Sample Application)
    public static final String FIELD_SEQNO_E_MAIL = "SEQNO_E_MAIL";

    // E-mail Address (Sample Application)
    public static final String FIELD_E_ADDR = "E_ADDR";

    // E-mail Address Description (Sample Application)
    public static final String FIELD_E_ADDR_TEXT = "E_ADDR_TEXT";

    public E_MAIL() throws DBException {
        super(TABLE_NAME, false);
    }

    public String getMANDT() {
        return (String) getField(FIELD_MANDT);
    }

    public void setMANDT(String value) {
        setField(FIELD_MANDT, value);
    }

    public Long getPERSNUMBER() {
        return (Long) getField(FIELD_PERSNUMBER);
    }

    public void setPERSNUMBER(Long value) {
        setField(FIELD_PERSNUMBER, value);
    }

    public Long getSEQNO_E_MAIL() {
        return (Long) getField(FIELD_SEQNO_E_MAIL);
    }

    public void setSEQNO_E_MAIL(Long value) {
        setField(FIELD_SEQNO_E_MAIL, value);
    }

    public String getE_ADDR() {
        return (String) getField(FIELD_E_ADDR);
    }

    public void setE_ADDR(String value) {
        setField(FIELD_E_ADDR, value);
    }

    public String getE_ADDR_TEXT() {
        return (String) getField(FIELD_E_ADDR_TEXT);
    }

    public void setE_ADDR_TEXT(String value) {
        setField(FIELD_E_ADDR_TEXT, value);
    }
}