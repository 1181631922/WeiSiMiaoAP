package cn.edu.sjzc.fanyafeng.bean;

/**
 * Created by Administrator on 2015/4/2.
 */
public class ZhuangShiBean implements Comparable {
    private String ZhuangShiImg;
    private String ZhuangShiName;
    private String ZhuangShiDetail;
    private String ZhuangShiPhone;

    public ZhuangShiBean(String zhuangShiImg, String zhuangShiName, String zhuangShiDetail, String zhuangShiPhone) {
        ZhuangShiImg = zhuangShiImg;
        ZhuangShiName = zhuangShiName;
        ZhuangShiDetail = zhuangShiDetail;
        ZhuangShiPhone = zhuangShiPhone;
    }

    public String getZhuangShiImg() {
        return ZhuangShiImg;
    }

    public void setZhuangShiImg(String zhuangShiImg) {
        ZhuangShiImg = zhuangShiImg;
    }

    public String getZhuangShiName() {
        return ZhuangShiName;
    }

    public void setZhuangShiName(String zhuangShiName) {
        ZhuangShiName = zhuangShiName;
    }

    public String getZhuangShiDetail() {
        return ZhuangShiDetail;
    }

    public void setZhuangShiDetail(String zhuangShiDetail) {
        ZhuangShiDetail = zhuangShiDetail;
    }

    public String getZhuangShiPhone() {
        return ZhuangShiPhone;
    }

    public void setZhuangShiPhone(String zhuangShiPhone) {
        ZhuangShiPhone = zhuangShiPhone;
    }

    @Override
    public String toString() {
        return "ZhuangShiBean{" +
                "ZhuangShiImg='" + ZhuangShiImg + '\'' +
                ", ZhuangShiName='" + ZhuangShiName + '\'' +
                ", ZhuangShiDetail='" + ZhuangShiDetail + '\'' +
                ", ZhuangShiPhone='" + ZhuangShiPhone + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
