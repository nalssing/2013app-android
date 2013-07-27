package org.sparcs.onestepandroid.votesurvey;

import java.util.LinkedList;
import java.util.List;

public class VoteSurveyQuestion {
	private int number;
	private String title;
	private Boolean is_essay;
	private List<String> choices;
	private int min = 1;
	private int max = 1;
	
	public VoteSurveyQuestion() {
		super();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getIs_essay() {
		return is_essay;
	}

	public void setIs_essay(Boolean is_essay) {
		this.is_essay = is_essay;
	}

	public List<String> getChoices() {
		return choices;
	}

	public void setChoices(LinkedList<String> linkedList) {
		this.choices = linkedList;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	
	
}
