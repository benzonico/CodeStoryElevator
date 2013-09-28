package com.bzn.codestory.elevator;

import static com.bzn.codestory.elevator.Command.CLOSE;
import static com.bzn.codestory.elevator.Command.DOWN;
import static com.bzn.codestory.elevator.Command.NOTHING;
import static com.bzn.codestory.elevator.Command.UP;

public class Elevator {

	private int currentFloor;

	private Integer calledAt;

	private boolean open;

	private Integer goingTo;

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
		calledAt = null;
		open = false;
		goingTo = null;
		users = 0;
	}

	public Command nextCommand() {
		if (open) {
			open = false;
			return CLOSE;
		}

		if (goingTo != null) {
			if (currentFloor == goingTo) {
				goingTo = null;
				return open();
			} else if (currentFloor < goingTo) {
				return up();
			} else {
				return down();
			}
		}

		if (calledAt == null) {
			return NOTHING;
		} else if (calledAt == currentFloor) {
			calledAt = null;
			return open();
		} else if (calledAt > currentFloor) {
			return up();
		} else {
			return down();
		}
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
		calledAt = floor;
	}

	public void goTo(int floor) {
		if (floor != currentFloor) {
			goingTo = floor;
		}
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
