package org.snuderl.mobilni;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.snuderl.web.RestClient;
import org.snuderl.web.RestClient.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Category {
	public String Name;
	public int Id;
	public Category[] Children;
	
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public Category[] getChildren() {
		return Children;
	}

	public void setChildren(Category[] children) {
		Children = children;
	}
	
	public List<String> Children(){
		List<String> cd = new ArrayList<String>();
		for(Category c : Children){
			cd.add(c.Name);
		}
		return cd;
	}
	

	public static Category[] GetCategories() throws Exception{
		Category[] categories;
		RestClient rc = new RestClient("http://mobilniportalnovic.apphb.com/Categories/Index");
		rc.AddParam("format", "json");
		rc.Execute(RequestMethod.GET);
		String response = rc.getResponse();
		
		GsonBuilder builder = new GsonBuilder();
		Gson g = builder.create();
		categories  = g.fromJson(response, Category[].class);
		
		return categories;
	}
}
