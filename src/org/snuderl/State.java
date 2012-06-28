package org.snuderl;

import org.snuderl.mobilni.NewsMessage;
import org.snuderl.web.PortalApi;

public class State {

	public NewsMessage selected = null;

	private static State state = null;
	private static PortalApi api = null;

	private State() {
	}

	public static State getState() {
		if (state == null) {
			state = new State();
		}

		return state;
	}

	public static PortalApi getPortalApi() {
		if (api == null)
			api = new PortalApi();
		return api;
	}
}
