package org.sparcs.onestepandroid.votesurvey;

import java.util.ArrayList;

import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;
import org.sparcs.onestepandroid.util.XmlParser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class VoteSurveyMainFragment extends Fragment {
	private View view;
	private ListView list;
	private ArrayList<VoteSurveyMainItem> data;
	
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ProgressBar bar = (ProgressBar) view.findViewById(R.id.vote_survey_main_progressbar);
			list.setAdapter(new VoteSurveyMainListAdapter(getActivity(),data));
			bar.setVisibility(View.GONE);
			list.setVisibility(View.VISIBLE);
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.vote_survey_main, null);
		list = (ListView)view.findViewById(R.id.vote_survey_main_list);
		
		list.setClickable(true);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				VoteSurveyMainItem item = (VoteSurveyMainItem) arg0.getItemAtPosition(arg2);
				if( !item.getIs_done())
				{
					Bundle args = new Bundle();
					args.putInt("id", item.getId());
					VoteSurveyFormFragment fragment = new VoteSurveyFormFragment();
					fragment.setArguments(args);
					FragmentManager manager = getFragmentManager();
					for (int i=0; i < manager.getBackStackEntryCount(); i++) {
						manager.popBackStack();
					}
					if (getActivity() != null && getActivity().findViewById(R.id.content_frame) != null) {
						manager
						.beginTransaction()
						.replace(R.id.content_frame, fragment, "survey")
						//.add(R.id.content_frame, fragment, "survey")
						.addToBackStack(null)
						.commit();
					}
				}
				else
				{
					AlertDialog.Builder alertDlg = new AlertDialog.Builder(getActivity()) ;   
					alertDlg.setTitle("Survey aleady done") ;
					alertDlg.setMessage("Survey aleady done") ;   
					alertDlg.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
					 public void onClick(DialogInterface dialog, int whichButton) {
						 dialog.dismiss();
					 }
					});   
					alertDlg.show() ;
				}
			}
			
		});
		
		Thread thread = new Thread( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				NetworkReturning returning = NetworkManager.INSTANCE.getVoteSurveyList();
				int status = returning.getStatus();
				data = new ArrayList<VoteSurveyMainItem>();
				
				if (status == 500) {
					VoteSurveyMainItem info = new VoteSurveyMainItem();
					info.setTitle("서버와의 연결이 끊겼습니다.");
					info.setType("");
					info.setIs_done(false);
					data.add(info);
				}
				else {
					if (status == 200) {
						XmlParser parser = new XmlParser();
						ArrayList<VoteSurveyMainItem> parsed = parser.parseVoteSurveyMainItem(returning.getResponse());
						data = parsed;
						
					}
					else {
						VoteSurveyMainItem info = new VoteSurveyMainItem();
						info.setTitle("서버와의 연결이 끊겼습니다.");
						info.setType("");
						info.setIs_done(false);
						data.add(info);
					}
				}
				handler.sendEmptyMessage(0);
			}
		});
		thread.start();
		
		return view;
	}

}
