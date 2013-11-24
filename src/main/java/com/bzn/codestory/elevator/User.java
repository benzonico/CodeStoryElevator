package com.bzn.codestory.elevator;

public class User {

	private Call call;
	private GoTo goTo;
	private int timeArrivedInBuilding;
	private int timeEnteredCabin;

	private final static int waitWeight = 1;
	private final static int travelWeight = 1;

	public User(Call call, int timeArrivedInBuilding) {
		this.call = call;
		this.timeArrivedInBuilding = timeArrivedInBuilding;
	}

	public void enterCabin(int timeEnteredCabin) {
		this.timeEnteredCabin = timeEnteredCabin;
	}

	public void goTo(GoTo goTo) {
		this.goTo = goTo;
	}

	public Call getCall() {
		return call;
	}

	public int getPotentialMaxScore(int currentFloor,
			Direction currentDirection, int lower, int higher, int currentTime) {

		int potentialTravelTime = 0;
		if (currentDirection == call.direction) {
			potentialTravelTime = Math.abs(call.floor - currentFloor);
		} else {
			if (Direction.UP == currentDirection) {
				potentialTravelTime = Math.abs(currentFloor - higher)
						+ Math.abs(higher - call.floor);
			} else {
				potentialTravelTime = Math.abs(currentFloor - lower)
						+ Math.abs(lower - call.floor);
			}
		}

		int potentialScore = 20
				- waitWeight
				* (getWaitingTime(currentTime) + Math.abs(call.floor
						- currentFloor)) - travelWeight * (potentialTravelTime);
		return Math.max(0, potentialScore);
	}

	public int getWaitingTime(int currentTime) {
		return currentTime - timeArrivedInBuilding;
	}

}
