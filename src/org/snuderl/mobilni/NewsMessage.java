package org.snuderl.mobilni;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.snuderl.ApplicationState;

public class NewsMessage {
	public String Id;
	public String Title;
	public String Short;
	public String Date;
	public String Category;
	public String Link;
	public String Content;
	public String ParentCategory = null;
	public java.util.Date PubDate;

	public NewsMessage copy() {
		NewsMessage m = new NewsMessage();
		m.Id = this.Id;
		m.Title = this.Title;
		m.Date = this.Date;
		m.Short = this.Short;
		m.Link = this.Link;
		m.Category = this.Category;
		return m;
	}
	
	

	@Override
	public String toString() {
		return ApplicationState.GetApplicationState().GetParent(this.Category);
	}

}
