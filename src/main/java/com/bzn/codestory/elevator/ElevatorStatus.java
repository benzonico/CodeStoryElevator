package com.bzn.codestory.elevator;

import java.util.SortedMap;

class ElevatorStatus {

	public int currentFloor;
	public int usersInCabin;
	public int cabinSize;
	public boolean open;
	public Direction currentDirection;
	public SortedMap<Integer, Integer> frequencies;

	ElevatorStatus(int currentFloor, int usersInCabin, int cabinSize, boolean open, Direction currentDirection, SortedMap<Integer, Integer> frequencies) {
		this.currentFloor = currentFloor;
		this.usersInCabin = usersInCabin;
		this.cabinSize = cabinSize;
		this.open = open;
		this.currentDirection = currentDirection;
		this.frequencies = frequencies;
	}

}
