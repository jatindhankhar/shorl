package in.jatindhankhar.shorl.model;

import com.google.gson.annotations.SerializedName;


/**
 * Created by jatin on 12/29/16.
 */

public class Analytics {
    @SerializedName("allTime")
    private TimeData allTime;

    @SerializedName("month")
    private TimeData month;

    @SerializedName("week")
    private TimeData week;

    @SerializedName("day")
    private TimeData day;

    @SerializedName("twoHours")
    private TimeData twohours;


    public TimeData getAllTime() {
        return allTime;
    }

    public void setAllTime(TimeData allTime) {
        this.allTime = allTime;
    }

    public TimeData getMonth() {
        return month;
    }

    public void setMonth(TimeData month) {
        this.month = month;
    }

    public TimeData getWeek() {
        return week;
    }

    public void setWeek(TimeData week) {
        this.week = week;
    }

    public TimeData getDay() {
        return day;
    }

    public void setDay(TimeData day) {
        this.day = day;
    }

    public TimeData getTwohours() {
        return twohours;
    }

    public void setTwohours(TimeData twohours) {
        this.twohours = twohours;
    }
}


