package cn.edu.sjzc.fanyafeng.bean;

/**
 * Created by Administrator on 2015/4/2.
 */
public class FaQiBean implements Comparable {
    private String FaQiImg;
    private String FaQiName;
    private String FaQiDetail;
    private String FaQiPhone;

    public FaQiBean(String faQiImg, String faQiName, String faQiDetail, String faQiPhone) {
        FaQiImg = faQiImg;
        FaQiName = faQiName;
        FaQiDetail = faQiDetail;
        FaQiPhone = faQiPhone;
    }

    public String getFaQiImg() {
        return FaQiImg;
    }

    public void setFaQiImg(String faQiImg) {
        FaQiImg = faQiImg;
    }

    public String getFaQiName() {
        return FaQiName;
    }

    public void setFaQiName(String faQiName) {
        FaQiName = faQiName;
    }

    public String getFaQiDetail() {
        return FaQiDetail;
    }

    public void setFaQiDetail(String faQiDetail) {
        FaQiDetail = faQiDetail;
    }

    public String getFaQiPhone() {
        return FaQiPhone;
    }

    public void setFaQiPhone(String faQiPhone) {
        FaQiPhone = faQiPhone;
    }

    @Override
    public String toString() {
        return "FaQiBean{" +
                "FaQiImg='" + FaQiImg + '\'' +
                ", FaQiName='" + FaQiName + '\'' +
                ", FaQiDetail='" + FaQiDetail + '\'' +
                ", FaQiPhone='" + FaQiPhone + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
