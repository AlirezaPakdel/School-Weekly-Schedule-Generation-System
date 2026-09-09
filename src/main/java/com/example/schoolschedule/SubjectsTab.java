package com.example.schoolschedule;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/** تب «دروس»: افزودن درس جدید، مشخص کردن تخصصی بودن و انتخاب چندگانه‌ی معلم‌های آن */
class SubjectsTab {

    static Tab build(AppData appData) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        TextField nameField = new TextField();
        nameField.setPromptText("نام درس");

        CheckBox specializedCheck = new CheckBox("درس تخصصی است (ترجیحاً آخر وقت قرار نگیرد)");

        ListView<Teacher> teacherList = new ListView<>(appData.getTeachers());
        teacherList.setPrefHeight(120);
        teacherList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button addBtn = new Button("افزودن درس");
        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                errorLabel.setText("نام درس را وارد کنید.");
                return;
            }
            List<Teacher> selectedTeachers = new ArrayList<>(teacherList.getSelectionModel().getSelectedItems());
            if (selectedTeachers.isEmpty()) {
                errorLabel.setText("حداقل یک معلم برای این درس انتخاب کنید (از تب معلم‌ها اضافه کرده باشید).");
                return;
            }
            errorLabel.setText("");
            appData.getSubjects().add(new Subject(
                    appData.newSubjectId(), name, specializedCheck.isSelected(), selectedTeachers));
            nameField.clear();
            specializedCheck.setSelected(false);
            teacherList.getSelectionModel().clearSelection();
        });

        VBox form = new VBox(8,
                new Label("افزودن درس جدید"),
                nameField,
                specializedCheck,
                new Label("معلم‌های این درس (چند مورد را می‌توانید انتخاب کنید):"),
                teacherList,
                addBtn,
                errorLabel);
        form.setPadding(new Insets(0, 0, 15, 0));

        TableView<Subject> table = new TableView<>(appData.getSubjects());
        table.getColumns().add(Utils.textColumn("نام درس", Subject::getName));
        table.getColumns().add(Utils.textColumn("تخصصی", Subject::getSpecializedText));
        table.getColumns().add(Utils.textColumn("معلم‌ها", Subject::getTeachersText));
        table.getColumns().add(Utils.deleteColumn("حذف", appData.getSubjects()));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        root.setTop(form);
        root.setCenter(table);

        Tab tab = new Tab("دروس", root);
        tab.setClosable(false);
        return tab;
    }
}
