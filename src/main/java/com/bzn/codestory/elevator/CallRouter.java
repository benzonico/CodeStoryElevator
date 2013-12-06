package com.bzn.codestory.elevator;

public class CallRouter {

	public Elevator route(Elevator[] elevators, Floor floor, Direction to) {
		Elevator result = null;
		for (Elevator elevator : elevators) {
			if (elevator.getCurrentDirection().equals(to)
					&& elevator.floorIsInSameDirection(floor.getFloorNumber())) {
				result = elevator;
				break;
			}
		}
		if (result == null) {
			for (Elevator elevator : elevators) {
				if (elevator.floorIsInSameDirection(floor.getFloorNumber())) {
					result = elevator;
					break;
				}
			}
		}
		if (result == null) {
			for (Elevator elevator : elevators) {
				if (result == null) {
					// Choose first elevator
					result = elevator;
				} else if (result.hasMoreUsersInCabinThan(elevator)) {
					// Assign better elevator
					result = elevator;
				}
			}
		}
		return result;
	}
}

