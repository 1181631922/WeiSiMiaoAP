package cn.edu.sjzc.fanyafeng.bean;

/**
 * Created by Administrator on 2015/4/2.
 */
public class GongZhuoBean implements Comparable {
    private String GongZhuoImg;
    private String GongZhuoName;
    private String GongZhuoDetail;
    private String GongZhuoPhone;

    public GongZhuoBean(String gongZhuoImg, String gongZhuoName, String gongZhuoDetail, String gongZhuoPhone) {
        GongZhuoImg = gongZhuoImg;
        GongZhuoName = gongZhuoName;
        GongZhuoDetail = gongZhuoDetail;
        GongZhuoPhone = gongZhuoPhone;
    }

    public String getGongZhuoImg() {
        return GongZhuoImg;
    }

    public void setGongZhuoImg(String gongZhuoImg) {
        GongZhuoImg = gongZhuoImg;
    }

    public String getGongZhuoName() {
        return GongZhuoName;
    }

    public void setGongZhuoName(String gongZhuoName) {
        GongZhuoName = gongZhuoName;
    }

    public String getGongZhuoDetail() {
        return GongZhuoDetail;
    }

    public void setGongZhuoDetail(String gongZhuoDetail) {
        GongZhuoDetail = gongZhuoDetail;
    }

    public String getGongZhuoPhone() {
        return GongZhuoPhone;
    }

    public void setGongZhuoPhone(String gongZhuoPhone) {
        GongZhuoPhone = gongZhuoPhone;
    }

    @Override
    public String toString() {
        return "GongZhuoBean{" +
                "GongZhuoImg='" + GongZhuoImg + '\'' +
                ", GongZhuoName='" + GongZhuoName + '\'' +
                ", GongZhuoDetail='" + GongZhuoDetail + '\'' +
                ", GongZhuoPhone='" + GongZhuoPhone + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
