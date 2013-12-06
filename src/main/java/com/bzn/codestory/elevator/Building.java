package com.bzn.codestory.elevator;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class Building {
	private static final int DEFAULT_FLOORS = 20;
	private static final int DEFAULT_CABIN_COUNT = 2;
	private static final int DEFAULT_CABIN_SIZE = 20;
	
	public Elevator[] elevators;
	private Map<Integer, Floor> floors;
	private int cabinCount;
	private Clock clock;
	
	public Building(int lower, int higher, int cabinCount, int cabinSize){
		this.cabinCount = cabinCount;
		floors = Maps.<Integer, Floor> newHashMap();
		clock = new Clock();
		for (int i = lower; i <= higher; i++) {
			floors.put(i, new Floor(i));
		}
		elevators = new Elevator[cabinCount];
		for (int cabin=0; cabin < cabinCount; cabin ++) {
			elevators[cabin] = new Elevator();
			elevators[cabin].reset(lower, higher, cabinSize, clock);
		}
	}
	
	public Building(){
		this(0, DEFAULT_FLOORS - 1, DEFAULT_CABIN_COUNT, DEFAULT_CABIN_SIZE);
	}
	
	public void receiveCall(int floor, Direction direction){
		Elevator picked = new CallRouter().route(elevators, floors.get(floor), direction);
		picked.call(floor, direction);
		User newUser = new User(new Call(floor, direction), clock);
		floors.get(floor).addUser(newUser);
	}
	
	public void receiveGoTo(int cabin, int floor){
		elevators[cabin].goTo(floor);
	}
	
	public void userEntered(int cabin){
		Elevator elevator = elevators[cabin];
		Direction elevatorDirection = elevator.getCurrentDirection();
		boolean userFound = false;
		User userEntering = null;
		List<User> usersAtFloor = floors.get(elevator.currentFloor).getUsers();
		Iterator<User> iterator = usersAtFloor.iterator();
		while(!userFound && iterator.hasNext()){
			User userInspected = iterator.next();
			userFound = elevatorDirection == Direction.NIL || elevatorDirection == userInspected.getCall().getDirection();
			if (userFound) {
				userEntering = userInspected;
				iterator.remove();
			}
		}
		if (userEntering != null) {
			elevator.userEntered(userEntering);
		}
	}
	
	public void userExited(int cabin){
		elevators[cabin].userExited();
	}
	
	public String[] nextCommands() {
		clock.tic();
		String[] commands = new String[cabinCount];
		for (int cabin=0; cabin < cabinCount; cabin ++) {
			commands[cabin] = elevators[cabin].nextCommand().toString();
		}
		return commands;
	}
	
}
