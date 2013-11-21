package com.bzn.codestory.elevator;

public class Call extends Order {

	private final static int waitWeight = 1;
	private final static int travelWeight = 1;

	private int callTime;

	public Call(int floor, Direction direction, int callTime) {
		super(floor, direction);
		this.callTime = callTime;
	}

	public int getPotentialMaxScore(int currentFloor, int lowerFloor,
			int higherFloor, int currentTime, Direction currentDir) {

		int potentialTravelTime = 0;
		if (currentDir == direction) {
			potentialTravelTime = Math.abs(floor - currentFloor);
		} else {
			if (Direction.UP == currentDir) {
				potentialTravelTime = Math.abs(currentFloor - higherFloor)
						+ Math.abs(higherFloor - floor);
			} else {
				potentialTravelTime = Math.abs(currentFloor - lowerFloor)
						+ Math.abs(lowerFloor - floor);
			}
		}

		return 20
				- waitWeight
				* (getWaitingTime(currentTime) + Math.abs(floor - currentFloor))
				- travelWeight * (potentialTravelTime);
	}

	public int getWaitingTime(int currentTime) {
		return currentTime - callTime;
	}

}
