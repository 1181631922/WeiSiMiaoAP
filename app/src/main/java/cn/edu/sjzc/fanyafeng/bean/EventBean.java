package cn.edu.sjzc.fanyafeng.bean;

/**
 * Created by Administrator on 2015/4/2.
 */
public class EventBean implements java.lang.Comparable{
    private String EventImg;
    private String EventName;
    private String EventDetail;
    private String EventTime;

    public EventBean(String eventImg, String eventName, String eventDetail, String eventTime) {
        EventImg = eventImg;
        EventName = eventName;
        EventDetail = eventDetail;
        EventTime = eventTime;
    }

    public EventBean(String eventImg, String eventName, String eventTime) {
        EventImg = eventImg;
        EventName = eventName;
        EventTime = eventTime;
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

    public String getEventDetail() {
        return EventDetail;
    }

    public void setEventDetail(String eventDetail) {
        EventDetail = eventDetail;
    }

    public String getEventTime() {
        return EventTime;
    }

    public void setEventTime(String eventTime) {
        EventTime = eventTime;
    }

    public String getEventImg() {
        return EventImg;
    }

    @Override
    public String toString() {
        return "EventBean{" +
                "EventImg='" + EventImg + '\'' +
                ", EventName='" + EventName + '\'' +
                ", EventDetail='" + EventDetail + '\'' +
                ", EventTime='" + EventTime + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
