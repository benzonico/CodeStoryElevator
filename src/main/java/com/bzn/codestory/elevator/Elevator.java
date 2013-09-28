package com.bzn.codestory.elevator;

import static com.bzn.codestory.elevator.Command.CLOSE;
import static com.bzn.codestory.elevator.Command.DOWN;
import static com.bzn.codestory.elevator.Command.NOTHING;
import static com.bzn.codestory.elevator.Command.UP;

import java.util.HashSet;
import java.util.Set;

public class Elevator {

	private int currentFloor;

	// private Call calledAt;
	// private Set<Call> calls;

	private boolean open;

	private Set<Integer> goingTo;

	private int users;

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
		// calledAt = null;
		// calls = new HashSet<>();
		open = false;
		goingTo = new HashSet<>();
		users = 0;
	}

	public Command nextCommand() {
		if (open) {
			open = false;
			return CLOSE;
		}
		// if (calledAt != null && calledAt.getFloor() == currentFloor) {
		// calls.remove(calledAt);
		// calledAt = calls.isEmpty() ? null : calls.iterator().next();
		// }
		if (goingTo.contains(currentFloor)) {
			goingTo.remove(currentFloor);
			return open();
		} else if (!goingTo.isEmpty()) {
			if (currentFloor < goingTo.iterator().next()) {
				return up();
			} else {
				return down();
			}
		}
		return NOTHING;
	}

	private Command down() {
		currentFloor--;
		return DOWN;
	}

	private Command up() {
		currentFloor++;
		return UP;
	}

	private Command open() {
		open = true;
		return Command.OPEN;
	}

	public void call(int floor, Direction direction) {
		goTo(floor);
		// Call call = new Call(floor, direction);
		// if (calls.isEmpty()) {
		// calledAt = call;
		// }
		// calls.add(call);
	}

	public void goTo(int floor) {
		goingTo.add(floor);
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
