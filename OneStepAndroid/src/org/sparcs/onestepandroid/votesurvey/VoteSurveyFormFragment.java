package org.sparcs.onestepandroid.votesurvey;

import java.util.LinkedList;
import java.util.List;

import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.util.NetworkManager;
import org.sparcs.onestepandroid.util.NetworkReturning;
import org.sparcs.onestepandroid.util.XmlParser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VoteSurveyFormFragment extends Fragment {
	private LinearLayout form;
	private List<VoteSurveyQuestion> data;
	final int offset = 0x2929;
	private ProgressDialog progDig;
	final VoteSurveyFormFragment ref = this;
	private LayoutInflater inflater;
	private String formType;
//	private OnSubmitHandler onSubmit;
//	private OnFormHandler onForm;
//	
//	public VoteSurveyFormFragment() {
//		this.onSubmit = new OnSubmitHandler();
//		this.onForm = new OnFormHandler();
//	}
	
	final Handler onForm = new Handler() {
	//final class OnFormHandler extends Handler {
		public void handleMessage(Message msg) {
			
			if (msg.what==1)
			{
				getActivity().onBackPressed();
			}
			
			LinearLayout loading = (LinearLayout) form.findViewById(R.id.vote_survey_form_loading);
			loading.setVisibility(View.GONE);
			
			final Context mActivity = getActivity();
			final int numQuestion = data.size();
			LinearLayout questionform;
			VoteSurveyQuestion question;
			EditText area;
			CheckBox box;
			RadioGroup radiogroup;
			RadioButton button;
			LayoutParams paramsQuestion = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			paramsQuestion.setMargins(8,15,15,15);
			LayoutParams paramsText = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			paramsText.setMargins(40,10,20,10);
			LayoutParams paramsBox = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			paramsBox.setMargins(20, 0, 0, 0);
			//LayoutParams paramsSubmit = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			//paramsText.setMargins(0,20,0,20);
			
			int questionId = 0;
			
			for (int i=1;i<=numQuestion;i++) {
				question = data.get(i-1);
				questionId = offset+i*100;
				questionform = new LinearLayout(getActivity());
				questionform.setId(questionId);
				questionform.setOrientation(LinearLayout.VERTICAL);
				questionform.setPadding(0, 0, 0, 10);

				questionform.setBackgroundResource(R.drawable.vote_survey_round_corner);
				
				TextView title = new TextView(mActivity);
				title.setPadding(10, 0, 0, 20);
				StringBuilder builder = new StringBuilder();
				builder
					.append(i)
					.append(". ")
					.append(question .getTitle());
				if (!question.getIs_essay() && formType.equals("survey") )
				{
					builder.append('(')
						.append(question.getMin())
						.append('~')
						.append(question.getMax())
						.append(')');
				}
				title.setText(builder.toString());
				questionform.addView(title, paramsQuestion);
				
				if (question.getIs_essay())
				{
					questionform.setTag("text");
					area = new EditText(mActivity);
					area.setBackgroundResource(R.drawable.vote_survey_text_corner);
					area.setPadding(10, 10, 10, 10);
					area.setId(questionId+1);
					area.setTextSize(12.0f);
					questionform.addView(area,paramsText);
				}
				else
				{
					questionform.setTag(question.getChoices().size());
					int boxId=questionId+1;
					if (formType.equals("vote"))
					{
						radiogroup = new RadioGroup(mActivity);
						for (String choice : question.getChoices()) {
							ImageView border = new ImageView(mActivity);
							border.setImageResource(R.drawable.vote_survey_checkbox_corner);
							questionform.addView(border, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							if (formType.equals("survey"))
							{
								box = new CheckBox(mActivity);
								box.setText(choice);
								box.setId(boxId++);
								//box.setBackgroundResource(R.drawable.vote_survey_checkbox_corner);
								questionform.addView(box,paramsBox);
								Log.i("adding box",choice);
							}
							else if (formType.equals("vote"))
							{
								button = new RadioButton(mActivity);
								button.setText(choice);
								button.setId(boxId++);
								//box.setBackgroundResource(R.drawable.vote_survey_checkbox_corner);
								//questionform.addView(button,paramsBox);
								radiogroup.addView(button,paramsBox);
								Log.i("adding box",choice);
							}
						}
						questionform.addView(radiogroup);
					}
					else if (formType.equals("survey"))
					{
						for (String choice : question.getChoices())
						{
							ImageView border = new ImageView(mActivity);
							border.setImageResource(R.drawable.vote_survey_checkbox_corner);
							questionform.addView(border, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							box = new CheckBox(mActivity);
							box.setText(choice);
							box.setId(boxId++);
							//box.setBackgroundResource(R.drawable.vote_survey_checkbox_corner);
							questionform.addView(box,paramsBox);
							Log.i("adding box",choice);
						}
					}
				}
				form.addView(questionform, paramsQuestion);
			}
			
			RelativeLayout submit = (RelativeLayout) inflater.inflate(R.layout.vote_survey_form_submit, null, false);
			
//			Button button = new Button(mActivity);
//			button.setText("submit");
			submit.setOnClickListener( new OnClickListener() {
				
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
								LinearLayout layout = (LinearLayout) form.findViewById(p);
								if ( layout.getTag().equals("text") )
								{
									number.add(i);
									EditText text = (EditText) form.findViewById(p+1);
									if (text.getText().toString().equals(""))
									{
										Message msg = new Message();
										msg.what = 2;
										msg.arg1 = i;
										onSubmit.sendMessage(msg);
										return;
									}
									answersheet.add(text.getText().toString());
									Log.i("added answer",Integer.toString(i)+" : "+ text.getText().toString());
								}
								else // Tag equals check
								{
									int boxnum = (Integer)layout.getTag();
									Log.i("checkbox",Integer.toString(boxnum));
									int numcheck=0;
									if (formType.equals("survey"))
									{
										for (int j=1;j<=boxnum;j++)
										{
											CheckBox box = (CheckBox) form.findViewById(p+j);
											if (box.isChecked())
											{
												numcheck++;
												number.add(i);
												answersheet.add(Integer.toString(j));
												Log.i("added answer",Integer.toString(i)+" : "+ Integer.toString(j));
											}
										}
										if (numcheck<data.get(i-1).getMin())
										{
											Message msg = new Message();
											msg.what = 3;
											msg.arg1 = i;
											msg.arg2 = -1;
											onSubmit.sendMessage(msg);
											return;
										}
										else if (numcheck>data.get(i-1).getMax())
										{
											Message msg = new Message();
											msg.what = 3;
											msg.arg1 = i;
											msg.arg2 = 1;
											onSubmit.sendMessage(msg);
											return;
										}
									}
									else if (formType.equals("vote"))
									{
										for (int j=1;j<=boxnum;j++)
										{
											RadioButton box = (RadioButton) form.findViewById(p+j);
											if (box.isChecked())
											{
												numcheck++;
												number.add(i);
												answersheet.add(Integer.toString(j));
												Log.i("added answer",Integer.toString(i)+" : "+ Integer.toString(j));
											}
										}
										if (numcheck==0)
										{
											Message msg = new Message();
											msg.what = 4;
											msg.arg1 = i;
											onSubmit.sendMessage(msg);
											return;
										}
									}
								}
							}							

							int id = ref.getArguments().getInt("id");
							NetworkReturning returning = NetworkManager.INSTANCE.SubmitSurvey(number, id, answersheet);
							if (returning.getStatus()==200)
							{
								onSubmit.sendEmptyMessage(0);
							}
							else
							{
								onSubmit.sendEmptyMessage(1);
							}
							//onSubmit.sendEmptyMessage(0);
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
			
			form.addView(submit);
		};
	};
	
	//final static Handler onSubmitHandler = new Handler() {
	final Handler onSubmit = new Handler() {
		public void handleMessage(Message msg) {
			
			progDig.dismiss();
			AlertDialog.Builder alertDlg;
			
			switch(msg.what)
			{
			case 0:
				//FragmentManager manager = getFragmentManager();
				getActivity().onBackPressed();
				break;
			case 1:
				alertDlg = new AlertDialog.Builder(getActivity()) ;   
				alertDlg.setTitle("Submit failed") ;
				alertDlg.setMessage("Submit failed. Try again please.") ;   
				alertDlg.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog, int whichButton) {
					 dialog.dismiss();
				 }
				});   
				alertDlg.show() ;
				break;
			case 2:
			case 4:
				alertDlg = new AlertDialog.Builder(getActivity()) ;   
				alertDlg.setTitle("빈 항목이 있습니다.");
				alertDlg.setMessage(Integer.toString(msg.arg1)+"번 항목을 채워주십시오") ;   
				alertDlg.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog, int whichButton) {
					 dialog.dismiss();
				 }
				});   
				alertDlg.show() ;
				break;
			case 3:
				alertDlg = new AlertDialog.Builder(getActivity()) ;   
				alertDlg.setTitle(Integer.toString(msg.arg1)+"번 항목을 확인하여 주십시오");
				if (msg.arg2==-1)
					alertDlg.setMessage("최소 갯수보다 적게 선택하셨습니다.") ;
				else if (msg.arg2==1)
					alertDlg.setMessage("최대 갯수보다 많게 선택하셨습니다.") ; 
				alertDlg.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog, int whichButton) {
					 dialog.dismiss();
				 }
				});   
				alertDlg.show() ;
				break;
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		View view = inflater.inflate(R.layout.vote_survey_form, null, false);
		form = (LinearLayout) view.findViewById(R.id.vote_survey_form);
		TextView title = (TextView) form.findViewById(R.id.vote_survey_form_title);
		title.setText(this.getArguments().getString("title"));
		TextView type = (TextView) form.findViewById(R.id.vote_survey_form_type);
		formType = this.getArguments().getString("type");
		type.setText(formType);
		final int id = this.getArguments().getInt("id");
		
		Thread thread = new Thread( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				NetworkReturning returning = NetworkManager.INSTANCE.getSurvey(id);
				int status = returning.getStatus();
				
				if (status == 500) {
					VoteSurveyQuestion info = new VoteSurveyQuestion();
					info.setTitle("서버와의 연결이 끊겼습니다.");
					info.setIs_essay(true);
					data.add(info);
					onForm.sendEmptyMessage(1);
				}
				else {
					if (status == 200) {
						XmlParser parser = new XmlParser();
						data = parser.parseVoteSurveyForm(returning.getResponse());
						onForm.sendEmptyMessage(0);
					}
					else {
						VoteSurveyQuestion info = new VoteSurveyQuestion();
						info.setTitle("서버와의 연결이 끊겼습니다.");
						info.setIs_essay(true);
						data.add(info);
						onForm.sendEmptyMessage(1);
					}
				}
				
			}
		});
		thread.start();
		
		return view;
	}
}
