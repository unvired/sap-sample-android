package com.unvired.sapsample.be;

import com.unvired.database.DBException;
import com.unvired.model.DataStructure;

/*
This class is part of the BE "PERSON".
*/
public class PERSON_HEADER extends DataStructure {

    public static final String TABLE_NAME = "PERSON_HEADER";

    // Client
    public static final String FIELD_MANDT = "MANDT";

    // Person Number (Sample Application)
    public static final String FIELD_PERSNUMBER = "PERSNUMBER";

    // First Name (Sample Application)
    public static final String FIELD_FIRST_NAME = "FIRST_NAME";

    // Last Name (Sample Application)
    public static final String FIELD_LAST_NAME = "LAST_NAME";

    // Profession (Sample Application)
    public static final String FIELD_PROFESSION = "PROFESSION";

    // Gender (Sample Application)
    public static final String FIELD_SEX = "SEX";

    // Birthday (Sample Application)
    public static final String FIELD_BIRTHDAY = "BIRTHDAY";

    // Weight (Sample Application)
    public static final String FIELD_WEIGHT = "WEIGHT";

    // Height (Sample Application)
    public static final String FIELD_HEIGHT = "HEIGHT";

    // Category1 (Sample Application)
    public static final String FIELD_CATEGORY1 = "CATEGORY1";

    // Category2 (Sample Application)
    public static final String FIELD_CATEGORY2 = "CATEGORY2";

    // Created on
    public static final String FIELD_CREDAT = "CREDAT";

    // Created by
    public static final String FIELD_CRENAM = "CRENAM";

    // Create Time
    public static final String FIELD_CRETIM = "CRETIM";

    // Last Changed on
    public static final String FIELD_CHGDAT = "CHGDAT";

    // Last Changed by
    public static final String FIELD_CHGNAM = "CHGNAM";

    // Last Changed at
    public static final String FIELD_CHGTIM = "CHGTIM";

    public PERSON_HEADER() throws DBException {
        super(TABLE_NAME, true);
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

    public String getFIRST_NAME() {
        return (String) getField(FIELD_FIRST_NAME);
    }

    public void setFIRST_NAME(String value) {
        setField(FIELD_FIRST_NAME, value);
    }

    public String getLAST_NAME() {
        return (String) getField(FIELD_LAST_NAME);
    }

    public void setLAST_NAME(String value) {
        setField(FIELD_LAST_NAME, value);
    }

    public String getPROFESSION() {
        return (String) getField(FIELD_PROFESSION);
    }

    public void setPROFESSION(String value) {
        setField(FIELD_PROFESSION, value);
    }

    public String getSEX() {
        return (String) getField(FIELD_SEX);
    }

    public void setSEX(String value) {
        setField(FIELD_SEX, value);
    }

    public String getBIRTHDAY() {
        return (String) getField(FIELD_BIRTHDAY);
    }

    public void setBIRTHDAY(String value) {
        setField(FIELD_BIRTHDAY, value);
    }

    public Double getWEIGHT() {
        return (Double) getField(FIELD_WEIGHT);
    }

    public void setWEIGHT(Double value) {
        setField(FIELD_WEIGHT, value);
    }

    public Double getHEIGHT() {
        return (Double) getField(FIELD_HEIGHT);
    }

    public void setHEIGHT(Double value) {
        setField(FIELD_HEIGHT, value);
    }

    public Long getCATEGORY1() {
        return (Long) getField(FIELD_CATEGORY1);
    }

    public void setCATEGORY1(Long value) {
        setField(FIELD_CATEGORY1, value);
    }

    public String getCATEGORY2() {
        return (String) getField(FIELD_CATEGORY2);
    }

    public void setCATEGORY2(String value) {
        setField(FIELD_CATEGORY2, value);
    }

    public String getCREDAT() {
        return (String) getField(FIELD_CREDAT);
    }

    public void setCREDAT(String value) {
        setField(FIELD_CREDAT, value);
    }

    public String getCRENAM() {
        return (String) getField(FIELD_CRENAM);
    }

    public void setCRENAM(String value) {
        setField(FIELD_CRENAM, value);
    }

    public String getCRETIM() {
        return (String) getField(FIELD_CRETIM);
    }

    public void setCRETIM(String value) {
        setField(FIELD_CRETIM, value);
    }

    public String getCHGDAT() {
        return (String) getField(FIELD_CHGDAT);
    }

    public void setCHGDAT(String value) {
        setField(FIELD_CHGDAT, value);
    }

    public String getCHGNAM() {
        return (String) getField(FIELD_CHGNAM);
    }

    public void setCHGNAM(String value) {
        setField(FIELD_CHGNAM, value);
    }

    public String getCHGTIM() {
        return (String) getField(FIELD_CHGTIM);
    }

    public void setCHGTIM(String value) {
        setField(FIELD_CHGTIM, value);
    }
}