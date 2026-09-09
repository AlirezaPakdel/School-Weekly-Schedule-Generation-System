package com.example.schoolschedule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** یک کلاس/پایه درسی، به همراه دروسی که باید در هفته داشته باشد و تعداد ساعت هرکدام */
public class SchoolClass {
    private final String id;
    private final String name;   // مثلاً «الف»
    private final String grade;  // مثلاً «دهم ریاضی»
    private final List<SubjectHours> subjectHoursList = new ArrayList<>();

    public SchoolClass(String id, String name, String grade, List<SubjectHours> subjectHoursList) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.subjectHoursList.addAll(subjectHoursList);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getGrade() { return grade; }
    public List<SubjectHours> getSubjectHoursList() { return subjectHoursList; }

    public Map<Subject, Integer> getWeeklyHours() {
        Map<Subject, Integer> map = new LinkedHashMap<>();
        for (SubjectHours sh : subjectHoursList) {
            map.put(sh.getSubject(), sh.getHoursPerWeek());
        }
        return map;
    }

    public String getFullTitle() { return grade + " - " + name; }

    /** متن نمایشی دروس این کلاس، برای جدول در UI */
    public String getSubjectsSummaryText() {
        StringBuilder sb = new StringBuilder();
        for (SubjectHours sh : subjectHoursList) {
            if (sb.length() > 0) sb.append("، ");
            sb.append(sh.getSubjectName()).append(":").append(sh.getHoursPerWeek());
        }
        return sb.length() == 0 ? "-" : sb.toString();
    }

    @Override
    public String toString() { return getFullTitle(); }
}
