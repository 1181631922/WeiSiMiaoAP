package cn.edu.sjzc.fanyafeng.bean;

/**
 * Created by Administrator on 2015/4/10.
 */
public class ExhibitionBean implements java.lang.Comparable{
    private String ExhibitionImg;
    private String ExhibitionDesc;
    private String ExhibitionTime;
    private String ExhibitionPalace;
    private boolean IsFinished = false;
    private int State;

    public ExhibitionBean(String exhibitionImg, String exhibitionDesc, String exhibitionTime, String exhibitionPalace, int state) {
        ExhibitionImg = exhibitionImg;
        ExhibitionDesc = exhibitionDesc;
        ExhibitionTime = exhibitionTime;
        ExhibitionPalace = exhibitionPalace;
        State = state;
    }

    public String getExhibitionImg() {
        return ExhibitionImg;
    }

    public void setExhibitionImg(String exhibitionImg) {
        ExhibitionImg = exhibitionImg;
    }

    public String getExhibitionDesc() {
        return ExhibitionDesc;
    }

    public void setExhibitionDesc(String exhibitionDesc) {
        ExhibitionDesc = exhibitionDesc;
    }

    public String getExhibitionTime() {
        return ExhibitionTime;
    }

    public void setExhibitionTime(String exhibitionTime) {
        ExhibitionTime = exhibitionTime;
    }

    public String getExhibitionPalace() {
        return ExhibitionPalace;
    }

    public void setExhibitionPalace(String exhibitionPalace) {
        ExhibitionPalace = exhibitionPalace;
    }

    public boolean isFinished() {
        return IsFinished;
    }

    public void setFinished(boolean isFinished) {
        IsFinished = isFinished;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    @Override
    public String toString() {
        return "ExhibitionBean{" +
                "ExhibitionImg='" + ExhibitionImg + '\'' +
                ", ExhibitionDesc='" + ExhibitionDesc + '\'' +
                ", ExhibitionTime='" + ExhibitionTime + '\'' +
                ", ExhibitionPalace='" + ExhibitionPalace + '\'' +
                ", IsFinished=" + IsFinished +
                ", State=" + State +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
