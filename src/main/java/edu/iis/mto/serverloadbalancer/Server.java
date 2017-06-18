package edu.iis.mto.serverloadbalancer;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

public class Server {

	public static final double MAXIMUM_CAPACITY = 100.0d;
	public double currentLoadPercentage;
	public int capacity;

	
	private List<Vm> vms = new ArrayList<Vm>();
	
	public boolean contains(Vm theVm) {
		return vms.contains(theVm);
	}

	public Server(int capacity) {
		super();
		this.capacity = capacity;
	}

	public void addVm(Vm vm) {
		currentLoadPercentage = (double)vm.size /(double) capacity * MAXIMUM_CAPACITY;
		this.vms.add(vm);
	}

	public int countVms() {
		return vms.size();
	}

	public boolean canFit(Vm vm) {
		return currentLoadPercentage + ((double)vm.size / (double)this.capacity * MAXIMUM_CAPACITY) <= MAXIMUM_CAPACITY;
	}

	
	
}
