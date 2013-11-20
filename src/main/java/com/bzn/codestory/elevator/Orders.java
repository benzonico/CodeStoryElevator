package com.bzn.codestory.elevator;

import static com.bzn.codestory.elevator.Direction.DOWN;
import static com.bzn.codestory.elevator.Direction.UP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Orders {
	private List<Order> orders;

	public Orders() {
		orders = new ArrayList<>();
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
		Iterator<Order> iterOrders = orders.iterator();
		while (iterOrders.hasNext()) {
			Order order = (Order) iterOrders.next();
			if (order.isAtFloor(currentFloor)) {
				iterOrders.remove();
			}
		}
	}

	public void add(Order order) {
		orders.add(order);
	}

	public boolean hasOrderTo(int currentFloor, Direction dir) {
		return orders.contains(new Call(currentFloor, dir, 0))
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
