package org.sparcs.onestepandroid.noticeBoard;


import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.policysuggestion.PolicyReadFragment;
import org.sparcs.onestepandroid.policysuggestion.PolicySuggestionListFragment;
import org.sparcs.onestepandroid.policysuggestion.PolicyWriteFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PolicyTabMenuFragment extends Fragment {
	private int selectedPosition;
	private Bundle bundle;
	private PolicySuggestionListFragment tab1;
	private PolicySuggestionListFragment tab2;
	private PolicySuggestionListFragment tab3;
	private PolicySuggestionListFragment tab4;
	private Context context;

	public PolicyTabMenuFragment() {
		super();
	}
	public static PolicyTabMenuFragment newInstance(Context context) {
		PolicyTabMenuFragment instance = new PolicyTabMenuFragment();
		instance.initialize(context);
		return instance;
	}
	public void initialize(Context context) {
		this.context = context;
		this.bundle = getArguments();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i("","recreate view of noticetab");
		
		View v = inflater.inflate(R.layout.policy_suggestion_tab_fragment, null);

		Button view = (Button) v.findViewById(R.id.policy_top_bar_tab1);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PolicyReadFragment rp = (PolicyReadFragment) getFragmentManager().findFragmentByTag("readpolicy");
				if ( rp!=null)
				{
					getFragmentManager().beginTransaction().remove(rp).commit();
					getFragmentManager().popBackStack();
				}
				PolicyWriteFragment wp = (PolicyWriteFragment) getFragmentManager().findFragmentByTag("writepolicy");
				if ( wp!=null)
				{
					getFragmentManager().beginTransaction().remove(wp).commit();
					getFragmentManager().popBackStack();
				}
				goToTab(1, null, 0);
			}
		});

		view = (Button) v.findViewById(R.id.policy_top_bar_tab2);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PolicyReadFragment rp = (PolicyReadFragment) getFragmentManager().findFragmentByTag("readpolicy");
				if ( rp!=null)
				{
					getFragmentManager().beginTransaction().remove(rp).commit();
					getFragmentManager().popBackStack();
				}
				PolicyWriteFragment wp = (PolicyWriteFragment) getFragmentManager().findFragmentByTag("writepolicy");
				if ( wp!=null)
				{
					getFragmentManager().beginTransaction().remove(wp).commit();
					getFragmentManager().popBackStack();
				}
				goToTab(2, null, 0);
			}
		});

		view = (Button) v.findViewById(R.id.policy_top_bar_tab3);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PolicyReadFragment rp = (PolicyReadFragment) getFragmentManager().findFragmentByTag("readpolicy");
				if ( rp!=null)
				{
					getFragmentManager().beginTransaction().remove(rp).commit();
					getFragmentManager().popBackStack();
				}
				PolicyWriteFragment wp = (PolicyWriteFragment) getFragmentManager().findFragmentByTag("writepolicy");
				if ( wp!=null)
				{
					getFragmentManager().beginTransaction().remove(wp).commit();
					getFragmentManager().popBackStack();
				}
				goToTab(3, null, 0);
			}
		});
		
		view = (Button) v.findViewById(R.id.policy_top_bar_tab4);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PolicyReadFragment rp = (PolicyReadFragment) getFragmentManager().findFragmentByTag("readpolicy");
				if ( rp!=null)
				{
					getFragmentManager().beginTransaction().remove(rp).commit();
					getFragmentManager().popBackStack();
				}
				PolicyWriteFragment wp = (PolicyWriteFragment) getFragmentManager().findFragmentByTag("writepolicy");
				if ( wp!=null)
				{
					getFragmentManager().beginTransaction().remove(wp).commit();
					getFragmentManager().popBackStack();
				}
				goToTab(4, null, 0);
			}
		});
		return v;
	}
	
	public void goToTab(int position, String boardname, int articleID) {
		View selectedTab;
		View view;
		switch(selectedPosition) {
		case 1:
			selectedTab = getView().findViewById(R.id.policy_top_bar_tab1);
			break;
		case 2:
			selectedTab = getView().findViewById(R.id.policy_top_bar_tab2);
			break;
		case 3:
			selectedTab = getView().findViewById(R.id.policy_top_bar_tab3);
			break;
		default:
			selectedTab = getView().findViewById(R.id.policy_top_bar_tab4);
		}
		if (position == 1) {
			selectedPosition = position;
			selectedTab.setSelected(false);
			tab1 = PolicySuggestionListFragment.newInstance(context, articleID);
			bundle = new Bundle();
			bundle.putString("tab", getResources().getString(R.string.policy_tab1));
			bundle.putString("board", "Best");
			bundle.putInt("articleID", articleID);
			tab1.setArguments(bundle);
			tab1.initialize(context);
			view = getView().findViewById(R.id.policy_top_bar_tab1);
			view.setSelected(true);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.policy_fragment_content, tab1);
			ft.commit();
		}
		else if (position == 2) {
			selectedPosition = position;
			selectedTab.setSelected(false);
			view = getView().findViewById(R.id.policy_top_bar_tab2);
			view.setSelected(true);
			tab2 = PolicySuggestionListFragment.newInstance(context, articleID);
			bundle = new Bundle();
			bundle.putString("tab", getResources().getString(R.string.policy_tab2));
			bundle.putString("board", "Exam");
			bundle.putInt("articleID", articleID);
			tab2.setArguments(bundle);
			tab2.initialize(context);

			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.policy_fragment_content, tab2);
			ft.commit();
		}
		else if (position == 3) {
			selectedPosition = position;
			selectedTab.setSelected(false);
			view = getView().findViewById(R.id.policy_top_bar_tab3);
			view.setSelected(true);
			tab3 = PolicySuggestionListFragment.newInstance(context, articleID);
			bundle = new Bundle();
			bundle.putString("tab", getResources().getString(R.string.policy_tab3));
			bundle.putString("board", "Proc");
			bundle.putInt("articleID", articleID);
			tab3.setArguments(bundle);
			tab3.initialize(context);

			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.policy_fragment_content, tab3);
			ft.commit();
		}
		else if (position == 4) {
			selectedPosition = position;
			selectedTab.setSelected(false);
			view = getView().findViewById(R.id.policy_top_bar_tab4);
			view.setSelected(true);
			tab4 = PolicySuggestionListFragment.newInstance(context, articleID);
			bundle = new Bundle();
			bundle.putString("tab", getResources().getString(R.string.policy_tab4));
			bundle.putString("board", "Normal");
			bundle.putInt("articleID", articleID);
			tab4.setArguments(bundle);
			tab4.initialize(context);

			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.policy_fragment_content, tab4);
			ft.commit();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		goToTab(bundle.getInt("position"), bundle.getString("tab"), bundle.getInt("articleID"));
	}
}
