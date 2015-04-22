package cn.edu.sjzc.fanyafeng.bean;

/**
 * Created by Administrator on 2015/4/2.
 */
public class FoXiangBean implements Comparable {
    private String FoXiangImg;
    private String FoXiangName;
    private String FoXiangDetail;
    private String FoXiangPhone;

    public FoXiangBean(String foXiangImg, String foXiangName, String foXiangDetail, String foXiangPhone) {
        FoXiangImg = foXiangImg;
        FoXiangName = foXiangName;
        FoXiangDetail = foXiangDetail;
        FoXiangPhone = foXiangPhone;
    }

    public String getFoXiangImg() {
        return FoXiangImg;
    }

    public void setFoXiangImg(String foXiangImg) {
        FoXiangImg = foXiangImg;
    }

    public String getFoXiangName() {
        return FoXiangName;
    }

    public void setFoXiangName(String foXiangName) {
        FoXiangName = foXiangName;
    }

    public String getFoXiangDetail() {
        return FoXiangDetail;
    }

    public void setFoXiangDetail(String foXiangDetail) {
        FoXiangDetail = foXiangDetail;
    }

    public String getFoXiangPhone() {
        return FoXiangPhone;
    }

    public void setFoXiangPhone(String foXiangPhone) {
        FoXiangPhone = foXiangPhone;
    }

    @Override
    public String toString() {
        return "FoXiangBean{" +
                "FoXiangImg='" + FoXiangImg + '\'' +
                ", FoXiangName='" + FoXiangName + '\'' +
                ", FoXiangDetail='" + FoXiangDetail + '\'' +
                ", FoXiangPhone='" + FoXiangPhone + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
