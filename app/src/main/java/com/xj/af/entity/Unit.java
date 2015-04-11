package com.xj.af.entity;

import java.io.Serializable;

/**
 * @author xiaojia
 */
public class Unit implements Serializable {

    private String unitName;
    private String unitManager;// 法人
    private String phoneNumber;// 电话
    private String unitType;// 性质
    private String leaderUnit;// 主管单位
    private String address;// 地址
    private String permissions;// 权限
    private String domain;// 域名
    private String bank;//开户行
    private String blankName;// 银行账号名称
    private String blankNum;// 银行账号
    private String lng;
    private String lat;
    private int id;
    private String qq;
    private String weixin;
    private String zhifubao;


    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getZhifubao() {
        return zhifubao;
    }

    public void setZhifubao(String zhifubao) {
        this.zhifubao = zhifubao;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getBlankName() {
        return blankName;
    }

    public void setBlankName(String blankName) {
        this.blankName = blankName;
    }

    public String getBlankNum() {
        return blankNum;
    }

    public void setBlankNum(String blankNum) {
        this.blankNum = blankNum;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getLeaderUnit() {
        return leaderUnit;
    }

    public void setLeaderUnit(String leaderUnit) {
        this.leaderUnit = leaderUnit;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitManager() {
        return unitManager;
    }

    public void setUnitManager(String unitManager) {
        this.unitManager = unitManager;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

}
