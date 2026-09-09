package com.example.schoolschedule;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/** معلم؛ هر معلم فقط در روزهای مشخصی می‌تواند تدریس کند (از UI وارد می‌شود) */
public class Teacher {
    private final String id;
    private final String name;
    private final Set<Day> availableDays;

    public Teacher(String id, String name, Set<Day> availableDays) {
        this.id = id;
        this.name = name;
        this.availableDays = availableDays.isEmpty() ? EnumSet.noneOf(Day.class) : EnumSet.copyOf(availableDays);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Set<Day> getAvailableDays() { return Collections.unmodifiableSet(availableDays); }
    public boolean isAvailableOn(Day day) { return availableDays.contains(day); }

    /** متن نمایشی روزهای در دسترس، برای جدول در UI */
    public String getAvailableDaysText() {
        StringBuilder sb = new StringBuilder();
        for (Day d : Day.values()) {
            if (availableDays.contains(d)) {
                if (sb.length() > 0) sb.append("، ");
                sb.append(d.getPersianName());
            }
        }
        return sb.length() == 0 ? "-" : sb.toString();
    }

    @Override
    public String toString() { return name; }
}
