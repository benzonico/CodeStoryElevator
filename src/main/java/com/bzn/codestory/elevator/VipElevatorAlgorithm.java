package com.bzn.codestory.elevator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VipElevatorAlgorithm extends ElevatorAlgorithm {

	Logger logger = LoggerFactory.getLogger(VipElevatorAlgorithm.class);

	public VipElevatorAlgorithm(Elevator elevator) {
		super(elevator);
	}

	@Override
	public boolean shouldClose() {
		return getElevator().getPotentialScoreForCurrentFloor() == 0;
	}

	@Override
	public boolean shouldOpen() {
		boolean hasCallsMakingPoints = getElevator()
				.getPotentialScoreForCurrentFloor() > 0;
		boolean hasGotos = getElevator().getOrders().hasGoTo(
				getElevator().currentFloor);
		boolean shouldOpen = hasCallsMakingPoints || hasGotos;
		if (shouldChangeDirection()) {
			getElevator().setCurrentDirection(Direction.NIL);
		}
		logger.info("opening doors - reason : "
				+ getOpeningReason(hasCallsMakingPoints, hasGotos));
		return shouldOpen;
	}

	private String getOpeningReason(boolean hasCallsMakingPoints,
			boolean hasGotos) {
		if (hasCallsMakingPoints) {
			return "Calls making points detected";
		} else {
			return "Users want to exit";
		}
	}

	@Override
	public boolean shouldChangeDirection() {
		boolean shouldChange = false;
		if (getElevator().getCurrentDirection().isUp()) {
			shouldChange = (getElevator().getOrders()
					.countGoToAbove(getElevator().currentFloor) == 0 && getPotentialScoreUpper() < getPotentialScoreLower())
					|| getElevator().isAtTop();
		}
		if (getElevator().getCurrentDirection().isDown()) {
			shouldChange = (getElevator().getOrders().countGoToAbove(
					getElevator().currentFloor) == 0 && getPotentialScoreLower() < getPotentialScoreUpper())
					|| getElevator().isAtBottom();
		}
		return shouldChange;
	}

	private int getPotentialScoreUpper() {
		int potentialScoreUpper = 0;
		for (int floor = getElevator().getHigher(); floor > getElevator().currentFloor; floor--) {
			potentialScoreUpper = potentialScoreUpper
					+ getElevator().getPotentialScoreForFloor(floor);
		}
		return potentialScoreUpper;
	}

	private int getPotentialScoreLower() {
		int potentialScoreLower = 0;
		for (int floor = getElevator().getLower(); floor < getElevator().currentFloor; floor++) {
			potentialScoreLower = potentialScoreLower
					+ getElevator().getPotentialScoreForFloor(floor);
		}
		return potentialScoreLower;
	}

	@Override
	public boolean shouldGoUp() {
		if (getElevator().getCurrentDirection().isNil()) {
			return getPotentialScoreLower() < getPotentialScoreUpper()
					|| getElevator().isAtBottom();
		}
		return getElevator().getCurrentDirection().isUp();
	}

}
