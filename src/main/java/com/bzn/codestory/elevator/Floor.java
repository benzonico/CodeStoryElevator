package com.bzn.codestory.elevator;

import java.util.List;

import com.google.common.collect.Lists;

public class Floor {
	private int floorNumber;
	private List<User> users;
	private boolean floorTakenUp;
	private int cabinTakingFloorUp;
	private boolean floorTakenDown;
	private int cabinTakingFloorDown;
	
	public Floor(int floorNumber){
		this.floorNumber = floorNumber;
		floorTakenUp = false;
		floorTakenDown = false;
		users = Lists.<User> newArrayList();
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
	public List<User> getUsers() {
		return users;
	}

	public void addUser(User newUser) {
		users.add(newUser);
	}
}
