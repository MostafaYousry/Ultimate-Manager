package com.example.android.employeesmanagementapp.utils;

import com.example.android.employeesmanagementapp.R;

public final class ColorUtils {
    private static int[] taskColorResources = new int[12];
    private static int[] accent100Values = new int[16];
    private static int[] accent700Values = new int[16];


    static {

        taskColorResources[0] = R.color.task_color_1;
        taskColorResources[1] = R.color.task_color_2;
        taskColorResources[2] = R.color.task_color_3;
        taskColorResources[3] = R.color.task_color_4;
        taskColorResources[4] = R.color.task_color_5;
        taskColorResources[5] = R.color.task_color_6;
        taskColorResources[6] = R.color.task_color_7;
        taskColorResources[7] = R.color.task_color_8;
        taskColorResources[8] = R.color.task_color_9;
        taskColorResources[9] = R.color.task_color_10;
        taskColorResources[10] = R.color.task_color_11;
        taskColorResources[11] = R.color.task_color_12;

        accent100Values[0] = R.color.A100_1;
        accent100Values[1] = R.color.A100_2;
        accent100Values[2] = R.color.A100_3;
        accent100Values[3] = R.color.A100_4;
        accent100Values[4] = R.color.A100_5;
        accent100Values[5] = R.color.A100_6;
        accent100Values[6] = R.color.A100_7;
        accent100Values[7] = R.color.A100_8;
        accent100Values[8] = R.color.A100_9;
        accent100Values[9] = R.color.A100_10;
        accent100Values[10] = R.color.A100_11;
        accent100Values[11] = R.color.A100_12;
        accent100Values[12] = R.color.A100_13;
        accent100Values[13] = R.color.A100_14;
        accent100Values[14] = R.color.A100_15;
        accent100Values[15] = R.color.A100_16;


        accent700Values[0] = R.color.A700_1;
        accent700Values[1] = R.color.A700_2;
        accent700Values[2] = R.color.A700_3;
        accent700Values[3] = R.color.A700_4;
        accent700Values[4] = R.color.A700_5;
        accent700Values[5] = R.color.A700_6;
        accent700Values[6] = R.color.A700_7;
        accent700Values[7] = R.color.A700_8;
        accent700Values[8] = R.color.A700_9;
        accent700Values[9] = R.color.A700_10;
        accent700Values[10] = R.color.A700_11;
        accent700Values[11] = R.color.A700_12;
        accent700Values[12] = R.color.A700_13;
        accent700Values[13] = R.color.A700_14;
        accent700Values[14] = R.color.A700_15;
        accent700Values[15] = R.color.A700_16;

    }

    public static int[] getTaskColorResources() {
        return taskColorResources;
    }

    public static int getLetterColor(Object object) {
        return accent700Values[Math.abs(object.hashCode()) % accent700Values.length];
    }

    public static int getLetterBackgroundColor(Object object) {
        return accent100Values[Math.abs(object.hashCode()) % accent100Values.length];
    }
}
