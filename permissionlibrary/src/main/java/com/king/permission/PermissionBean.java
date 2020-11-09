package com.king.permission;

public class PermissionBean {

    public String requestPermission;
    public  PermissionCallback permissionCallback;

    public PermissionBean(String requestPermission, PermissionCallback permissionCallback) {
        this.requestPermission = requestPermission;
        this.permissionCallback = permissionCallback;
    }


}
