package com.example.schoolschedule;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * نقطه‌ی شروع برنامه. تمام اطلاعات (معلم، درس، کلاس) از طریق فرم‌های
 * موجود در تب‌ها وارد می‌شود؛ هیچ داده‌ای به‌صورت ثابت در کد نوشته نشده است.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        AppData appData = new AppData();

        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(TeachersTab.build(appData));
        tabPane.getTabs().add(SubjectsTab.build(appData));
        tabPane.getTabs().add(ClassesTab.build(appData));
        tabPane.getTabs().add(GenerateTab.build(appData));

        Scene scene = new Scene(tabPane, 1050, 680);
        primaryStage.setTitle("سیستم تولید برنامه هفتگی مدرسه");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
