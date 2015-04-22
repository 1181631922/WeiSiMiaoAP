package cn.edu.sjzc.fanyafeng.bean;

/**
 * Created by Administrator on 2015/4/2.
 */
public class SiMiaoBean implements Comparable {
    private String SiMiaoImg;
    private String SiMiaoName;
    private String SiMiaoDetail;
    private String SiMiaoPhone;

    public SiMiaoBean(String siMiaoImg, String siMiaoName, String siMiaoDetail, String siMiaoPhone) {
        SiMiaoImg = siMiaoImg;
        SiMiaoName = siMiaoName;
        SiMiaoDetail = siMiaoDetail;
        SiMiaoPhone = siMiaoPhone;
    }

    public String getSiMiaoImg() {
        return SiMiaoImg;
    }

    public void setSiMiaoImg(String siMiaoImg) {
        SiMiaoImg = siMiaoImg;
    }

    public String getSiMiaoName() {
        return SiMiaoName;
    }

    public void setSiMiaoName(String siMiaoName) {
        SiMiaoName = siMiaoName;
    }

    public String getSiMiaoDetail() {
        return SiMiaoDetail;
    }

    public void setSiMiaoDetail(String siMiaoDetail) {
        SiMiaoDetail = siMiaoDetail;
    }

    public String getSiMiaoPhone() {
        return SiMiaoPhone;
    }

    public void setSiMiaoPhone(String siMiaoPhone) {
        SiMiaoPhone = siMiaoPhone;
    }

    @Override
    public String toString() {
        return "SiMiaoBean{" +
                "SiMiaoImg='" + SiMiaoImg + '\'' +
                ", SiMiaoName='" + SiMiaoName + '\'' +
                ", SiMiaoDetail='" + SiMiaoDetail + '\'' +
                ", SiMiaoPhone='" + SiMiaoPhone + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
