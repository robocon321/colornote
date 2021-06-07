package com.example.colornote.util;

import android.util.Log;

import java.util.Date;
import java.util.Calendar;
import java.util.Comparator;

public class DateConvert implements Comparator<DateConvert>{
    private int second;
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;

    public DateConvert(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.second = calendar.get(Calendar.SECOND);
        this.minute= calendar.get(Calendar.MINUTE);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR) - 1900;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getSecond() {
        return second;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }

    public String showTime(){
        Calendar cal = Calendar.getInstance();
        if(cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) + 1 == month  && cal.get(Calendar.DAY_OF_MONTH) == day){
            return this.hour +":"+this.minute;
        }else {
            return this.year + "/" + this.month + "/" + this.day;
        }
    }

    @Override
    public int compare(DateConvert d1, DateConvert d2) {
        if(d1.year > d2.year){
            return 1;
        }else if(d1.year == d2.year){
            if(d1.month > d2.month){
                return 1;
            }else if(d1.month == d2.month){
                if(d1.day > d2.day){
                    return 1;
                }else if(d1.day == d2.day){
                    if(d1.hour > d2.hour){
                        return 1;
                    }else if(d1.hour == d2.hour){
                        if(d1.minute > d2.minute){
                            return 1;
                        }else if(d1.minute == d2.minute){
                            if(d1.second > d2.second){
                                return 1;
                            }else{
                                return -1;
                            }
                        }else{
                            return -1;
                        }
                    }else{
                        return -1;
                    }
                }else{
                    return -1;
                }

            }else{
                return -1;
            }
        }else {
            return -1;
        }
    }
}