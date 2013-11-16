package com.bzn.codestory.elevator;

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
		orders.remove(new Call(currentFloor, Direction.UP));
		orders.remove(new Call(currentFloor, Direction.DOWN));
		orders.remove(new GoTo(currentFloor, Direction.UP));
		orders.remove(new GoTo(currentFloor, Direction.DOWN));
	}

	public void add(Order order) {
		orders.add(order);
	}

	public boolean hasOrderTo(int currentFloor, Direction dir) {
		return orders.contains(new Call(currentFloor, dir)) || orders.contains(new GoTo(currentFloor, dir));
	}
}
