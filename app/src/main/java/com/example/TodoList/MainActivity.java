package com.example.TodoList;

import java.util.Calendar;
import java.text.SimpleDateFormat;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.DatePicker;
import android.net.Uri;
import android.widget.DatePicker.OnDateChangedListener;

import com.example.TodoList.R.layout;
import com.example.TodoList.db.TaskContract;
import com.example.TodoList.db.TaskDBHelper;
import android.content.Intent;


public class MainActivity extends ListActivity {
	private ListAdapter listAdapter;
	private TaskDBHelper helper;
	private int Task_year;
	private int Task_month;
	private int Task_day;
    private DatePicker datePicker;
	private  boolean passed;
    private Button but1;

    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		updateUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_add_task:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);

                break;
            case R.id.action_add_user:
                Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null,
                        null, null, null);
                if(userCursor.getCount() > 0) {
                    userCursor.moveToFirst();
                    String userName = userCursor.getString(userCursor.getColumnIndex("name"));
                    Toast.makeText(this, userName, Toast.LENGTH_LONG).show();
                }
                break;
			default:
				return false;
		}
        return false;
	}

	private void jumpToSetting() {
        /*
        datePicker.init(year,month, day,new OnDateChangedListener(){
	    	
	    	@Override
	    	public void onDateChanged(DatePicker view,int years, int monthOfYear,
	    			int dayOfMonth){
	    		Calendar calendar = Calendar.getInstance();
	    		calendar.set(years, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy年MM月dd日  HH:mm");
                Toast.makeText(MainActivity.this,
                        format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();

	    		Task_year = years;
	    		Task_month = monthOfYear;
	    		Task_day = dayOfMonth;
	 
	    	}
	    	
	    });*/
	    
	    Button but1 = (Button) findViewById(R.id.button1);
        /*
	    but1.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
            	EditText text = (EditText) findViewById(R.id.editText1);
        		String task = text.getText().toString();
            	helper = new TaskDBHelper(MainActivity.this);
				SQLiteDatabase db = helper.getWritableDatabase();
				ContentValues values = new ContentValues();

				values.clear();
				values.put(TaskContract.Columns.TASK,task);

				db.insertWithOnConflict(TaskContract.TABLE,null,values,SQLiteDatabase.CONFLICT_IGNORE);
				updateUI();
            }

        });
	    */

	    
	}

	protected void jumpBack() {
		setContentView(R.layout.main);
	}

	private void updateUI() {
		helper = new TaskDBHelper(MainActivity.this);
		SQLiteDatabase sqlDB = helper.getReadableDatabase();
		Cursor cursor = sqlDB.query(TaskContract.TABLE,
				new String[]{TaskContract.Columns._ID,
                        TaskContract.Columns.TASK,
                        TaskContract.Columns.Task_date},
				null, null, null, null, null);


		listAdapter = new SimpleCursorAdapter(
				this,
				R.layout.task_view,
				cursor,
				new String[]{TaskContract.Columns.TASK, TaskContract.Columns.Task_date},
				new int[]{R.id.taskTextView,R.id.info},
				0
		);

		this.setListAdapter(listAdapter);
	}

	public void onDoneButtonClick(View view) {
        /*
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);*/
        //asdf

		View v = (View) view.getParent();
		TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
		String task = taskTextView.getText().toString();

		String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
						TaskContract.TABLE,
						TaskContract.Columns.TASK,
						task);


		helper = new TaskDBHelper(MainActivity.this);
		SQLiteDatabase sqlDB = helper.getWritableDatabase();
		sqlDB.execSQL(sql);
		updateUI();
	}

    public void onGetUserClick(View view) {

        Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null,
                null, null, null);
        if(userCursor.getCount() > 0){
            userCursor.moveToFirst();
            String userName = userCursor.getString(userCursor.getColumnIndex("name"));
            Toast.makeText(MainActivity.this, userName, Toast.LENGTH_LONG).show();
        }

    }
    public String calculate(int Task_year, int Task_month, int Task_day){

        passed = true;
        int y,m,d;
        Calendar cal=Calendar.getInstance();
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DATE);
        return "asdfaf";
    }
}
