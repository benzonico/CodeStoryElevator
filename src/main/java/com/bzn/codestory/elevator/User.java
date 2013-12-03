package com.bzn.codestory.elevator;

public class User implements Comparable<User> {

	private Call call;
	private GoTo goTo;
	private Integer timeArrivedInBuilding;
	private Integer timeEnteredCabin;

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
	
	public GoTo getGoTo() {
		return goTo;
	}

	public int getPotentialMaxScore(int currentFloor,
			Direction currentDirection, int lower, int higher, int currentTime) {

		int potentialTravelTime = 0;
		if (Direction.UP == call.direction) {
			potentialTravelTime = Math.abs(call.floor - higher);
		} else {
			potentialTravelTime = Math.abs(lower - call.floor);
		}

		int potentialScore = 2
				* (higher - lower + 1)
				- waitWeight
				* (getWaitingTime(currentTime) + Math.abs(call.floor
						- currentFloor)) - travelWeight * (potentialTravelTime);
		return Math.max(0, potentialScore);
	}

	public int getWaitingTime(int currentTime) {
		return currentTime - timeArrivedInBuilding;
	}

	@Override
	public int compareTo(User other) {
		int comparison = timeArrivedInBuilding.compareTo(other.timeArrivedInBuilding);
		if (comparison == 0) {
			comparison = call.compareTo(other.call);
			if (comparison == 0 && timeEnteredCabin != null) {
				comparison = timeEnteredCabin.compareTo(other.timeEnteredCabin);
				if (comparison == 0 && goTo != null) {
					comparison = goTo.compareTo(other.goTo);
				}
			}
		}
		return comparison;
	}

}
