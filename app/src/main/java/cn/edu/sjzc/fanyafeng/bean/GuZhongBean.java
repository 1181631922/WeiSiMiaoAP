package cn.edu.sjzc.fanyafeng.bean;

/**
 * Created by Administrator on 2015/4/2.
 */
public class GuZhongBean implements Comparable {
    private String GuZhongImg;
    private String GuZhongName;
    private String GuZhongDetail;
    private String GuZhongPhone;

    public GuZhongBean(String guZhongImg, String guZhongName, String guZhongDetail, String guZhongPhone) {
        GuZhongImg = guZhongImg;
        GuZhongName = guZhongName;
        GuZhongDetail = guZhongDetail;
        GuZhongPhone = guZhongPhone;
    }

    public String getGuZhongImg() {
        return GuZhongImg;
    }

    public void setGuZhongImg(String guZhongImg) {
        GuZhongImg = guZhongImg;
    }

    public String getGuZhongName() {
        return GuZhongName;
    }

    public void setGuZhongName(String guZhongName) {
        GuZhongName = guZhongName;
    }

    public String getGuZhongDetail() {
        return GuZhongDetail;
    }

    public void setGuZhongDetail(String guZhongDetail) {
        GuZhongDetail = guZhongDetail;
    }

    public String getGuZhongPhone() {
        return GuZhongPhone;
    }

    public void setGuZhongPhone(String guZhongPhone) {
        GuZhongPhone = guZhongPhone;
    }

    @Override
    public String toString() {
        return "GuZhongBean{" +
                "GuZhongImg='" + GuZhongImg + '\'' +
                ", GuZhongName='" + GuZhongName + '\'' +
                ", GuZhongDetail='" + GuZhongDetail + '\'' +
                ", GuZhongPhone='" + GuZhongPhone + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
