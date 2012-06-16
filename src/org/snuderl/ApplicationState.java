package org.snuderl;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ApplicationState {
	public Category[] Categories;
	private Map<String, String> ChildToParent = null;

	public CharSequence[] Categories() {
		CharSequence[] sequence = new CharSequence[Categories.length];
		for (int i = 0; i < Categories.length; i++) {
			sequence[i] = Categories[i].Name;
		}
		return sequence;
	}

	private static ApplicationState singleton = null;

	private ApplicationState() {
	}

	public static ApplicationState GetApplicationState() {
		if (singleton == null)
			singleton = new ApplicationState();
		return singleton;
	}

	public String GetParent(String child) {
		if (ChildToParent == null) {
			ChildToParent = new HashMap<String, String>();
			for (Category p :Categories ) {
				for(Category c : p.Children){
					ChildToParent.put(c.Name, p.Name);
				}
			}
		}

		return ChildToParent.get(child);
	}
}
