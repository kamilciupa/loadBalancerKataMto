package edu.iis.mto.serverloadbalancer;


import static edu.iis.mto.serverloadbalancer.CurrentLoadPercentageMatcher.hasCurrentLoadOf;
import static edu.iis.mto.serverloadbalancer.ServerBuilder.server;
import static edu.iis.mto.serverloadbalancer.ServerVmsCountMatcher.hasAVmsCount;
import static edu.iis.mto.serverloadbalancer.VmBuilder.vm;
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

	@Test
	public void balancingServer_OneServer_OneVm_fullLoad() {
		
		Server theServer = a(ServerBuilder.server().withCapacity(1));
		Vm theVm = a(VmBuilder.vm().ofSize(1));
		
		balancing(aServersListwith(theServer), aVmsListWith(theVm));
		
		assertThat(theServer, CurrentLoadPercentageMatcher.hasCurrentLoadOf(100.0d));
		assertThat("Server should contain the vm", theServer.contains(theVm));
		
	}
	

	@Test
	public void balancingServer_ServerWithTenSlotsCapacity_oneSlotVm_FillsTenPercent(){
		
		Server theServer = a(ServerBuilder.server().withCapacity(10));
		Vm theVm = a(VmBuilder.vm().ofSize(1));	
		
		balancing(aServersListwith(theServer), aVmsListWith(theVm));
		
		assertThat(theServer, CurrentLoadPercentageMatcher.hasCurrentLoadOf(10.0d));
		assertThat("Server should contain the vm", theServer.contains(theVm));	
	}
	
	@Test
	public void balancingServer_ServerWithEnoughSpace_manyVms_FillsAll(){
		Server theServer = a(ServerBuilder.server().withCapacity(100));
		Vm theFirstVm = a(VmBuilder.vm().ofSize(1));
		Vm theSecondVm = a(VmBuilder.vm().ofSize(1));	
		
		balancing(aServersListwith(theServer), aVmsListWith(theFirstVm, theSecondVm));
		
		assertThat(theServer, hasAVmsCount(2));	
		assertThat("Server should contain the vm", theServer.contains(theFirstVm));
		assertThat("Server should contain the vm", theServer.contains(theSecondVm));
	}
	
	@Test
	public void balancingServer_VmShouldBeBalancedOnLessLoadedServerFirst(){
		Server moreLoadedServer = a(ServerBuilder.server().withCapacity(100).withCurrentLoadOf(50.0d));
		Server lessLoadedServer = a(ServerBuilder.server().withCapacity(100).withCurrentLoadOf(45.0d));
		Vm theVm = a(VmBuilder.vm().ofSize(10));
		
		balancing(aServersListwith(moreLoadedServer, lessLoadedServer), aVmsListWith(theVm));
		
//		assertThat(theServer, hasAVmsCount(2));	
		assertThat(" less loaded Server should contain the vm", lessLoadedServer.contains(theVm));
		assertThat("more loaded Server should not contain the vm", !moreLoadedServer.contains(theVm));
	}
	
	@Test
	public void balancingServer_ServerWithoutEnoughSpace_NotFilledByVm(){
		
		Server theServer = a(ServerBuilder.server().withCapacity(10).withCurrentLoadOf(90.0d));
		Vm theVm = a(VmBuilder.vm().ofSize(2));
		
		balancing(aServersListwith(theServer), aVmsListWith(theVm));
		assertThat("Server should not contain the vm", !theServer.contains(theVm));
	}
	
	
	@Test
	public void balancingFinal(){
		
		Server server1 = a(server().withCapacity(4));
		Server server2 = a(server().withCapacity(6));
		
		Vm vm1 = a(vm().ofSize(1));
		Vm vm2 = a(vm().ofSize(4));
		Vm vm3 = a(vm().ofSize(2));
		
		balancing(aServersListwith(server1, server2), aVmsListWith(vm1, vm2, vm3));
		assertThat("Server 1 should contain the vm1", server1.contains(vm1));
		assertThat("Server 2 should contain the vm2", server2.contains(vm2));
		assertThat("Server 1 should contain the vm3", server1.contains(vm3));
		
		assertThat(server1, hasCurrentLoadOf(75.0d));
		assertThat(server2, hasCurrentLoadOf(66.66d));
	}
	
	private Vm[] aVmsListWith(Vm ...vms) {
		return vms;
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

	private <T> T  a (Builder<T> builder){
		return builder.build();
	}
	
	

}
