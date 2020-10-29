package com.king.permission;

public class PermissionBean {

    public PermissionBean(String requestPermission, PermissionCallback permissionCallback) {
        this.requestPermission = requestPermission;
        this.permissionCallback = permissionCallback;
    }

    public String requestPermission;
    public  PermissionCallback permissionCallback;
}
