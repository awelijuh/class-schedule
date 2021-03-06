package com.example.awelijuh.classschedule;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;

public class Parser {
    Context context;
    Workbook workbook;
    boolean valid = false;


    Parser(Context context) {
        this.context = context;
        parse();
    }

    private void parse() {
        try {
            String path = context.getCacheDir() + "/" + "schedule";
            File file = new File(path);
            workbook = Workbook.getWorkbook(file);
            valid = true;
        } catch (Exception e) {
            valid = false;
        }

    }

    public String[] getSheetNames() {
        try {
            return workbook.getSheetNames();
        } catch (Exception e) {
            return null;
        }
    }

    public String[] getGroupNames(int numSheet) {
        try {
            if (!isValid()) {
                return null;
            }
            Sheet sheet = workbook.getSheet(numSheet);
            ArrayList<String> list = new ArrayList<>();
            for (int i = 1; i < sheet.getColumns(); i++) {
                Cell cell = sheet.getCell(i, 3);
                if (cell != null && !cell.getContents().isEmpty()) {
                    list.add(cell.getContents());
                }
            }

            return list.toArray(new String[0]);
        } catch (Exception e) {
            return null;
        }
    }


    public String[] getDateNames(int numSheet) {
        try {
            Sheet sheet = workbook.getSheet(numSheet);
            String[] dates = new String[6];
            dates[0] = sheet.getCell(0, 4).getContents() + "\n" + sheet.getCell(1, 4).getContents();
            dates[1] = sheet.getCell(0, 9).getContents() + "\n" + sheet.getCell(1, 9).getContents();
            dates[2] = sheet.getCell(0, 14).getContents() + "\n" + sheet.getCell(1, 14).getContents();
            dates[3] = sheet.getCell(0, 19).getContents() + "\n" + sheet.getCell(1, 19).getContents();
            dates[4] = sheet.getCell(0, 24).getContents() + "\n" + sheet.getCell(1, 24).getContents();
            dates[5] = sheet.getCell(0, 29).getContents() + "\n" + sheet.getCell(1, 29).getContents();
            return dates;
        } catch (Exception e) {
            return null;
        }
    }

    public String[] getTimePeriod(int numSheet, int numDay) {
        try {
            Sheet sheet = workbook.getSheet(numSheet);
            String[] periods = new String[4];
            for (int i = 0; i < 4; i++) {
                periods[i] = sheet.getCell(0, (numDay + 1) * 5 + i).getContents();
            }
            return periods;
        } catch (Exception e) {
            return null;
        }
    }

    public String[] getSchedule(int numSheet, int numGroup, int numDay) {
        try {
            String[] schedule = new String[4];
            for (int i = 0; i < 4; i++) {
                schedule[i] = getCellContents(numSheet, numGroup + 1, (numDay + 1) * 5 + i);
            }
            return schedule;
        } catch (Exception e) {
            return null;
        }
    }


    private String getCellContents(int numSheet, int column, int row) {
        try {
            Sheet sheet = workbook.getSheet(numSheet);
            String s = sheet.getCell(column, row).getContents();
            if (!s.isEmpty()) {
                return s;
            }
            for (Range range : sheet.getMergedCells()) {
                int row0 = range.getTopLeft().getRow();
                int row1 = range.getBottomRight().getRow();
                int column0 = range.getTopLeft().getColumn();
                int column1 = range.getBottomRight().getColumn();
                if (row0 <= row && row <= row1 && column0 <= column && column <= column1) {
                    return range.getTopLeft().getContents();
                }
            }
            return "";
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isValid() {
        return valid;
    }
}