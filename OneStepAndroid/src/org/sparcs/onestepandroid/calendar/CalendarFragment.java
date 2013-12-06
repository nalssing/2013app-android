package org.sparcs.onestepandroid.calendar;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.sitesuggestion.SiteSuggestionInfo;
import org.sparcs.onestepandroid.sitesuggestion.SiteSuggestionListAdapter;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;
import org.sparcs.onestepandroid.util.XmlParser;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class CalendarFragment extends Fragment implements OnItemClickListener, OnClickListener {
	 private static final boolean DEBUG = true;
		private static final String TAG = CalendarFragment.class.getSimpleName();
		
		public static int SUNDAY        = 1;
	    public static int MONDAY        = 2;
	    public static int TUESDAY       = 3;
	    public static int WEDNSESDAY    = 4;
	    public static int THURSDAY      = 5;
	    public static int FRIDAY        = 6;
	    public static int SATURDAY      = 7;
	    private String[] DateFormatter;
	     
	    private TextView mTvCalendarTitle;
	    private TextView mTvNextMonthTitle;
	    private TextView mTvLastMonthTitle;
	    private GridView mGvCalendar;
	    private GridView weekTitle;
	    
	    private ArrayList<EventInfo> data;
	    private ArrayList<DayInfo> mDayList;
	    private CalendarAdapter mCalendarAdapter;
	    
	    private ArrayList<WeekdayInfo> mWeekdayList;
	    private WeekdayAdapter mWeekdayAdapter;
	    	     
	    Calendar mLastMonthCalendar;
	    Calendar mThisMonthCalendar;
	    Calendar mNextMonthCalendar;
	    
	    private String test;
	    
	    private View selected;
	    private View view;
	    private Thread thread;
	     
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        view = inflater.inflate(R.layout.calendar, container, false);
	        DateFormatter = new DateFormatSymbols(Locale.KOREAN).getShortMonths();
	        
	        mTvLastMonthTitle = (TextView)view.findViewById(R.id.gv_calendar_activity_b_last);
	        mTvNextMonthTitle = (TextView)view.findViewById(R.id.gv_calendar_activity_b_next);
	         
	        mTvCalendarTitle = (TextView)view.findViewById(R.id.gv_calendar_activity_tv_title);
	        mGvCalendar = (GridView)view.findViewById(R.id.gv_calendar_activity_gv_calendar);
	        
	        weekTitle = (GridView) view.findViewById(R.id.gv_calendar_weekdays);
	        mWeekdayList = new ArrayList<WeekdayInfo>();
	        String[] WeekdayList = getResources().getStringArray(R.array.weekdays);
	        for(int i=0;i<7;i++)
	        {
	        	mWeekdayList.add(new WeekdayInfo(WeekdayList[i]));
	        }
	        mWeekdayAdapter = new WeekdayAdapter(getActivity(), R.layout.weekday,mWeekdayList);
	        weekTitle.setAdapter(mWeekdayAdapter);
	          
	        mTvLastMonthTitle.setOnClickListener(this);
	        mTvNextMonthTitle.setOnClickListener(this);
	        mGvCalendar.setOnItemClickListener(this);
	 
	        mDayList = new ArrayList<DayInfo>();
	        selected = null;
	        
	        //TextView loading = (TextView) view.findViewById(R.id.calendar_event_text);
	        //loading.setMovementMethod(new ScrollingMovementMethod());
	        
	        return view;
	    }
	 
	    @Override
	    public void onResume()
	    {
	        super.onResume();
	         
	        // 이번달 의 캘린더 인스턴스를 생성한다.
	        mThisMonthCalendar = Calendar.getInstance();
	        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
	        
//	        Thread thread = new Thread (new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					NetworkReturning returning = NetworkManager.INSTANCE.getMonthEvent();
//					int status = returning.getStatus();
//					data = new ArrayList<EventInfo>();
//					
//					if (status == 200) {
//						XmlParser parser = new XmlParser();
//						data.addAll(parser.parseEvent(returning.getResponse()));
//					}
//					handler.sendEmptyMessage(0);
//				}
//			});
	        
	        
	        getCalendar(mThisMonthCalendar);
	    }
	 
	    /**
	     * 달력을 셋팅한다.
	     * 
	     * @param calendar 달력에 보여지는 이번달의 Calendar 객체
	     */
	    private void getCalendar(Calendar calendar)
	    {
	        int lastMonthStartDay;
	        int dayOfMonth;
	        int thisMonthLastDay;
	         
	        mDayList.clear();
	         
	        // 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
	        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
	        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	        calendar.add(Calendar.MONTH, -1);
	 
	        // 지난달의 마지막 일자를 구한다.
	        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	        calendar.add(Calendar.MONTH, 1);

	        if(dayOfMonth == SUNDAY)
	        {
	            dayOfMonth += 7;
	        }
	         
	        lastMonthStartDay -= (dayOfMonth-1)-1;
	         
	 
	        // 캘린더 타이틀(년월 표시)을 세팅한다. 
	        mTvCalendarTitle.setText(DateFormatter[mThisMonthCalendar.get(Calendar.MONTH)]+" "+mThisMonthCalendar.get(Calendar.YEAR));
	 
	        DayInfo day;
	        calendar.add(Calendar.MONTH, -1);  
	        for(int i=0; i<dayOfMonth-1; i++)
	        {
	            int date = lastMonthStartDay+i;
	            day = new DayInfo();
	            day.setMonth(calendar.get(Calendar.MONTH));
	            day.setYear(calendar.get(Calendar.YEAR));
	            day.setDay(Integer.toString(date));
	            day.setInMonth(false);
	             
	            mDayList.add(day);
	        }
	        calendar.add(Calendar.MONTH, 1);
	        for(int i=1; i <= thisMonthLastDay; i++)
	        {
	            day = new DayInfo();
	            day.setYear(calendar.get(Calendar.YEAR));
	            day.setMonth((calendar.get(Calendar.MONTH)));
	            day.setDay(Integer.toString(i));
	            day.setInMonth(true);
	             
	            mDayList.add(day);
	        }
	        calendar.add(Calendar.MONTH, 1);
	        for(int i=1; i<42-(thisMonthLastDay+dayOfMonth-1)+1; i++)
	        {
	            day = new DayInfo();
	            day.setMonth(calendar.get(Calendar.MONTH));
	            day.setYear(calendar.get(Calendar.YEAR));
	            day.setDay(Integer.toString(i));
	            day.setInMonth(false);
	            mDayList.add(day);
	        }
	        calendar.add(Calendar.MONTH, -1);
	        
	        calendar.add(Calendar.MONTH, -1);
	        mTvLastMonthTitle.setText(DateFormatter[mThisMonthCalendar.get(Calendar.MONTH)]+" "+mThisMonthCalendar.get(Calendar.YEAR));
	        calendar.add(Calendar.MONTH, +2);
	        mTvNextMonthTitle.setText(DateFormatter[mThisMonthCalendar.get(Calendar.MONTH)]+" "+mThisMonthCalendar.get(Calendar.YEAR));
	        calendar.add(Calendar.MONTH, -1);
	        
	        final int startYear = mDayList.get(0).getYear();
	        final int startMonth = mDayList.get(0).getMonth();
	        final int startDay = Integer.parseInt(mDayList.get(0).getDay());
	        int last = mDayList.size()-1;
	        final int lastYear = mDayList.get(last).getYear();
	        final int lastMonth = mDayList.get(last).getMonth();
	        final int lastDay = Integer.parseInt(mDayList.get(last).getDay());
	        thread = new Thread(new Runnable() {
				@Override
				public void run() {
					NetworkReturning returning = NetworkManager.INSTANCE.getMonthEvent
							(startYear, startMonth+1, startDay,lastYear, lastMonth+1, lastDay);
					int status = returning.getStatus();
					Log.i("Calendar",Integer.toString(status));
					data = new ArrayList<EventInfo>();
					if (status == 500) {
						EventInfo info = new EventInfo();
						info.setTitle("서버와의 연결이 끊겼습니다.");
						info.setContent("");
						data.add(info);
					}
					else {
						if (status == 200) {
							XmlParser parser = new XmlParser();
							//Log.i("Calendar",returning.getResponse());
							ArrayList<EventInfo> parsed = parser.parseEvent(returning.getResponse());
							data = parsed;
						}
						else {
							EventInfo info = new EventInfo();
							info.setTitle("서버와의 연결이 끊겼습니다.");
							info.setContent("");
							data.add(info);
						}
					}
					handler.sendEmptyMessage(0);
				}
			});
	        thread.start();
	         
	        initCalendarAdapter();
	    }
	 
	    /**
	     * 지난달의 Calendar 객체를 반환합니다.
	     * 
	     * @param calendar
	     * @return LastMonthCalendar
	     */
	    private Calendar getLastMonth(Calendar calendar)
	    {
	    	//mTvNextMonthTitle.setText(mTvCalendarTitle.getText());
	        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
	        calendar.add(Calendar.MONTH, -1);
	        /*
	        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 " + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
	        mTvCalendarTitle.setText(DateFormatter[mThisMonthCalendar.get(Calendar.MONTH)]+" "+mThisMonthCalendar.get(Calendar.YEAR));
	        calendar.add(Calendar.MONTH, -1);
	        mTvLastMonthTitle.setText(DateFormatter[mThisMonthCalendar.get(Calendar.MONTH)]+" "+mThisMonthCalendar.get(Calendar.YEAR));
	        */
	        return calendar;
	    }
	 
	    /**
	     * 다음달의 Calendar 객체를 반환합니다.
	     * 
	     * @param calendar
	     * @return NextMonthCalendar
	     */
	    private Calendar getNextMonth(Calendar calendar)
	    {
	    	//mTvLastMonthTitle.setText(mTvCalendarTitle.getText());

	    	calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
	        calendar.add(Calendar.MONTH, +1);
	        /*
	        mTvCalendarTitle.setText(DateFormatter[mThisMonthCalendar.get(Calendar.MONTH)]+" "+mThisMonthCalendar.get(Calendar.YEAR));       
	        calendar.add(Calendar.MONTH, +1);
	        mTvNextMonthTitle.setText(DateFormatter[mThisMonthCalendar.get(Calendar.MONTH)]+" "+mThisMonthCalendar.get(Calendar.YEAR));
	        */
	        return calendar;
	    }
	     
	    @Override
	    public void onItemClick(AdapterView<?> parent, View v, int position, long arg3)
	    {
	        if (selected!=null)
	        	selected.setBackgroundColor(Color.WHITE);
	        v.setBackgroundColor(Color.argb(255, 151, 193, 233));
//	        TextView test = (TextView)view.findViewById(R.id.calendar_event_text);
//			test.setText(mDayList.get(position).getTest());
	        ListView list = (ListView)view.findViewById(R.id.calendar_event_list);
	        list.setAdapter(new EventListAdapter(getActivity(), mDayList.get(position).getEvents()));
	        selected = v;
	    }
	     
	    @Override
	    public void onClick(View v)
	    {
	    	if (thread!=null && thread.isAlive())
	    		thread.interrupt();
	    	handler.sendEmptyMessage(1);
	        switch(v.getId())
	        {
	        case R.id.gv_calendar_activity_b_last:
	            mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
	            if (selected!=null)
	            	selected.setBackgroundColor(Color.WHITE);
	            selected=null;
	            getCalendar(mThisMonthCalendar);
	            break;

	        case R.id.gv_calendar_activity_b_next:
	            mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
	            if (selected!=null)
	            	selected.setBackgroundColor(Color.WHITE);
	            selected=null;
	            getCalendar(mThisMonthCalendar);
	            break;
	        }
	        ListView list = (ListView)view.findViewById(R.id.calendar_event_list);
	        list.setAdapter(new EventListAdapter(getActivity(), new ArrayList<EventInfo>()));
	    }
	 
	    private void initCalendarAdapter()
	    {
	        mCalendarAdapter = new CalendarAdapter(getActivity(), R.layout.day, mDayList);
	        mGvCalendar.setAdapter(mCalendarAdapter);
	    }
	    
	    final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch(msg.what)
				{
				case 0:
					//TextView test = (TextView)view.findViewById(R.id.calendar_event_text);
					//test.setText(Integer.toString(data.size()));
					int size = mDayList.size();
					int numEvent = data.size();
					int p = 0;
					for (int i=0;i<size;i++)
					{
						DayInfo day = mDayList.get(i);
						View v = mGvCalendar.getChildAt(i);
						View point = v.findViewById(R.id.day_cell_tv_has_event);
						
						for (;p<numEvent;p++)
						{
							EventInfo info = data.get(p);
							Calendar cal = Calendar.getInstance();
							cal.setTime(info.getDate());
							
							if (day.getYear()==cal.get(Calendar.YEAR)
									&&day.getMonth()==cal.get(Calendar.MONTH)
									&&Integer.parseInt(day.getDay())==cal.get(Calendar.DAY_OF_MONTH))
							{
								Log.i("cal point","");
								point.setVisibility(View.VISIBLE);
								day.getEvents().add(info);
								day.setTest(info.getTitle());
							}
							else
							{
								//Log.i("cal point","break");
								break;
							}
						}
					}
					break;
				default:
					this.removeMessages(0);
					break;
				}
			};
		};
}
