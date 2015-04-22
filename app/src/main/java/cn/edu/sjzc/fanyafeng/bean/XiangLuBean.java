package cn.edu.sjzc.fanyafeng.bean;

/**
 * Created by Administrator on 2015/4/2.
 */
public class XiangLuBean implements Comparable {
    private String XiangLuImg;
    private String XiangLuName;
    private String XiangLuDetail;
    private String XiangLuPhone;

    public XiangLuBean(String xiangLuImg, String xiangLuName, String xiangLuDetail, String xiangLuPhone) {
        XiangLuImg = xiangLuImg;
        XiangLuName = xiangLuName;
        XiangLuDetail = xiangLuDetail;
        XiangLuPhone = xiangLuPhone;
    }

    public String getXiangLuImg() {
        return XiangLuImg;
    }

    public void setXiangLuImg(String xiangLuImg) {
        XiangLuImg = xiangLuImg;
    }

    public String getXiangLuName() {
        return XiangLuName;
    }

    public void setXiangLuName(String xiangLuName) {
        XiangLuName = xiangLuName;
    }

    public String getXiangLuDetail() {
        return XiangLuDetail;
    }

    public void setXiangLuDetail(String xiangLuDetail) {
        XiangLuDetail = xiangLuDetail;
    }

    public String getXiangLuPhone() {
        return XiangLuPhone;
    }

    public void setXiangLuPhone(String xiangLuPhone) {
        XiangLuPhone = xiangLuPhone;
    }

    @Override
    public String toString() {
        return "XiangLuBean{" +
                "XiangLuImg='" + XiangLuImg + '\'' +
                ", XiangLuName='" + XiangLuName + '\'' +
                ", XiangLuDetail='" + XiangLuDetail + '\'' +
                ", XiangLuPhone='" + XiangLuPhone + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
