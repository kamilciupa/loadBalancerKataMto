package edu.iis.mto.serverloadbalancer;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ServerLoadBalancerTest {
	@Test
	public void itCompiles() {
		assertThat(true, equalTo(true));
	}
	
	@Test
	public void balancingServer_NoVm_serverEmpty() {
		
		Server theServer = a(ServerBuilder.server().withCapacity(1));
		
		balancing(aServersListwith(theServer), aVmsListEmpty());
		
		assertThat(theServer, CurrentLoadPercentageMatcher.hasCurrentLoadOf(0.0d));
		
	}

	

	private void balancing(Server[] servers, Vm[] vms) {
		new ServerLoadBalancer().balance(servers,vms );
	}

	private Vm[] aVmsListEmpty() {
		return new Vm[0];
	}

	private Server[] aServersListwith(Server ... servers) {
		return servers;
	}

	private Server a(ServerBuilder builder) {
		return builder.build();
	}



}
