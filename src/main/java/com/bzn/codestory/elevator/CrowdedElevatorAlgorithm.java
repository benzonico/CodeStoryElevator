package com.bzn.codestory.elevator;

public class CrowdedElevatorAlgorithm extends ElevatorAlgorithm {

	protected CrowdedElevatorAlgorithm(Elevator elevator) {
		super(elevator);
	}

	@Override
	public boolean shouldClose() {
		return true;
	}

	@Override
	public boolean shouldGoUp() {
		if (getElevator().getCurrentDirection().isNil()) {
			return getElevator().getUsers().countGoToBelow(
					getElevator().currentFloor) <= getElevator()
					.getUsers()
					.countGoToAbove(getElevator().currentFloor);
		}
		return getElevator().getCurrentDirection().isUp();
	}

	@Override
	public boolean shouldOpen() {
		if (shouldChangeDirection()) {
			getElevator().setCurrentDirection(Direction.NIL);
		}
		return getElevator().getUsers().hasUsersGoingTo(getElevator().currentFloor);
	}

	@Override
	public boolean shouldChangeDirection() {
		boolean shouldChange = true;
		if (getElevator().getCurrentDirection().isUp()) {
			shouldChange = getElevator().getUsers().countGoToAbove(
					getElevator().currentFloor) == 0;
		}
		if (getElevator().getCurrentDirection().isDown()) {
			shouldChange = getElevator().getUsers().countGoToBelow(
					getElevator().currentFloor) == 0;
		}
		return shouldChange;
	}

}
