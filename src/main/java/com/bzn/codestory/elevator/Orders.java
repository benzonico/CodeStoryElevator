package com.bzn.codestory.elevator;

import static com.bzn.codestory.elevator.Direction.DOWN;
import static com.bzn.codestory.elevator.Direction.UP;

import java.util.HashSet;
import java.util.Set;

public class Orders {
	private Set<Order> orders;

	public Orders() {
		orders = new HashSet<>();
	}

	public boolean hasOrder() {
		return !orders.isEmpty();
	}

	public int countOrdersAbove(int currentFloor) {
		int countCallsUp = 0;
		for (Order call : orders) {
			if (call.isHigherThan(currentFloor)) {
				countCallsUp++;
			}
		}
		return countCallsUp;
	}

	public int countOrdersBelow(int currentFloor) {
		int countCallsDown = 0;
		for (Order call : orders) {
			if (call.isLowerThan(currentFloor)) {
				countCallsDown++;
			}
		}
		return countCallsDown;
	}

	public void remove(int currentFloor) {
		// TODO change this big crap
		orders.remove(new Call(currentFloor, UP));
		orders.remove(new Call(currentFloor, DOWN));
		orders.remove(new GoTo(currentFloor, UP));
		orders.remove(new GoTo(currentFloor, DOWN));
	}

	public void add(Order order) {
		orders.add(order);
	}

	public boolean hasOrderTo(int currentFloor, Direction dir) {
		return orders.contains(new Call(currentFloor, dir))
				|| orders.contains(new GoTo(currentFloor, dir));
	}

	public boolean hasOrderTo(int currentFloor) {
		return hasOrderTo(currentFloor, UP) || hasOrderTo(currentFloor, DOWN);
	}

	public boolean hasGoTo(int currentFloor) {
		return orders.contains(new GoTo(currentFloor, UP))
				|| orders.contains(new GoTo(currentFloor, DOWN));
	}

	public int countGoToAbove(int currentFloor) {
		int countGotosUp = 0;
		for (Order call : orders) {
			if (call.isHigherThan(currentFloor) && call.isOutput()) {
				countGotosUp++;
			}
		}
		return countGotosUp;
	}

	public int countGoToBelow(int currentFloor) {
		int countGotosDown = 0;
		for (Order call : orders) {
			if (call.isLowerThan(currentFloor) && call.isOutput()) {
				countGotosDown++;
			}
		}
		return countGotosDown;
	}
}
