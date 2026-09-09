package com.example.schoolschedule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * تب «کلاس‌ها»: تعیین تعداد و مشخصات کلاس‌ها.
 * هر کلاس یک نام، یک پایه/رشته و مجموعه‌ای از (درس، ساعت هفتگی) دارد
 * که همگی از طریق فرم وارد می‌شوند - هیچ‌کدام در کد اصلی نوشته نشده‌اند.
 */
class ClassesTab {

    static Tab build(AppData appData) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        TextField nameField = new TextField();
        nameField.setPromptText("نام کلاس، مثلاً «الف»");

        TextField gradeField = new TextField();
        gradeField.setPromptText("پایه/رشته، مثلاً «دهم ریاضی»");

        ComboBox<Subject> subjectCombo = new ComboBox<>(appData.getSubjects());
        subjectCombo.setPromptText("انتخاب درس");

        Spinner<Integer> hoursSpinner = new Spinner<>(1, 20, 2);
        hoursSpinner.setEditable(true);

        ObservableList<SubjectHours> tempSubjectHours = FXCollections.observableArrayList();
        TableView<SubjectHours> tempTable = new TableView<>(tempSubjectHours);
        tempTable.getColumns().add(Utils.textColumn("درس", SubjectHours::getSubjectName));
        tempTable.getColumns().add(Utils.textColumn("ساعت هفتگی", sh -> String.valueOf(sh.getHoursPerWeek())));
        tempTable.getColumns().add(Utils.deleteColumn("حذف", tempSubjectHours));
        tempTable.setPrefHeight(150);
        tempTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button addSubjectToClassBtn = new Button("افزودن درس به کلاس");
        addSubjectToClassBtn.setOnAction(e -> {
            Subject subject = subjectCombo.getValue();
            if (subject == null) {
                errorLabel.setText("یک درس انتخاب کنید (از تب دروس اضافه کرده باشید).");
                return;
            }
            boolean exists = tempSubjectHours.stream().anyMatch(sh -> sh.getSubject().equals(subject));
            if (exists) {
                errorLabel.setText("این درس قبلاً به کلاس اضافه شده است.");
                return;
            }
            errorLabel.setText("");
            tempSubjectHours.add(new SubjectHours(subject, hoursSpinner.getValue()));
            subjectCombo.setValue(null);
        });

        HBox subjectRow = new HBox(8, subjectCombo, new Label("ساعت هفتگی:"), hoursSpinner, addSubjectToClassBtn);

        Button saveClassBtn = new Button("ثبت کلاس");
        saveClassBtn.setStyle("-fx-font-weight: bold;");
        saveClassBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String grade = gradeField.getText().trim();
            if (name.isEmpty() || grade.isEmpty()) {
                errorLabel.setText("نام کلاس و پایه/رشته را وارد کنید.");
                return;
            }
            if (tempSubjectHours.isEmpty()) {
                errorLabel.setText("حداقل یک درس به کلاس اضافه کنید.");
                return;
            }
            errorLabel.setText("");
            appData.getClasses().add(new SchoolClass(
                    appData.newClassId(), name, grade, new ArrayList<>(tempSubjectHours)));
            nameField.clear();
            gradeField.clear();
            tempSubjectHours.clear();
        });

        VBox form = new VBox(8,
                new Label("افزودن کلاس جدید"),
                new HBox(8, new Label("نام کلاس:"), nameField, new Label("پایه/رشته:"), gradeField),
                new Label("دروس این کلاس:"),
                subjectRow,
                tempTable,
                saveClassBtn,
                errorLabel);
        form.setPadding(new Insets(0, 0, 15, 0));

        TableView<SchoolClass> classesTable = new TableView<>(appData.getClasses());
        classesTable.getColumns().add(Utils.textColumn("پایه/رشته", SchoolClass::getGrade));
        classesTable.getColumns().add(Utils.textColumn("نام کلاس", SchoolClass::getName));
        classesTable.getColumns().add(Utils.textColumn("دروس", SchoolClass::getSubjectsSummaryText));
        classesTable.getColumns().add(Utils.deleteColumn("حذف", appData.getClasses()));
        classesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        root.setTop(form);
        root.setCenter(classesTable);

        Tab tab = new Tab("کلاس‌ها", root);
        tab.setClosable(false);
        return tab;
    }
}
