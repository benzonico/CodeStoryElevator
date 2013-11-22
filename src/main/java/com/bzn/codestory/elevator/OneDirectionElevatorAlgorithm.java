package com.bzn.codestory.elevator;

public class OneDirectionElevatorAlgorithm extends ElevatorAlgorithm {

	public OneDirectionElevatorAlgorithm(Elevator elevator) {
		super(elevator);
	}

	@Override
	public boolean shouldClose() {
		return !getElevator().getOrders().hasCallsFrom(
				getElevator().currentFloor);
	}

	@Override
	public boolean shouldOpen() {
		if (shouldChangeDirection()) {
			getElevator().setCurrentDirection(Direction.NIL);
		}
		return getElevator().getOrders().hasOrderTo(getElevator().currentFloor,
				getElevator().getCurrentDirection())
				|| (getElevator().getCurrentDirection().isNil() && getElevator()
						.getOrders().hasOrderTo(getElevator().currentFloor));
	}

	@Override
	public boolean shouldChangeDirection() {
		boolean shouldChange = true;
		if (getElevator().getCurrentDirection().isUp()) {
			shouldChange = getElevator().getOrders().countOrdersAbove(
					getElevator().currentFloor) == 0;
		}
		if (getElevator().getCurrentDirection().isDown()) {
			shouldChange = getElevator().getOrders().countOrdersBelow(
					getElevator().currentFloor) == 0;
		}
		return shouldChange;
	}

	@Override
	public boolean shouldGoUp() {
		if (getElevator().getCurrentDirection().isNil()) {
			return getElevator().getOrders().countOrdersBelow(
					getElevator().currentFloor) <= getElevator().getOrders()
					.countOrdersAbove(getElevator().currentFloor);
		}
		return getElevator().getCurrentDirection().isUp();
	}

}
