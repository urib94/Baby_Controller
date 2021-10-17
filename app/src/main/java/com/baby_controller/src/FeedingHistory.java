package com.baby_controller.src;

import java.util.Date;

public class FeedingHistory {
    private Day _head;
    private Day _curr;

    public FeedingHistory(Child child){
        _head = new Day(child);
        _head.set_carrDate(new Date(System.currentTimeMillis()));
        _curr = _head;
    }
    public void addNextDay(Child child){
        Day newDay = new Day(child);
    }

    public void set_head(Day _head) {
        this._head = _head;
    }

    public void set_curr(Day _curr) {
        this._curr = _curr;
    }

    public Day get_head() {
        return _head;
    }

    public Day getLast(){
        Day tmp = _head;
        while (tmp.get_next() != null){
            tmp = tmp.get_next();
        }
        return tmp;



    }
    public Day get_curr() {
        return _curr;
    }

    public FeedingHistory() {
    }
}