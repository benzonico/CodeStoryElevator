package com.bzn.codestory.elevator;

import static com.bzn.codestory.elevator.Command.CLOSE;
import static com.bzn.codestory.elevator.Command.DOWN;
import static com.bzn.codestory.elevator.Command.NOTHING;
import static com.bzn.codestory.elevator.Command.UP;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;

public class Elevator {

	private static final int DEFAULT_FLOORS = 20;

	private static final int DEFAULT_CABIN_SIZE = 30;

	private int currentFloor;
	private int cabinSize;
	private Set<Call> calls;
	private boolean open;
	private int users;
	private Direction currentDirection;
	private SortedMap<Integer, Integer> frequencies;


	public Elevator() {
		this(0);
	}

	@VisibleForTesting
	Elevator(int startFloor) {
		reset(0, DEFAULT_FLOORS - 1, DEFAULT_CABIN_SIZE);
		currentFloor = startFloor;
	}

	public void reset(int lower, int higher, int cabinSize) {
		resetFrequencies(lower, higher);
		currentFloor = 0;
		this.cabinSize = cabinSize;
		reset();
	}

	private void resetFrequencies(int lower, int higher) {
		frequencies = Maps.newTreeMap();
		for(int floor = lower; floor < higher + 1; floor++){
			frequencies.put(floor, 0);
		}
	}

	private void reset() {
		currentFloor = 0;
		calls = new HashSet<>();
		open = false;
		users = 0;
		currentDirection = Direction.NIL;
	}

	public Command nextCommand() {
		if (open) {
			return close();
		}
		if (shouldOpen()) {
			return open();
		} else if (hasCall()) {
			if (shouldGoUp()) {
				return up();
			} else {
				return down();
			}
		}
		return idle();
	}

	private boolean hasCall() {
		return !calls.isEmpty();
	}

	private boolean shouldGoUp() {
		if (currentDirection.isNil()) {
			return countCallsBelow() <= countCallsAbove();
		}
		return currentDirection.isUp();
	}

	private boolean shouldOpen() {
		if (shouldChangeDirection()) {
			currentDirection = Direction.NIL;
		}
		return hasCallToCurrentFloor(currentDirection)
				|| (currentDirection.isNil() && (hasCallToCurrentFloor(Direction.UP) || hasCallToCurrentFloor(Direction.DOWN)));
	}

	private boolean shouldChangeDirection() {
		boolean shouldChange = true;
		if (currentDirection.isUp()) {
			shouldChange = countCallsAbove() == 0;
		}
		if (currentDirection.isDown()) {
			shouldChange = countCallsBelow() == 0;
		}
		return shouldChange;
	}

	private int countCallsAbove() {
		int countCallsUp = 0;
		for (Call call : calls) {
			if (call.isHigherThan(currentFloor)) {
				countCallsUp++;
			}
		}
		return countCallsUp;
	}

	private int countCallsBelow() {
		int countCallsDown = 0;
		for (Call call : calls) {
			if (call.isLowerThan(currentFloor)) {
				countCallsDown++;
			}
		}
		return countCallsDown;
	}

	private Command down() {
		currentFloor--;
		currentDirection = Direction.DOWN;
		return DOWN;
	}

	private Command up() {
		currentFloor++;
		currentDirection = Direction.UP;
		return UP;
	}

	private Command close() {
		open = false;
		return CLOSE;
	}

	private Command open() {
		removeAllCallOfCurrentFloor();
		open = true;
		return Command.OPEN;
	}

	private Command idle() {
		Command idleCommand = doNothing();
		if(currentFloor > getMiddleFloor()){
			idleCommand = down();
		}else if(currentFloor < getMiddleFloor()){
			idleCommand = up();
		}
		return idleCommand;
	}

	private int getMiddleFloor() {
		return frequencies.size() / 2;
	}
	
	private Command doNothing() {
		currentDirection = Direction.NIL;
		return NOTHING;
	}

	private void removeAllCallOfCurrentFloor() {
		calls.remove(new Call(currentFloor, Direction.UP));
		calls.remove(new Call(currentFloor, Direction.DOWN));
	}

	public void call(int floor, Direction direction) {
		calls.add(new Call(floor, direction));
		incrementFrequency(floor);
	}

	public void goTo(int floor) {
		Direction dir = Direction.UP;
		if (floor < currentFloor) {
			dir = Direction.DOWN;
		}
		call(floor, dir);
	}

	public void userEntered() {
		users++;
	}

	public int usersInCabin() {
		return users;
	}

	public void userExited() {
		users--;
	}

	public Integer[] getFrequencies() {
		if (frequencies == null) {
			resetFrequencies(0, DEFAULT_FLOORS);
		}
		return frequencies.values().toArray(new Integer[0]);
	}

	private boolean hasCallToCurrentFloor(Direction dir) {
		return calls.contains(new Call(currentFloor, dir));
	}

	private void incrementFrequency(int floor) {
		frequencies.put(floor, frequencies.get(floor) + 1);
	}

}
