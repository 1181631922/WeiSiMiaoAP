package cn.edu.sjzc.fanyafeng.bean;

/**
 * Created by Administrator on 2015/4/2.
 */
public class EventBean implements java.lang.Comparable {
    private String EventImg;
    private String EventName;
    private String EventStartName;
    private String EventEndTime;
    private String EventDetail;
    private String EventMoney;

    public EventBean(String eventImg, String eventName, String eventStartName, String eventEndTime, String eventDetail, String eventMoney) {
        EventImg = eventImg;
        EventName = eventName;
        EventStartName = eventStartName;
        EventEndTime = eventEndTime;
        EventDetail = eventDetail;
        EventMoney = eventMoney;
    }

    public String getEventImg() {
        return EventImg;
    }

    public void setEventImg(String eventImg) {
        EventImg = eventImg;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventStartName() {
        return EventStartName;
    }

    public void setEventStartName(String eventStartName) {
        EventStartName = eventStartName;
    }

    public String getEventEndTime() {
        return EventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        EventEndTime = eventEndTime;
    }

    public String getEventDetail() {
        return EventDetail;
    }

    public void setEventDetail(String eventDetail) {
        EventDetail = eventDetail;
    }

    public String getEventMoney() {
        return EventMoney;
    }

    public void setEventMoney(String eventMoney) {
        EventMoney = eventMoney;
    }

    @Override
    public String toString() {
        return "EventBean{" +
                "EventImg='" + EventImg + '\'' +
                ", EventName='" + EventName + '\'' +
                ", EventStartName='" + EventStartName + '\'' +
                ", EventEndTime='" + EventEndTime + '\'' +
                ", EventDetail='" + EventDetail + '\'' +
                ", EventMoney='" + EventMoney + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
