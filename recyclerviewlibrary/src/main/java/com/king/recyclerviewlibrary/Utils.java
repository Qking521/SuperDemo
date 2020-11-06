package com.king.recyclerviewlibrary;

import java.io.File;

public class Utils {

    public static CommonItem getCommonItem(Object o) {
        if (o instanceof File) {
            return getCommonItemOfFileType(o);
        }
        if (o instanceof String) {
            return getCommonItemOfStringType(o);
        }
        return null;
    }

    private static CommonItem getCommonItemOfFileType(Object o) {
        File file = (File)o;
        CommonItem commonItem = new CommonItem();
        commonItem.title = file.getName();
        commonItem.file = file;
        return commonItem;
    }

    private static CommonItem getCommonItemOfStringType(Object o) {
        String s = (String)o;
        CommonItem commonItem = new CommonItem();
        commonItem.title = s;
        return commonItem;
    }
}
