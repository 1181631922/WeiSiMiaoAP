package cn.edu.sjzc.fanyafeng.bean;

/**
 * Created by Administrator on 2015/4/2.
 */
public class SengFuBean implements Comparable {
    private String SengFuImg;
    private String SengFuName;
    private String SengFuDetail;
    private String SengFuPhone;


    public SengFuBean(String sengFuImg, String sengFuName, String sengFuDetail, String sengFuPhone) {
        SengFuImg = sengFuImg;
        SengFuName = sengFuName;
        SengFuDetail = sengFuDetail;
        SengFuPhone = sengFuPhone;
    }

    public String getSengFuImg() {
        return SengFuImg;
    }

    public void setSengFuImg(String sengFuImg) {
        SengFuImg = sengFuImg;
    }

    public String getSengFuName() {
        return SengFuName;
    }

    public void setSengFuName(String sengFuName) {
        SengFuName = sengFuName;
    }

    public String getSengFuDetail() {
        return SengFuDetail;
    }

    public void setSengFuDetail(String sengFuDetail) {
        SengFuDetail = sengFuDetail;
    }

    public String getSengFuPhone() {
        return SengFuPhone;
    }

    public void setSengFuPhone(String sengFuPhone) {
        SengFuPhone = sengFuPhone;
    }

    @Override
    public String toString() {
        return "SengFuBean{" +
                "SengFuImg='" + SengFuImg + '\'' +
                ", SengFuName='" + SengFuName + '\'' +
                ", SengFuDetail='" + SengFuDetail + '\'' +
                ", SengFuPhone='" + SengFuPhone + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
