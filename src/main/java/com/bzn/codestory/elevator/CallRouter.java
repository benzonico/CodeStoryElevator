package com.bzn.codestory.elevator;

public class CallRouter {

	public Elevator route(Elevator[] elevators, int floor, Direction to) {
		Elevator result = null;
		for (Elevator elevator : elevators) {
			if (elevator.getCurrentDirection().equals(to)
					&& floorIsInDirectionOfElevator(elevator.currentFloor,
							floor, elevator.getCurrentDirection())) {
				result = elevator;
				break;
			}
		}
		if (result == null) {
			for (Elevator elevator : elevators) {
				if (floorIsInDirectionOfElevator(elevator.currentFloor, floor,
						elevator.getCurrentDirection())) {
					result = elevator;
					break;
				}
			}
		}
		if (result == null) {
			for (Elevator elevator : elevators) {
				if (result == null
						|| result.getUsers().countUsersInCabin() > elevator
								.getUsers().countUsersInCabin()) {
					result = elevator;
				}
			}
		}
		return result;
	}

	private boolean floorIsInDirectionOfElevator(int currentFloor, int floor,
			Direction elevatorDir) {
		int diff = currentFloor - floor;
		if (elevatorDir.isUp()) {
			diff = -1 * diff;
		}
		return diff > 0;
	}
}

