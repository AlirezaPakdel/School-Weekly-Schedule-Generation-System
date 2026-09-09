package com.example.schoolschedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** درس؛ ممکن است چند معلم بتوانند آن را تدریس کنند (از UI وارد می‌شود) */
public class Subject {
    private final String id;
    private final String name;
    private final boolean specialized; // آیا درس تخصصی رشته است؟
    private final List<Teacher> possibleTeachers = new ArrayList<>();

    public Subject(String id, String name, boolean specialized, List<Teacher> teachers) {
        this.id = id;
        this.name = name;
        this.specialized = specialized;
        this.possibleTeachers.addAll(teachers);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isSpecialized() { return specialized; }
    public List<Teacher> getPossibleTeachers() { return Collections.unmodifiableList(possibleTeachers); }

    public String getSpecializedText() { return specialized ? "بله" : "خیر"; }

    /** متن نمایشی معلم‌های این درس، برای جدول در UI */
    public String getTeachersText() {
        StringBuilder sb = new StringBuilder();
        for (Teacher t : possibleTeachers) {
            if (sb.length() > 0) sb.append("، ");
            sb.append(t.getName());
        }
        return sb.length() == 0 ? "-" : sb.toString();
    }

    @Override
    public String toString() { return name; }
}
