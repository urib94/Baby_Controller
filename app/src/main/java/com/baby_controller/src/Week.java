package com.baby_controller.src;

import java.util.Date;

public class Week {
    Day[] _daysOfTheWeek = new Day[7];
    Date startDate = new Date(System.currentTimeMillis());
    protected Child _child ;

    public Week(Child child){
        int i = 0;
        this._child = child;

        for(Day day:_daysOfTheWeek){
            day = new Day(_child);
            day.set_carrDate(new Date(System.currentTimeMillis() + (1000 * 60 * 24) * i++));
        }
    };


    public Day getLastDay(){
        for(Day day : _daysOfTheWeek){
            if(day.get_currDate().compareTo(new Date(System.currentTimeMillis())) == 0){
                return day;
            }
        }
        return null;
    }

    public Day[] get_daysOfTheWeek() {
        return _daysOfTheWeek;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Child get_child() {
        return _child;
    }
}

