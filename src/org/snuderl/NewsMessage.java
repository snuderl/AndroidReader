package org.snuderl;

public class NewsMessage {
	public String id;
	public String Title;
	public String Short;
	public String Date;
	public String Category;
	public String link;
	public String Content;
	
	public NewsMessage copy(){
		NewsMessage m = new NewsMessage();
		m.id=this.id;
		m.Title=this.Title;
		m.Date=this.Date;
		m.Short=this.Short;
		return m;		
	}
}
