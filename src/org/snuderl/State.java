package org.snuderl;

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
