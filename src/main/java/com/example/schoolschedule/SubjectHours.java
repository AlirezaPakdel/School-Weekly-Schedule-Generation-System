package com.example.schoolschedule;

/** یک جفت (درس، تعداد ساعت هفتگی) - برای تعریف دروس هر کلاس در UI استفاده می‌شود */
public class SubjectHours {
    private final Subject subject;
    private final int hoursPerWeek;

    public SubjectHours(Subject subject, int hoursPerWeek) {
        this.subject = subject;
        this.hoursPerWeek = hoursPerWeek;
    }

    public Subject getSubject() { return subject; }
    public int getHoursPerWeek() { return hoursPerWeek; }
    public String getSubjectName() { return subject.getName(); }

    @Override
    public String toString() { return subject.getName() + " (" + hoursPerWeek + " ساعت)"; }
}
