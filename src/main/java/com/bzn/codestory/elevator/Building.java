package com.bzn.codestory.elevator;

import java.util.Map;

import com.google.common.collect.Maps;

public class Building {
	private static final int DEFAULT_FLOORS = 20;
	private static final int DEFAULT_CABIN_COUNT = 2;
	private static final int DEFAULT_CABIN_SIZE = 20;
	
	public Elevator[] elevators;
	private Map<Integer, Floor> floors;
	private int lower;
	private int higher;
	private int cabinCount;
	
	public Building(int lower, int higher, int cabinCount, int cabinSize){
		this.lower = lower;
		this.higher = higher;
		this.cabinCount = cabinCount;
		floors = Maps.<Integer, Floor> newHashMap();
		for (int i = lower; i <= higher; i++) {
			floors.put(i, new Floor(i));
		}
		elevators = new Elevator[cabinCount];
		for (int cabin=0; cabin < cabinCount; cabin ++) {
			elevators[cabin] = new Elevator();
			elevators[cabin].reset(lower, higher, cabinSize);
		}
	}
	
	public Building(){
		new Building(0, DEFAULT_FLOORS - 1, DEFAULT_CABIN_COUNT, DEFAULT_CABIN_SIZE);
	}
	
	public void receiveCall(int floor, Direction direction){
		elevators[0].call(floor, direction);
	}
	
	public void receiveGoTo(int cabin, int floor){
		elevators[cabin].goTo(floor);
	}
	
	public void userEntered(int cabin){
		elevators[cabin].userEntered();
	}
	
	public void userExited(int cabin){
		elevators[cabin].userExited();
	}
	
	public String[] nextCommands() {
		String[] commands = new String[cabinCount];
		for (int cabin=0; cabin < cabinCount; cabin ++) {
			commands[cabin] = elevators[cabin].nextCommand().toString();
		}
		return commands;
	}
	
}
