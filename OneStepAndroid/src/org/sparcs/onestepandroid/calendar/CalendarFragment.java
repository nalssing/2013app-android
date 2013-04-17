package org.sparcs.onestepandroid.calendar;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import org.sparcs.onestepandroid.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
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
	     
	    private ArrayList<DayInfo> mDayList;
	    private CalendarAdapter mCalendarAdapter;
	    
	    private ArrayList<WeekdayInfo> mWeekdayList;
	    private WeekdayAdapter mWeekdayAdapter;
	    	     
	    Calendar mLastMonthCalendar;
	    Calendar mThisMonthCalendar;
	    Calendar mNextMonthCalendar;
	     
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        View view = inflater.inflate(R.layout.calendar, container, false);
	         
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
	        
	        return view;
	    }
	 
	    @Override
	    public void onResume()
	    {
	        super.onResume();
	         
	        // 이번달 의 캘린더 인스턴스를 생성한다.
	        mThisMonthCalendar = Calendar.getInstance();
	        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
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
	         	         
	        for(int i=0; i<dayOfMonth-1; i++)
	        {
	            int date = lastMonthStartDay+i;
	            day = new DayInfo();
	            day.setDay(Integer.toString(date));
	            day.setInMonth(false);
	             
	            mDayList.add(day);
	        }
	        for(int i=1; i <= thisMonthLastDay; i++)
	        {
	            day = new DayInfo();
	            day.setDay(Integer.toString(i));
	            day.setInMonth(true);
	             
	            mDayList.add(day);
	        }
	        for(int i=1; i<42-(thisMonthLastDay+dayOfMonth-1)+1; i++)
	        {
	            day = new DayInfo();
	            day.setDay(Integer.toString(i));
	            day.setInMonth(false);
	            mDayList.add(day);
	        }
	        
	        calendar.add(Calendar.MONTH, -1);
	        mTvLastMonthTitle.setText(DateFormatter[mThisMonthCalendar.get(Calendar.MONTH)]+" "+mThisMonthCalendar.get(Calendar.YEAR));
	        calendar.add(Calendar.MONTH, +2);
	        mTvNextMonthTitle.setText(DateFormatter[mThisMonthCalendar.get(Calendar.MONTH)]+" "+mThisMonthCalendar.get(Calendar.YEAR));
	        calendar.add(Calendar.MONTH, -1);
	         
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
	         
	    }
	     
	    @Override
	    public void onClick(View v)
	    {
	        switch(v.getId())
	        {
	        case R.id.gv_calendar_activity_b_last:
	            mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
	            getCalendar(mThisMonthCalendar);
	            break;

	        case R.id.gv_calendar_activity_b_next:
	            mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
	            getCalendar(mThisMonthCalendar);
	            break;
	        }
	    }
	 
	    private void initCalendarAdapter()
	    {
	        mCalendarAdapter = new CalendarAdapter(getActivity(), R.layout.day, mDayList);
	        mGvCalendar.setAdapter(mCalendarAdapter);
	    }
}
