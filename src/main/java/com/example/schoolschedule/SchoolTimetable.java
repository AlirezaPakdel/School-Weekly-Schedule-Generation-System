package com.example.schoolschedule;

import java.util.*;

/** نگهدارنده‌ی برنامه نهایی و ابزارهای سریع برای بررسی تداخل زمانی */
public class SchoolTimetable {
    private final List<ScheduleEntry> entries = new ArrayList<>();

    private final Map<String, Set<TimeSlot>> teacherBusy = new HashMap<>();
    private final Map<String, Set<TimeSlot>> classBusy = new HashMap<>();
    private final Map<String, Map<Day, Set<String>>> classDaySubjects = new HashMap<>();

    public boolean isTeacherFree(Teacher teacher, TimeSlot slot) {
        return !teacherBusy.getOrDefault(teacher.getId(), Collections.emptySet()).contains(slot);
    }

    public boolean isClassFree(SchoolClass schoolClass, TimeSlot slot) {
        return !classBusy.getOrDefault(schoolClass.getId(), Collections.emptySet()).contains(slot);
    }

    public boolean subjectAlreadyOnDay(SchoolClass schoolClass, Subject subject, Day day) {
        Set<String> set = classDaySubjects
                .getOrDefault(schoolClass.getId(), Collections.emptyMap())
                .getOrDefault(day, Collections.emptySet());
        return set.contains(subject.getId());
    }

    public void add(ScheduleEntry entry) {
        entries.add(entry);
        teacherBusy.computeIfAbsent(entry.getTeacher().getId(), k -> new HashSet<>()).add(entry.getTimeSlot());
        classBusy.computeIfAbsent(entry.getSchoolClass().getId(), k -> new HashSet<>()).add(entry.getTimeSlot());
        classDaySubjects
                .computeIfAbsent(entry.getSchoolClass().getId(), k -> new HashMap<>())
                .computeIfAbsent(entry.getTimeSlot().getDay(), k -> new HashSet<>())
                .add(entry.getSubject().getId());
    }

    public void remove(ScheduleEntry entry) {
        entries.remove(entry);
        teacherBusy.getOrDefault(entry.getTeacher().getId(), Collections.emptySet()).remove(entry.getTimeSlot());
        classBusy.getOrDefault(entry.getSchoolClass().getId(), Collections.emptySet()).remove(entry.getTimeSlot());
        Map<Day, Set<String>> m = classDaySubjects.get(entry.getSchoolClass().getId());
        if (m != null) {
            Set<String> s = m.get(entry.getTimeSlot().getDay());
            if (s != null) s.remove(entry.getSubject().getId());
        }
    }

    public List<ScheduleEntry> getEntries() { return entries; }
}
