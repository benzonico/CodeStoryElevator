package com.bzn.codestory.elevator;

public class OneDirectionElevatorAlgorithm extends ElevatorAlgorithm {

	public OneDirectionElevatorAlgorithm(Elevator elevator) {
		super(elevator);
	}

	@Override
	public boolean shouldClose() {
		if (getElevator().getCurrentDirection().isNil()) {
			return !getElevator().hasUsersCallingFromCurrentFloor();
		} else {
			return !getElevator().hasUsersCallingFromCurrentFloorGoingInSameDirection();
		}
	}

	@Override
	public boolean shouldOpen() {
		if (shouldChangeDirection()) {
			getElevator().setCurrentDirection(Direction.NIL);
		}
		return getElevator().hasUsersCallingFromCurrentFloorGoingInSameDirection() || getElevator().hasUsersGoingToCurrentFloor()
				|| (getElevator().getCurrentDirection().isNil() && getElevator().hasUsersCallingFromCurrentFloor());
	}

	@Override
	public boolean shouldChangeDirection() {
		boolean shouldChange = false;
		if (!getElevator().hasUsersCallingFromCurrentFloorGoingInSameDirection()) {
			if (getElevator().getCurrentDirection().isUp()) {
				shouldChange = getElevator().countUsersAboveCurrentFloor() == 0;
			}
			if (getElevator().getCurrentDirection().isDown()) {
				shouldChange = getElevator().countUsersBelowCurrentFloor() == 0;
			}
		}
		return shouldChange;
	}

	@Override
	public boolean shouldGoUp() {
		if (getElevator().getCurrentDirection().isNil()) {
			return getElevator().countUsersBelowCurrentFloor() <= getElevator().countUsersAboveCurrentFloor();
		}
		return getElevator().getCurrentDirection().isUp();
	}

}
