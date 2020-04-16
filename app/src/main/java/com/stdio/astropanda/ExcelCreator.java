package com.stdio.astropanda;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.stdio.astropanda.gmailHelper.GMailSender;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;

public class ExcelCreator {

    static Context context;
    static SharedPreferences prefs;
    static Activity mActivity;
    private static String recipient = "martiality.me@yandex.ru";
    private static String senderMail = "finic.app@gmail.com";
    private static String senderPassword = "yourpassword";

    public static void createExcelFile(Context context, String name, SharedPreferences prefs) throws ParseException {

        ExcelCreator.context = context;
        ExcelCreator.prefs = prefs;

        // создание самого excel файла в памяти
        XSSFWorkbook workbook = new XSSFWorkbook();
        // создание листа с названием "Просто лист"
        XSSFSheet sheet = workbook.createSheet("List");

        // счетчик для строк
        int rowNum = 0;

        // создаем подписи к столбцам (это будет первая строчка в листе Excel файла)
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue("Имя");
        row.createCell(1).setCellValue(context.getString(R.string.etDate));
        row.createCell(2).setCellValue(context.getString(R.string.etEmail));
        row.createCell(3).setCellValue(context.getString(R.string.q1));
        row.createCell(4).setCellValue(context.getString(R.string.q2));
        row.createCell(5).setCellValue(context.getString(R.string.q3));
        row.createCell(6).setCellValue(context.getString(R.string.q4));
        row.createCell(7).setCellValue(context.getString(R.string.q5));
        row.createCell(8).setCellValue(context.getString(R.string.q6));
        row.createCell(9).setCellValue(context.getString(R.string.q7));
        row.createCell(10).setCellValue(context.getString(R.string.q8));
        row.createCell(11).setCellValue(context.getString(R.string.q9));

        createSheetHeader(sheet, ++rowNum);

        // записываем созданный в памяти Excel документ в файл
        File file = new File(context.getExternalFilesDir(null), name+".xlsx");
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            workbook.write(os);
            Log.w("FileUtils", "Writing file" + file);
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        sendMessage(file);
    }

    //if all survey is passed
    public static void createExcelFile(Activity activity, String name, SharedPreferences prefs) throws ParseException {
        mActivity = activity;
        ExcelCreator.prefs = prefs;
        ExcelCreator.context = activity;
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("List");
        int rowNum = 0;

        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue("Имя");
        row.createCell(1).setCellValue(activity.getString(R.string.etDate));
        row.createCell(2).setCellValue(activity.getString(R.string.etEmail));
        row.createCell(3).setCellValue(activity.getString(R.string.q1));
        row.createCell(4).setCellValue(activity.getString(R.string.q2));
        row.createCell(5).setCellValue(activity.getString(R.string.q3));
        row.createCell(6).setCellValue(activity.getString(R.string.q4));
        row.createCell(7).setCellValue(activity.getString(R.string.q5));
        row.createCell(8).setCellValue(activity.getString(R.string.q6));
        row.createCell(9).setCellValue(activity.getString(R.string.q7));
        row.createCell(10).setCellValue(activity.getString(R.string.q8));
        row.createCell(11).setCellValue(activity.getString(R.string.q9));

        createSheetHeader(sheet, ++rowNum);
        File file = new File(activity.getExternalFilesDir(null), name+".xlsx");
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            workbook.write(os);
            Log.w("FileUtils", "Writing file" + file);
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        sendMessage(file);
    }

    // заполнение строки (rowNum) определенного листа (sheet)
    // данными  из dataModel созданного в памяти Excel файла
    private static void createSheetHeader(XSSFSheet sheet, int rowNum) {
        Row row = sheet.createRow(rowNum);

        for (int i = 0; i < MainActivity.message.size(); i++) {
            row.createCell(i).setCellValue(MainActivity.message.get(i));
        }
    }

    private static void sendMessage(final File excelFile) {
        ProgressDialog dialog = null;
        if (prefs != null) {
            dialog = new ProgressDialog(context);
            dialog.setTitle("Sending Email");
            dialog.setMessage("Please wait");
            dialog.show();
        }
        final ProgressDialog finalDialog = dialog;
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(senderMail, senderPassword);
                    sender.sendMail(context.getString(R.string.app_name), excelFile,
                            senderMail,
                            recipient);
                    if (finalDialog != null) {
                        finalDialog.dismiss();
                    }
                    if (prefs != null) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("moneyCount", prefs.getInt("moneyCount", 0) + 170);
                        editor.apply();
                        mActivity.startActivity(new Intent(mActivity, CompleteActivity.class));
                        mActivity.finish();
                    }
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }
}