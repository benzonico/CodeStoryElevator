package com.bzn.codestory.elevator;

public class Floor {
	private int floorNumber;
	private boolean floorTakenUp;
	private int cabinTakingFloorUp;
	private boolean floorTakenDown;
	private int cabinTakingFloorDown;
	
	public Floor(int floorNumber){
		this.floorNumber = floorNumber;
		floorTakenUp = false;
		floorTakenDown = false;
	}
	
	public int getFloorNumber() {
		return floorNumber;
	}
	public boolean isFloorTakenUp() {
		return floorTakenUp;
	}
	public int getCabinTakingFloorUp() {
		return cabinTakingFloorUp;
	}
	public boolean isFloorTakenDown() {
		return floorTakenDown;
	}
	public int getCabinTakingFloorDown() {
		return cabinTakingFloorDown;
	}
}
