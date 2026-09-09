package com.example.schoolschedule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * تمام داده‌هایی که کاربر از UI وارد می‌کند اینجا نگه‌داری می‌شود
 * (نه در کد اصلی برنامه). هر تب همین شیء مشترک را می‌گیرد و به آن
 * اضافه/حذف می‌کند.
 */
public class AppData {
    private final ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private final ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private final ObservableList<SchoolClass> classes = FXCollections.observableArrayList();

    private int nextTeacherId = 1;
    private int nextSubjectId = 1;
    private int nextClassId = 1;

    public ObservableList<Teacher> getTeachers() { return teachers; }
    public ObservableList<Subject> getSubjects() { return subjects; }
    public ObservableList<SchoolClass> getClasses() { return classes; }

    public String newTeacherId() { return "t" + (nextTeacherId++); }
    public String newSubjectId() { return "s" + (nextSubjectId++); }
    public String newClassId() { return "c" + (nextClassId++); }
}
