package com.example.TodoList;

import android.app.DatePickerDialog;
import com.example.TodoList.MainActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.*;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.TodoList.db.TaskContract;
import com.example.TodoList.db.TaskDBHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.ContentValues;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.format.Time;


import static com.example.TodoList.db.TaskContract.*;

public class SettingActivity extends Activity implements View.OnClickListener {
    private DatePicker datePicker;
    private TaskDBHelper helper;
    //private TimePicker timePicker;
    private int Task_year;
    private int Task_month;
    private int Task_day;
    private String Task_repeat;
    private Button btnDate,but1,but2,but3;
    private boolean condition = false;
    //private TextView t;


    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        //datePicker = (DatePicker) findViewById(R.id.datePicker1);
        btnDate = (Button) findViewById(R.id.btnDatePickerDialog);
        btnDate.setOnClickListener(this);
        but1 = (Button) findViewById(R.id.button1);

        but2 = (Button) findViewById(R.id.button2);
        but2 = (Button) findViewById(R.id.choose);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDatePickerDialog:
                int y,m,d;
                Calendar cal=Calendar.getInstance();
                y = cal.get(Calendar.YEAR);
                m = cal.get(Calendar.MONTH);
                d = cal.get(Calendar.DATE);
                DatePickerDialog datePicker=new DatePickerDialog(SettingActivity.this, new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                        Toast.makeText(SettingActivity.this, year+"year "+ monthOfYear+"month "+dayOfMonth+"day", Toast.LENGTH_SHORT).show();
                        Task_year = year;
                        Task_month = monthOfYear + 1;
                        Task_day = dayOfMonth;
                        condition = true;
                        if (condition){
                            showDate(Task_year,Task_month,Task_day);
                        }
                            }
                        }, y, m, d);
                datePicker.show();

                break;

            case R.id.button1:
                but1.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        EditText text = (EditText) findViewById(R.id.editText1);
                        String task = text.getText().toString();
                        helper = new TaskDBHelper(SettingActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        String s = "" + Task_year + "." + Task_month + "." +Task_day;
                        values.clear();
                        values.put(TaskContract.Columns.TASK,task);
                        values.put(TaskContract.Columns.Day, Task_day);// String.valueOf(Task_day));
                        values.put(TaskContract.Columns.Month, Task_month);
                        values.put(TaskContract.Columns.Year, Task_year);
                        values.put(TaskContract.Columns.Task_date, s);
                        values.put(TaskContract.Columns.Repeat, Task_repeat);

                        db.insertWithOnConflict(TABLE,null,values,SQLiteDatabase.CONFLICT_IGNORE);

                        String calId = "";
                        Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null,
                                null, null, null);
                        if(userCursor.getCount() > 0){
                            userCursor.moveToFirst();
                            calId = userCursor.getString(userCursor.getColumnIndex("_id"));

                        }
                        ContentValues event = new ContentValues();
                        event.put("title", task);
                        event.put("description", "    ");
                        event.put("calendar_id",calId);

                        Calendar mCalendar = Calendar.getInstance();
                        mCalendar.set(Task_year,Task_month - 1,Task_day);
                        mCalendar.set(Calendar.HOUR_OF_DAY,10);
                        long start = mCalendar.getTime().getTime();
                        mCalendar.set(Calendar.HOUR_OF_DAY,11);
                        long end = mCalendar.getTime().getTime();

                        event.put("dtstart", start);
                        event.put("dtend", end);
                        event.put("hasAlarm",1);
                        event.put("eventTimezone", Time.getCurrentTimezone());

                        Uri newEvent = getContentResolver().insert(Uri.parse(calanderEventURL), event);
                        long id = Long.parseLong( newEvent.getLastPathSegment() );
                        ContentValues values1 = new ContentValues();
                        values1.put( "event_id", id );
                        values1.put( "method", 1 );
                        values1.put( "minutes", 10 );
                        getContentResolver().insert(Uri.parse(calanderRemiderURL), values1);
                        Toast.makeText(SettingActivity.this, "Successful", Toast.LENGTH_LONG).show();


                        Intent intent;
                        intent = new Intent(SettingActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                });
                break;
            case R.id.button2:
                but2.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        intent = new Intent(SettingActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                });
                break;
            case R.id.choose:
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("Choose");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                final String[] r = {"Every Year", "Every Month", "Every Week", "None"};
                builder.setSingleChoiceItems(r, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Task_repeat = r[which];
                        ShowRepeat(Task_repeat);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Task_repeat = "None";
                        ShowRepeat(Task_repeat);
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;

            default:
                return;
        }

    }

    private void ShowRepeat(String task_repeat) {

        TextView show=(TextView)findViewById(R.id.repeat);
        show.setText(task_repeat);

    }

    private void showDate(int year,int month,int day)
    {
        TextView show=(TextView)findViewById(R.id.show);
        show.setText(""+year+"."+ month +"."+day+".");

    }


}