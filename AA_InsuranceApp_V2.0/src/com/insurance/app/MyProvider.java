package com.insurance.app;
/**
'*********************************************************************************************************************
'* Project Name             : VHA-SPS
'* File Name                : ContentProvider.java
'* Description              : It helps to handle File content provider
'* Package Name             : com.vha.wrapper
'* Version Number           : 1.0
'* Original Author          : Mobility
'* Start Date               : 24-Sep-2014
'* Last Modified Date       : 24-Sep-2014
'* Modified By              : Mobility
'* © VHA-SPS, 2014, Confidential and proprietary.
'**********************************************************************************************************************
 **/
import java.io.File;
import java.io.FileNotFoundException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public class MyProvider extends ContentProvider {

@Override
public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
    File privateFile = new File(getContext().getFilesDir(), uri.getPath());
    return ParcelFileDescriptor.open(privateFile, ParcelFileDescriptor.MODE_READ_ONLY);
}

@Override
public int delete(Uri arg0, String arg1, String[] arg2) {
    return 0;
}

@Override
public String getType(Uri arg0) {
    return null;
}

@Override
public Uri insert(Uri arg0, ContentValues arg1) {
    return null;
}

@Override
public boolean onCreate() {
    return false;
}

@Override
public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
        String arg4) {
    return null;
}

@Override
public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
    return 0;
}
}
