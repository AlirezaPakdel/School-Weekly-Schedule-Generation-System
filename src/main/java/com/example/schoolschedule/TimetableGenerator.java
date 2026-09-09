package com.example.schoolschedule;

import java.util.*;

/**
 * موتور تولید برنامه هفتگی با استفاده از الگوریتم Backtracking.
 *
 * محدودیت‌های سخت (اجباری - هرگز نقض نمی‌شوند):
 *   1) معلم فقط در روزهایی که در دسترس اعلام کرده تدریس می‌کند
 *   2) هیچ معلمی در یک زنگ در دو جای مختلف همزمان نیست
 *   3) هیچ کلاسی در یک زنگ دو درس همزمان ندارد
 *   4) تعداد ساعت هفتگی هر درس دقیقاً برآورده می‌شود
 *
 * محدودیت نرم (ترجیحی - فقط در صورت اجبار نقض می‌شود):
 *   - دروس تخصصی ترجیحاً در چند زنگ آخر روز قرار نمی‌گیرند
 */
public class TimetableGenerator {

    private final List<SchoolClass> classes;
    private final List<Day> workingDays;
    private final int periodsPerDay;
    private final int lastPeriodsToAvoid;
    private final boolean avoidSameSubjectTwiceADay;

    private SchoolTimetable timetable;

    public TimetableGenerator(List<SchoolClass> classes, List<Day> workingDays, int periodsPerDay,
                               int lastPeriodsToAvoid, boolean avoidSameSubjectTwiceADay) {
        this.classes = classes;
        this.workingDays = workingDays;
        this.periodsPerDay = periodsPerDay;
        this.lastPeriodsToAvoid = lastPeriodsToAvoid;
        this.avoidSameSubjectTwiceADay = avoidSameSubjectTwiceADay;
    }

    private static class Unit {
        final SchoolClass schoolClass;
        final Subject subject;
        Unit(SchoolClass schoolClass, Subject subject) {
            this.schoolClass = schoolClass;
            this.subject = subject;
        }
    }

    public Optional<SchoolTimetable> generate() {
        timetable = new SchoolTimetable();
        List<Unit> units = buildUnits();
        units.sort(Comparator.comparingInt(this::difficultyScore));
        boolean ok = backtrack(units, 0);
        return ok ? Optional.of(timetable) : Optional.empty();
    }

    private int difficultyScore(Unit u) {
        int teacherCount = u.subject.getPossibleTeachers().size();
        int availableDaysSum = u.subject.getPossibleTeachers().stream()
                .mapToInt(t -> t.getAvailableDays().size()).sum();
        return teacherCount * 1000 + availableDaysSum;
    }

    private List<Unit> buildUnits() {
        List<Unit> units = new ArrayList<>();
        for (SchoolClass c : classes) {
            for (Map.Entry<Subject, Integer> e : c.getWeeklyHours().entrySet()) {
                for (int i = 0; i < e.getValue(); i++) {
                    units.add(new Unit(c, e.getKey()));
                }
            }
        }
        return units;
    }

    private boolean backtrack(List<Unit> units, int index) {
        if (index == units.size()) return true;
        Unit unit = units.get(index);

        List<TimeSlot> candidateSlots = candidateSlotsFor(unit.subject);

        for (Teacher teacher : orderedTeachers(unit.subject)) {
            for (TimeSlot slot : candidateSlots) {
                if (!teacher.isAvailableOn(slot.getDay())) continue;
                if (!timetable.isTeacherFree(teacher, slot)) continue;
                if (!timetable.isClassFree(unit.schoolClass, slot)) continue;
                if (avoidSameSubjectTwiceADay
                        && timetable.subjectAlreadyOnDay(unit.schoolClass, unit.subject, slot.getDay())) {
                    continue;
                }

                ScheduleEntry entry = new ScheduleEntry(unit.schoolClass, unit.subject, teacher, slot);
                timetable.add(entry);

                if (backtrack(units, index + 1)) return true;

                timetable.remove(entry);
            }
        }
        return false;
    }

    private List<Teacher> orderedTeachers(Subject subject) {
        List<Teacher> teachers = new ArrayList<>(subject.getPossibleTeachers());
        teachers.sort(Comparator.comparingInt(t -> t.getAvailableDays().size()));
        return teachers;
    }

    private List<TimeSlot> candidateSlotsFor(Subject subject) {
        List<TimeSlot> normal = new ArrayList<>();
        List<TimeSlot> late = new ArrayList<>();
        for (Day day : workingDays) {
            for (int p = 1; p <= periodsPerDay; p++) {
                TimeSlot slot = new TimeSlot(day, p);
                boolean isLatePeriod = p > (periodsPerDay - lastPeriodsToAvoid);
                if (subject.isSpecialized() && isLatePeriod) {
                    late.add(slot);
                } else {
                    normal.add(slot);
                }
            }
        }
        List<TimeSlot> result = new ArrayList<>(normal);
        result.addAll(late);
        return result;
    }
}
