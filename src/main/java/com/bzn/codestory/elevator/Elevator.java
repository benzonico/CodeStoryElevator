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
	private Set<Order> orders;
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
		orders = new HashSet<>();
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
		} else if (hasOrder()) {
			if (shouldGoUp()) {
				return up();
			} else {
				return down();
			}
		}
		return idle();
	}

	private boolean hasOrder() {
		return !orders.isEmpty();
	}

	private boolean shouldGoUp() {
		if (currentDirection.isNil()) {
			return countOrdersBelow() <= countOrdersAbove();
		}
		return currentDirection.isUp();
	}

	private boolean shouldOpen() {
		if (shouldChangeDirection()) {
			currentDirection = Direction.NIL;
		}
		return hasOrderToCurrentFloor(currentDirection)
				|| (currentDirection.isNil() && (hasOrderToCurrentFloor(Direction.UP) || hasOrderToCurrentFloor(Direction.DOWN)));
	}

	private boolean shouldChangeDirection() {
		boolean shouldChange = true;
		if (currentDirection.isUp()) {
			shouldChange = countOrdersAbove() == 0;
		}
		if (currentDirection.isDown()) {
			shouldChange = countOrdersBelow() == 0;
		}
		return shouldChange;
	}

	private int countOrdersAbove() {
		int countCallsUp = 0;
		for (Order call : orders) {
			if (call.isHigherThan(currentFloor)) {
				countCallsUp++;
			}
		}
		return countCallsUp;
	}

	private int countOrdersBelow() {
		int countCallsDown = 0;
		for (Order call : orders) {
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
		orders.remove(new Call(currentFloor, Direction.UP));
		orders.remove(new Call(currentFloor, Direction.DOWN));
		orders.remove(new GoTo(currentFloor, Direction.UP));
		orders.remove(new GoTo(currentFloor, Direction.DOWN));
	}

	public void call(int floor, Direction direction) {
		orders.add(new Call(floor, direction));
		incrementFrequency(floor);
	}

	public void goTo(int floor) {
		Direction dir = Direction.UP;
		if (floor < currentFloor) {
			dir = Direction.DOWN;
		}
		orders.add(new GoTo(floor, dir));
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

	private boolean hasOrderToCurrentFloor(Direction dir) {
		return orders.contains(new Call(currentFloor, dir)) || orders.contains(new GoTo(currentFloor, dir));
	}

	private void incrementFrequency(int floor) {
		frequencies.put(floor, frequencies.get(floor) + 1);
	}

}
