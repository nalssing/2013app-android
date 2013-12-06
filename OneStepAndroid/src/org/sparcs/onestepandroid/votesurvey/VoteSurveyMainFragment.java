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

public class VoteSurveyMainFragment extends Fragment {
	private View view;
	private ListView list;
	private ArrayList<VoteSurveyMainItem> data;
	
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ProgressBar bar = (ProgressBar) view.findViewById(R.id.vote_survey_main_progressbar);
			Log.i("data size:", Integer.toString(data.size()));
			VoteSurveyMainListAdapter adapter = new VoteSurveyMainListAdapter(getActivity(),data);
			list.setAdapter(adapter);
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
		Log.i("survey main",Integer.toString(getFragmentManager().getBackStackEntryCount()));
		
		list.setClickable(true);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				VoteSurveyMainItem item = (VoteSurveyMainItem) arg0.getItemAtPosition(arg2);
				if( !item.getIs_done())
				{
					Bundle args = new Bundle();
					args.putInt("id", item.getId());
					args.putString("title", item.getTitle());
					args.putString("type", item.getType());
					VoteSurveyFormFragment fragment = new VoteSurveyFormFragment();
					fragment.setArguments(args);
					FragmentManager manager = getFragmentManager();
					manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
					if (getActivity() != null && getActivity().findViewById(R.id.content_frame) != null) {
						manager
						.beginTransaction()
						.replace(R.id.content_frame, fragment, "survey")
						//.add(R.id.content_frame, fragment, "survey")
						.addToBackStack("survey")
						.commit();
					}
				}
				else
				{
					AlertDialog.Builder alertDlg = new AlertDialog.Builder(getActivity()) ;   
					alertDlg.setTitle(item.getType()+" aleady done") ;
					alertDlg.setMessage(item.getType()+" aleady done") ;   
					alertDlg.setPositiveButton("´Ý±â", new DialogInterface.OnClickListener() {
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
					info.setTitle("¼­¹ö¿ÍÀÇ ¿¬°áÀÌ ²÷°å½À´Ï´Ù.");
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
						info.setTitle("¼­¹ö¿ÍÀÇ ¿¬°áÀÌ ²÷°å½À´Ï´Ù.");
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
