package com.example.schoolschedule;

import java.util.Objects;

/** یک بازه زمانی مشخص در برنامه هفتگی: یک روز + یک زنگ (پیریود) */
public class TimeSlot {
    private final Day day;
    private final int period; // شماره زنگ، از ۱ شروع می‌شود

    public TimeSlot(Day day, int period) {
        this.day = day;
        this.period = period;
    }

    public Day getDay() { return day; }
    public int getPeriod() { return period; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSlot)) return false;
        TimeSlot t = (TimeSlot) o;
        return period == t.period && day == t.day;
    }

    @Override
    public int hashCode() { return Objects.hash(day, period); }

    @Override
    public String toString() { return day + " - زنگ " + period; }
}
