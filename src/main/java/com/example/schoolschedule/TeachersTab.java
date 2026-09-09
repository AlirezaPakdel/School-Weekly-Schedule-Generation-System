package com.example.schoolschedule;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/** تب «معلم‌ها»: افزودن معلم جدید با نام و روزهای در دسترس، و نمایش/حذف لیست معلم‌ها */
class TeachersTab {

    static Tab build(AppData appData) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        TextField nameField = new TextField();
        nameField.setPromptText("نام معلم");

        Map<Day, CheckBox> dayChecks = new EnumMap<>(Day.class);
        HBox daysBox = new HBox(10);
        for (Day d : Day.values()) {
            CheckBox cb = new CheckBox(d.getPersianName());
            dayChecks.put(d, cb);
            daysBox.getChildren().add(cb);
        }

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button addBtn = new Button("افزودن معلم");
        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                errorLabel.setText("نام معلم را وارد کنید.");
                return;
            }
            Set<Day> selected = EnumSet.noneOf(Day.class);
            for (Map.Entry<Day, CheckBox> entry : dayChecks.entrySet()) {
                if (entry.getValue().isSelected()) {
                    selected.add(entry.getKey());
                }
            }
            if (selected.isEmpty()) {
                errorLabel.setText("حداقل یک روز در دسترس را انتخاب کنید.");
                return;
            }
            errorLabel.setText("");
            appData.getTeachers().add(new Teacher(appData.newTeacherId(), name, selected));
            nameField.clear();
            for (CheckBox cb : dayChecks.values()) {
                cb.setSelected(false);
            }
        });

        VBox form = new VBox(8,
                new Label("افزودن معلم جدید"),
                nameField,
                new Label("روزهای در دسترس:"),
                daysBox,
                addBtn,
                errorLabel);
        form.setPadding(new Insets(0, 0, 15, 0));

        TableView<Teacher> table = new TableView<>(appData.getTeachers());
        table.getColumns().add(Utils.textColumn("نام", Teacher::getName));
        table.getColumns().add(Utils.textColumn("روزهای در دسترس", Teacher::getAvailableDaysText));
        table.getColumns().add(Utils.deleteColumn("حذف", appData.getTeachers()));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        root.setTop(form);
        root.setCenter(table);

        Tab tab = new Tab("معلم‌ها", root);
        tab.setClosable(false);
        return tab;
    }
}
