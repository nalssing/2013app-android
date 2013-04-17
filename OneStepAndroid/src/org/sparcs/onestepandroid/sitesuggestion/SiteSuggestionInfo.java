package org.sparcs.onestepandroid.sitesuggestion;

public class SiteSuggestionInfo {
	private String name;
	private String url;
	
	
	public SiteSuggestionInfo()
	{
		this.name="";
		this.url="";
	}
	
	public SiteSuggestionInfo(String name, String url)
	{
		this.name = name;
		this.url = url;
	}
	
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getUrl()
	{
		return this.url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
}
