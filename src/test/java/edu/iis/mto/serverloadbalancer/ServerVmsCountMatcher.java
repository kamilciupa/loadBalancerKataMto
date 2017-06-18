package edu.iis.mto.serverloadbalancer;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ServerVmsCountMatcher extends TypeSafeMatcher<Server>{

	private int expectedVmsCount;

	public ServerVmsCountMatcher(int expectedVmsCount) {
		this.expectedVmsCount = expectedVmsCount;
	}

	public void describeTo(Description description) {
		description.appendText(" a server with vms count " ).appendValue(expectedVmsCount);
	}

	@Override
	protected void describeMismatchSafely(Server item, Description description) {
		description.appendText(" a server with vms count " ).appendValue(item.countVms());
	}
	
	@Override
	protected boolean matchesSafely(Server server) {
		return expectedVmsCount == 	server.countVms();
	}

	
	public static ServerVmsCountMatcher hasAVmsCount(int expectedVmsCount) {
		return new ServerVmsCountMatcher(expectedVmsCount);
	}
}
