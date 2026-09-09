package com.example.schoolschedule;

/** یک خانه‌ی پرشده در برنامه هفتگی نهایی: چه درسی، چه معلمی، چه زمانی، برای کدام کلاس */
public class ScheduleEntry {
    private final SchoolClass schoolClass;
    private final Subject subject;
    private final Teacher teacher;
    private final TimeSlot timeSlot;

    public ScheduleEntry(SchoolClass schoolClass, Subject subject, Teacher teacher, TimeSlot timeSlot) {
        this.schoolClass = schoolClass;
        this.subject = subject;
        this.teacher = teacher;
        this.timeSlot = timeSlot;
    }

    public SchoolClass getSchoolClass() { return schoolClass; }
    public Subject getSubject() { return subject; }
    public Teacher getTeacher() { return teacher; }
    public TimeSlot getTimeSlot() { return timeSlot; }
}
