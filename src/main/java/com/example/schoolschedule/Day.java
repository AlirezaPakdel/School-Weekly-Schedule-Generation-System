package com.example.schoolschedule;

/** روزهای هفته‌ی مدرسه به همراه نام نمایشی فارسی */
public enum Day {
    SATURDAY("شنبه"),
    SUNDAY("یکشنبه"),
    MONDAY("دوشنبه"),
    TUESDAY("سه‌شنبه"),
    WEDNESDAY("چهارشنبه"),
    THURSDAY("پنجشنبه");

    private final String persianName;

    Day(String persianName) {
        this.persianName = persianName;
    }

    public String getPersianName() {
        return persianName;
    }

    @Override
    public String toString() {
        return persianName;
    }
}
