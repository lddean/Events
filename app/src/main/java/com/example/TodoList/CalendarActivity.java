package com.example.TodoList;

import java.util.Calendar;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.os.Bundle;


public class CalendarActivity extends Activity implements View.OnClickListener{
    private Button mReadUserButton;
    private Button mReadEventButton;
    private Button mWriteEventButton;

    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        mReadUserButton = (Button) findViewById(R.id.readUserButton);
        mReadEventButton = (Button)findViewById(R.id.readEventButton);
        mWriteEventButton = (Button)findViewById(R.id.writeEventButton);
        mReadUserButton.setOnClickListener(CalendarActivity.this);
        mReadEventButton.setOnClickListener(CalendarActivity.this);
        mWriteEventButton.setOnClickListener(CalendarActivity.this);
    }

    @Override
    public void onClick(View v) {
        if(v == mReadUserButton){

            Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null,
                    null, null, null);
            if(userCursor.getCount() > 0){
                userCursor.moveToFirst();
                String userName = userCursor.getString(userCursor.getColumnIndex("name"));
                Toast.makeText(CalendarActivity.this, userName, Toast.LENGTH_LONG).show();
            }
        }else if(v == mReadEventButton){
            Cursor eventCursor = getContentResolver().query(Uri.parse(calanderEventURL), null,
                    null, null, null);
            if(eventCursor.getCount() > 0){
                eventCursor.moveToLast();
                String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
                Toast.makeText(CalendarActivity.this, eventTitle, Toast.LENGTH_LONG).show();
            }
        }else if(v == mWriteEventButton){
            //获取要出入的gmail账户的id
            String calId = "";
            Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null,
                    null, null, null);
            if(userCursor.getCount() > 0){
                userCursor.moveToFirst();
                calId = userCursor.getString(userCursor.getColumnIndex("_id"));

            }
            ContentValues event = new ContentValues();
            event.put("title", "与苍井空小姐动作交流");
            event.put("description", "Frankie受空姐邀请,今天晚上10点以后将在Sheraton动作交流.lol~");
            //插入hoohbood@gmail.com这个账户
            event.put("calendar_id",calId);

            Calendar mCalendar = Calendar.getInstance();
            mCalendar.set(Calendar.HOUR_OF_DAY,10);
            long start = mCalendar.getTime().getTime();
            mCalendar.set(Calendar.HOUR_OF_DAY,11);
            long end = mCalendar.getTime().getTime();

            event.put("dtstart", start);
            event.put("dtend", end);
            event.put("hasAlarm",1);

            Uri newEvent = getContentResolver().insert(Uri.parse(calanderEventURL), event);
            long id = Long.parseLong( newEvent.getLastPathSegment() );
            ContentValues values = new ContentValues();
            values.put( "event_id", id );
            //提前10分钟有提醒
            values.put( "minutes", 10 );
            getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
            Toast.makeText(CalendarActivity.this, "插入事件成功!!!", Toast.LENGTH_LONG).show();
        }
    }

}
