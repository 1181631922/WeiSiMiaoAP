package cn.edu.sjzc.fanyafeng.bean;

/**
 * Created by Administrator on 2015/4/2.
 */
public class BuddhistServicesBean implements java.lang.Comparable{
    private String BSTitle;
    private String BSDetail;
    private String BSFirst;
    private String BSSecond;
    private String BSThird;

    public BuddhistServicesBean(String BSTitle, String BSDetail, String BSFirst, String BSSecond, String BSThird) {
        this.BSTitle = BSTitle;
        this.BSDetail = BSDetail;
        this.BSFirst = BSFirst;
        this.BSSecond = BSSecond;
        this.BSThird = BSThird;
    }


    public String getBSTitle() {
        return BSTitle;
    }

    public void setBSTitle(String BSTitle) {
        this.BSTitle = BSTitle;
    }

    public String getBSDetail() {
        return BSDetail;
    }

    public void setBSDetail(String BSDetail) {
        this.BSDetail = BSDetail;
    }

    public String getBSFirst() {
        return BSFirst;
    }

    public void setBSFirst(String BSFirst) {
        this.BSFirst = BSFirst;
    }

    public String getBSSecond() {
        return BSSecond;
    }

    public void setBSSecond(String BSSecond) {
        this.BSSecond = BSSecond;
    }

    public String getBSThird() {
        return BSThird;
    }

    public void setBSThird(String BSThird) {
        this.BSThird = BSThird;
    }

    @Override
    public String toString() {
        return "BuddhistServicesBean{" +
                "BSTitle='" + BSTitle + '\'' +
                ", BSDetail='" + BSDetail + '\'' +
                ", BSFirst='" + BSFirst + '\'' +
                ", BSSecond='" + BSSecond + '\'' +
                ", BSThird='" + BSThird + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
