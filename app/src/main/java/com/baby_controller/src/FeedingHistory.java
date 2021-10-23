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


    public FeedingHistory(Baby child){
        _head = new Day(child);
        _head.set_carrDate(new Date(System.currentTimeMillis()));
        _curr = _head;
    }
    public void addNextDay(Baby baby){
        Day newDay = new Day(baby);
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
        if(_head == null){
            _head = new Day(new Date(System.currentTimeMillis()));
        }
        Day tmp = _head;
        while (tmp.get_next() != null){
            tmp = tmp.get_next();
        }
        return tmp;



    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Day tmp = _head;
        while (tmp.get_next() != null){
            str.append(tmp.toString());
            tmp = tmp.get_next();
        }
        return str.toString();
    }

    public Day get_curr() {
        return _curr;
    }

    public FeedingHistory() {
    }
}
