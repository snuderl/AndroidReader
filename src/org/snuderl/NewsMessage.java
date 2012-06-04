package org.snuderl;

public class NewsMessage {
	public String Id;
	public String Title;
	public String Short;
	public String Date;
	public String Category;
	public String Link;
	public String Content;
	
	public NewsMessage copy(){
		NewsMessage m = new NewsMessage();
		m.Id=this.Id;
		m.Title=this.Title;
		m.Date=this.Date;
		m.Short=this.Short;
		m.Link=this.Link;
		return m;		
	}
	
	
}
