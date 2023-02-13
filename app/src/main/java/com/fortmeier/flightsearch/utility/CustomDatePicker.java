package com.fortmeier.flightsearch.utility;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;

public class CustomDatePicker {

    private DatePickerDialog datePickerDialog;

    public void initDatePicker(Button dayOfDeparture, Context context) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dayOfDeparture.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(context, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);

    }

    private String makeDateString(int day, int month, int year) {
        return day + ". " + getMonthFormat(month) + " " +  year;
    }

    private String getMonthFormat(int month){
        if(month == 1) return "JAN";
        if(month == 2) return "FEB";
        if(month == 3) return "MAR";
        if(month == 4) return "APR";
        if(month == 5) return "MAY";
        if(month == 6) return "JUN";
        if(month == 7) return "JUL";
        if(month == 8) return "AUG";
        if(month == 9) return "SEP";
        if(month == 10) return "OKT";
        if(month == 11) return "NOV";
        if(month == 12) return "DEZ";

        return "JAN";
    }


    public String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day,month,year);
    }

    public DatePickerDialog getDatePickerDialog() {
        return datePickerDialog;
    }

    public Date getDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePickerDialog.getDatePicker().getYear(), datePickerDialog.getDatePicker().getMonth(), datePickerDialog.getDatePicker().getDayOfMonth());
        Date date = calendar.getTime();
        return date;
    }


}
