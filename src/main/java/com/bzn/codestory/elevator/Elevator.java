package com.bzn.codestory.elevator;

import static com.bzn.codestory.elevator.Command.CLOSE;
import static com.bzn.codestory.elevator.Command.DOWN;
import static com.bzn.codestory.elevator.Command.NOTHING;
import static com.bzn.codestory.elevator.Command.UP;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Elevator {

	private int currentFloor;
	private Set<Call> calls;
	private boolean open;
	private int users;
	private Direction currentDirection;

	public Elevator() {
		this(0);
	}

	public Elevator(int startFloor) {
		reset(startFloor);
	}

	public void reset() {
		reset(0);
	}

	private void reset(int startFloor) {
		currentFloor = startFloor;
		calls = new HashSet<>();
		open = false;
		users = 0;
		currentDirection = Direction.NIL;
	}

	public Command nextCommand() {
		if (open) {
			open = false;
			return CLOSE;
		}
		if (shouldOpen()) {
			removeAllCallOfCurrentFloor();
			return open();
		} else if (!calls.isEmpty()) {
			if (shouldGoUp()) {
				return up();
			} else {
				return down();
			}
		}
		currentDirection = Direction.NIL;
		return NOTHING;
	}

	private void removeAllCallOfCurrentFloor() {
		Iterator<Call> callIterator = calls.iterator();
		while (callIterator.hasNext()) {
			Call call = callIterator.next();
			if (call.isAt(currentFloor)) {
				callIterator.remove();
			}
		}

	}

	private boolean shouldGoUp() {
		if (currentDirection.isUp()) {
			for (Call call : calls) {
				if (call.isHigherThan(currentFloor)) {
					return true;
				}
			}
		}
		if (currentDirection.isNil()) {
			int countCallsUp = 0;
			for (Call call : calls) {
				if (call.isLowerThan(currentFloor)) {
					countCallsUp--;
				} else {
					countCallsUp++;
				}
			}
			return countCallsUp >= 0;
		}
		return false;
	}

	private boolean shouldOpen() {
		boolean hasFollowingCall = false;
		if (currentDirection.isUp()) {
			for (Call call : calls) {
				hasFollowingCall = hasFollowingCall
						|| call.isHigherThan(currentFloor);
			}
		}
		if (currentDirection.isDown()) {
			for (Call call : calls) {
				hasFollowingCall = hasFollowingCall
						|| call.isLowerThan(currentFloor);
			}
		}
		if (!hasFollowingCall) {
			currentDirection = Direction.NIL;
		}
		return calls.contains(new Call(currentFloor, currentDirection))
				|| (currentDirection.isNil() && (calls.contains(new Call(
						currentFloor, Direction.UP)) || calls
						.contains(new Call(currentFloor, Direction.DOWN))));
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

	private Command open() {
		open = true;
		return Command.OPEN;
	}

	public void call(int floor, Direction direction) {
		Call call = new Call(floor, direction);
		calls.add(call);
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

}
