package com.example.schoolschedule;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;

/** توابع کمکی برای ساده‌سازی ساخت جدول‌ها در تب‌های مختلف */
final class Utils {
    private Utils() { }

    /** ستون متنی ساده که مقدارش از یک تابع استخراج می‌شود (بدون نیاز به JavaFX Property) */
    static <S> TableColumn<S, String> textColumn(String title, Function<S, String> extractor) {
        TableColumn<S, String> col = new TableColumn<>(title);
        col.setCellValueFactory(data -> new SimpleStringProperty(extractor.apply(data.getValue())));
        return col;
    }

    /** ستونی با یک دکمه‌ی «حذف» که آیتم را از لیست observable مربوطه حذف می‌کند */
    static <S> TableColumn<S, Void> deleteColumn(String title, ObservableList<S> list) {
        TableColumn<S, Void> col = new TableColumn<>(title);
        col.setSortable(false);
        col.setCellFactory(new Callback<TableColumn<S, Void>, TableCell<S, Void>>() {
            @Override
            public TableCell<S, Void> call(TableColumn<S, Void> column) {
                return new TableCell<S, Void>() {
                    private final Button button = new Button("حذف");
                    {
                        button.setOnAction(e -> {
                            S item = getTableView().getItems().get(getIndex());
                            list.remove(item);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : button);
                    }
                };
            }
        });
        return col;
    }
}
