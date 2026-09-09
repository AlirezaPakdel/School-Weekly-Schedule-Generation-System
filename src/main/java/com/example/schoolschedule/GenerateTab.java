package com.example.schoolschedule;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** تب «تولید برنامه»: تنظیمات کلی + دکمه تولید + نمایش برنامه‌ی هر کلاس به‌صورت جدول */
class GenerateTab {

    static Tab build(AppData appData) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Spinner<Integer> periodsSpinner = new Spinner<>(1, 12, 6);
        Spinner<Integer> lastPeriodsSpinner = new Spinner<>(0, 6, 2);

        CheckBox avoidRepeatCheck = new CheckBox("یک درس دوبار در یک روز برای یک کلاس تکرار نشود");
        avoidRepeatCheck.setSelected(true);

        Map<Day, CheckBox> workingDayChecks = new EnumMap<>(Day.class);
        HBox daysBox = new HBox(10);
        for (Day d : Day.values()) {
            CheckBox cb = new CheckBox(d.getPersianName());
            if (d != Day.THURSDAY) {
                cb.setSelected(true); // پیش‌فرض: پنجشنبه تعطیل، قابل تغییر توسط کاربر
            }
            workingDayChecks.put(d, cb);
            daysBox.getChildren().add(cb);
        }

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-weight: bold;");

        TabPane resultsPane = new TabPane();

        Button generateBtn = new Button("تولید برنامه هفتگی");
        generateBtn.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        generateBtn.setOnAction(e -> {
            resultsPane.getTabs().clear();

            if (appData.getClasses().isEmpty()) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("هیچ کلاسی تعریف نشده است. اول از تب «کلاس‌ها» کلاس اضافه کنید.");
                return;
            }

            List<Day> workingDays = new ArrayList<>();
            for (Day d : Day.values()) {
                if (workingDayChecks.get(d).isSelected()) {
                    workingDays.add(d);
                }
            }
            if (workingDays.isEmpty()) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("حداقل یک روز کاری انتخاب کنید.");
                return;
            }

            TimetableGenerator generator = new TimetableGenerator(
                    new ArrayList<>(appData.getClasses()),
                    workingDays,
                    periodsSpinner.getValue(),
                    lastPeriodsSpinner.getValue(),
                    avoidRepeatCheck.isSelected());

            Optional<SchoolTimetable> result = generator.generate();

            if (result.isEmpty()) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("با محدودیت‌های فعلی نمی‌توان برنامه‌ای ساخت. "
                        + "تعداد/دسترسی معلم‌ها را بیشتر کنید یا ساعت هفتگی دروس را کم کنید.");
                return;
            }

            statusLabel.setTextFill(Color.GREEN);
            statusLabel.setText("برنامه با موفقیت ساخته شد.");

            SchoolTimetable timetable = result.get();
            for (SchoolClass sc : appData.getClasses()) {
                GridPane grid = buildGridFor(sc, timetable, workingDays, periodsSpinner.getValue());
                ScrollPane scroll = new ScrollPane(grid);
                Tab classTab = new Tab(sc.getFullTitle(), scroll);
                classTab.setClosable(false);
                resultsPane.getTabs().add(classTab);
            }
        });

        VBox settingsBox = new VBox(8,
                new Label("تنظیمات برنامه‌ریزی"),
                new HBox(8,
                        new Label("تعداد زنگ در روز:"), periodsSpinner,
                        new Label("تعداد زنگ آخر که برای درس تخصصی ترجیح داده نمی‌شود:"), lastPeriodsSpinner),
                avoidRepeatCheck,
                new Label("روزهای کاری هفته:"),
                daysBox,
                generateBtn,
                statusLabel);
        settingsBox.setPadding(new Insets(0, 0, 15, 0));

        root.setTop(settingsBox);
        root.setCenter(resultsPane);

        Tab tab = new Tab("تولید برنامه", root);
        tab.setClosable(false);
        return tab;
    }

    private static GridPane buildGridFor(SchoolClass sc, SchoolTimetable timetable,
                                          List<Day> workingDays, int periods) {
        Map<TimeSlot, ScheduleEntry> bySlot = new HashMap<>();
        for (ScheduleEntry entry : timetable.getEntries()) {
            if (entry.getSchoolClass().equals(sc)) {
                bySlot.put(entry.getTimeSlot(), entry);
            }
        }

        GridPane grid = new GridPane();
        grid.setHgap(4);
        grid.setVgap(4);
        grid.setPadding(new Insets(10));

        Label cornerLabel = new Label("زنگ \\ روز");
        cornerLabel.setStyle("-fx-font-weight: bold;");
        grid.add(cornerLabel, 0, 0);

        for (int i = 0; i < workingDays.size(); i++) {
            Label dayLabel = new Label(workingDays.get(i).getPersianName());
            dayLabel.setStyle("-fx-font-weight: bold;");
            grid.add(dayLabel, i + 1, 0);
        }

        for (int p = 1; p <= periods; p++) {
            Label periodLabel = new Label("زنگ " + p);
            periodLabel.setStyle("-fx-font-weight: bold;");
            grid.add(periodLabel, 0, p);

            for (int i = 0; i < workingDays.size(); i++) {
                Day day = workingDays.get(i);
                ScheduleEntry entry = bySlot.get(new TimeSlot(day, p));
                Label cell;
                if (entry != null) {
                    cell = new Label(entry.getSubject().getName() + "\n" + entry.getTeacher().getName());
                    cell.setStyle("-fx-border-color: #999999; -fx-padding: 6; -fx-background-color: #eef7ee;");
                } else {
                    cell = new Label("-");
                    cell.setStyle("-fx-border-color: #cccccc; -fx-padding: 6;");
                }
                cell.setPrefWidth(120);
                cell.setAlignment(Pos.CENTER);
                grid.add(cell, i + 1, p);
            }
        }
        return grid;
    }
}
