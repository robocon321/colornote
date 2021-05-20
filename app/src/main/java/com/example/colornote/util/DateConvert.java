package com.example.colornote.util;

import java.sql.Date;
import java.util.Comparator;

public class DateConvert implements Comparator<DateConvert>{
    private int second;
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;

    public DateConvert(Date date){
        this.second = date.getSeconds();
        this.minute= date.getMinutes();
        this.hour = date.getHours();
        this.day = date.getDate();
        this.month = date.getMonth() + 1;
        this.year = date.getYear() + 1;
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

    public String getTime(){
        Date date = new Date(System.currentTimeMillis());
        if(date.getYear() == year && date.getMonth() == month  && date.getDay() == day){
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