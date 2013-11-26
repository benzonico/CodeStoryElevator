package com.bzn.codestory.elevator;

public class OneDirectionElevatorAlgorithm extends ElevatorAlgorithm {

	public OneDirectionElevatorAlgorithm(Elevator elevator) {
		super(elevator);
	}

	@Override
	public boolean shouldClose() {
		return !getElevator().getUsers().hasUsersCallingFrom(
				getElevator().currentFloor);
	}

	@Override
	public boolean shouldOpen() {
		if (shouldChangeDirection()) {
			getElevator().setCurrentDirection(Direction.NIL);
		}
		return getElevator().getUsers().hasOrderTo(getElevator().currentFloor,
				getElevator().getCurrentDirection())
				|| (getElevator().getCurrentDirection().isNil() && getElevator()
						.getUsers().hasOrderTo(getElevator().currentFloor));
	}

	@Override
	public boolean shouldChangeDirection() {
		boolean shouldChange = false;
		if (!getElevator().getUsers().hasOrderTo(getElevator().currentFloor,
				getElevator().getCurrentDirection())) {
			if (getElevator().getCurrentDirection().isUp()) {
				shouldChange = getElevator().getUsers().countUsersAbove(
						getElevator().currentFloor) == 0;
			}
			if (getElevator().getCurrentDirection().isDown()) {
				shouldChange = getElevator().getUsers().countUsersBelow(
						getElevator().currentFloor) == 0;
			}
		}
		return shouldChange;
	}

	@Override
	public boolean shouldGoUp() {
		if (getElevator().getCurrentDirection().isNil()) {
			return getElevator().getUsers().countUsersBelow(
					getElevator().currentFloor) <= getElevator().getUsers()
					.countUsersAbove(getElevator().currentFloor);
		}
		return getElevator().getCurrentDirection().isUp();
	}

}
