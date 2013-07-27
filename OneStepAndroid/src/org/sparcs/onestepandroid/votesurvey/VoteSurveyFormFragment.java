package org.sparcs.onestepandroid.votesurvey;

import java.util.LinkedList;
import java.util.List;

import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;
import org.sparcs.onestepandroid.util.XmlParser;

import android.R.color;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

public class VoteSurveyFormFragment extends Fragment {
	private View view;
	private List<VoteSurveyQuestion> data;
	final int offset = 0x2929;
	private ProgressDialog progDig;
	final VoteSurveyFormFragment ref = this;
	
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ProgressBar bar = (ProgressBar) view.findViewById(R.id.vote_survety_form_loading);
			final Context mActivity = getActivity();
			bar.setVisibility(View.GONE);
			
			final int numQuestion = data.size();
			LinearLayout form = (LinearLayout) view.findViewById(R.id.vote_survey_form);
			LinearLayout questionform;
			VoteSurveyQuestion question;
			EditText area;
			CheckBox box;
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			int questionId = 0;
			
			for (int i=1;i<=numQuestion;i++) {
				questionId = offset+i*100;
				questionform = new LinearLayout(getActivity());
				questionform.setId(questionId);
				questionform.setOrientation(LinearLayout.VERTICAL);
				questionform.setBackgroundResource(R.drawable.section_line_item);
				question = data.get(i-1);
				TextView title = new TextView(mActivity);
				StringBuilder builder = new StringBuilder();
				title.setText(builder
						.append(i)
						.append('.')
						.append(question.getTitle())
						.toString());
				questionform.addView(title, params);		
				//box = new CheckBox(mActivity);
				//box.setText("test");
				//questionform.addView(box);
				if (question.getIs_essay())
				{
					questionform.setTag("text");
					area = new EditText(mActivity);
					area.setBackgroundColor(color.white);
					area.setHint("hint");
					area.setId(questionId+1);
					questionform.addView(area,params);
				}
				else
				{
					questionform.setTag(question.getChoices().size());
					int boxId=questionId+1;
					for (String choice : question.getChoices()) {
						box = new CheckBox(mActivity);
						box.setText(choice);
						box.setId(boxId++);
						questionform.addView(box,params);
						Log.i("adding box",choice);
					}
				}
				form.addView(questionform, params);
			}
			
			Button button = new Button(mActivity);
			button.setText("submit");
			button.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					final Thread thread = new Thread( new Runnable () {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							LinkedList<Integer> number = new LinkedList<Integer>();
							LinkedList<String> answersheet = new LinkedList<String>();
										
							for (int i=1;i<=numQuestion;i++)
							{
								int p = offset+i*100;
								LinearLayout layout = (LinearLayout) view.findViewById(p);
								if ( layout.getTag().equals("text") )
								{
									number.add(i);
									EditText text = (EditText) view.findViewById(p+1);
									answersheet.add(text.getText().toString());
									Log.i("added answer",Integer.toString(i)+" : "+ text.getText().toString());
								}
								else // Tag equals check
								{
									int boxnum = (Integer)layout.getTag();
									Log.i("checkbox",Integer.toString(boxnum));
									for (int j=1;j<=boxnum;j++)
									{
										CheckBox box = (CheckBox) view.findViewById(p+j);
										if (box.isChecked())
										{
											number.add(i);
											answersheet.add(Integer.toString(j));
											Log.i("added answer",Integer.toString(i)+" : "+ Integer.toString(j));
										}
									}
								}
							}							

							int id = ref.getArguments().getInt("id");
							NetworkReturning returning = NetworkManager.INSTANCE.SubmitSurvey(number, id, answersheet);
							if (returning.getStatus()==200)
							{
								onSubmitHandler.sendEmptyMessage(0);
							}
							else
							{
								onSubmitHandler.sendEmptyMessage(1);
							}
						}
					});
					
					thread.start();
					
					progDig = new ProgressDialog(getActivity());
					progDig.setCancelable(true);
					progDig.setMessage("Submitting...");
					progDig.show();
					progDig.setOnCancelListener(new OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface dialog) {
							thread.interrupt();
						}
					});
					

				}
			});
			
			form.addView(button,params);
			
		};
	};
	
	final Handler onSubmitHandler = new Handler() {
		public void handleMessage(Message msg) {
			
			progDig.dismiss();
			
			if (msg.what==0)
			{
				FragmentManager manager = getFragmentManager();
				manager
				.beginTransaction()
				.replace(R.id.content_frame, new VoteSurveyMainFragment())
				.commit();
				manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			}
			else
			{
				AlertDialog.Builder alertDlg = new AlertDialog.Builder(getActivity()) ;   
				alertDlg.setTitle("Submit failed") ;
				alertDlg.setMessage("Submit failed. Try again please.") ;   
				alertDlg.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog, int whichButton) {
					 dialog.dismiss();
				 }
				});   
				alertDlg.show() ;
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.vote_survey_form, null, false);
		final int id = this.getArguments().getInt("id");
		Thread thread = new Thread( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				NetworkReturning returning = NetworkManager.INSTANCE.getSurvey(id);
				XmlParser parser = new XmlParser();
				data = parser.parseVoteSurveyForm(returning.getResponse());
				
				//data = new ArrayList<VoteSurveyMainItem>();
				
//				if (status == 500) {
//					VoteSurveyForm info = new VoteSurveyForm();
//					info.setTitle("서버와의 연결이 끊겼습니다.");
//					info.setType("");
//					info.setIs_done(false);
//					data.add(info);
//				}
//				else {
//					if (status == 200) {
//						XmlParser parser = new XmlParser();
//						ArrayList<VoteSurveyMainItem> parsed = parser.parseSurveyForm(returning.getResponse());
//						data = parsed;
//						
//					}
//					else {
//						VoteSurveyForm info = new VoteSurveyForm();
//						info.setTitle("서버와의 연결이 끊겼습니다.");
//						info.setType("");
//						info.setIs_done(false);
//						data.add(info);
//					}
//				}
				handler.sendEmptyMessage(0);
			}
		});
		thread.start();
		
		return view;
	}
}
