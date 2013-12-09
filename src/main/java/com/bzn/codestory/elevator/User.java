package com.bzn.codestory.elevator;

public class User {

	private Call call;
	private GoTo goTo;
	private Integer timeArrivedInBuilding;
	private Integer timeEnteredCabin;

	public User(Call call, Clock clock) {
		this.call = call;
		this.timeArrivedInBuilding = clock.getTime();
	}

	public void enterCabin(Clock clockWhenEntering) {
		this.timeEnteredCabin = clockWhenEntering.getTime();
	}

	public void goTo(GoTo goTo) {
		this.goTo = goTo;
	}

	public Call getCall() {
		return call;
	}
	
	public GoTo getGoTo() {
		return goTo;
	}

}
