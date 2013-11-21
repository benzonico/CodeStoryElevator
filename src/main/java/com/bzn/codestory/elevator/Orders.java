package com.bzn.codestory.elevator;

import java.util.Iterator;
import java.util.Queue;
import java.util.SortedMap;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

public class Orders {
	private SortedMap<Integer, Queue<Call>> calls;
	private SortedMap<Integer, Queue<GoTo>> gotos;

	public Orders(int lower, int higher) {
		calls = Maps.newTreeMap();
		gotos = Maps.newTreeMap();
		for (int floor = lower; floor < higher + 1; floor++) {
			calls.put(floor, Queues.<Call> newArrayDeque());
			gotos.put(floor, Queues.<GoTo> newArrayDeque());
		}
	}

	public boolean hasOrder() {
		boolean hasOrders = false;
		Iterator<Integer> iterator = calls.keySet().iterator();
		while (iterator.hasNext() && !hasOrders) {
			int floor = iterator.next();
			hasOrders = hasOrderTo(floor);
		}
		return hasOrders;
	}

	public int countOrdersAbove(int currentFloor) {
		int countCallsUp = 0;
		for (Integer floor: calls.keySet()) {
			if (floor > currentFloor) {
				countCallsUp += calls.get(floor).size() + gotos.get(floor).size();
			}
		}
		return countCallsUp;
	}

	public int countOrdersBelow(int currentFloor) {
		int countCallsDown = 0;
		for (Integer floor: calls.keySet()) {
			if (floor < currentFloor) {
				countCallsDown += calls.get(floor).size() + gotos.get(floor).size();
			}
		}
		return countCallsDown;
	}

	public void add(Call order) {
		calls.get(order.floor).add(order);
	}

	public void add(GoTo order) {
		gotos.get(order.floor).add(order);
	}

	public boolean hasOrderTo(int currentFloor, Direction dir) {
		return calls.get(currentFloor).contains(new Call(currentFloor, dir, 0))
				|| gotos.get(currentFloor).contains(new GoTo(currentFloor, dir));
	}

	public boolean hasOrderTo(int currentFloor) {
		return hasCallsFrom(currentFloor) || hasGoTo(currentFloor);
	}

	public boolean hasGoTo(int currentFloor) {
		return !gotos.get(currentFloor).isEmpty();
	}

	public boolean hasCallsFrom(int currentFloor) {
		return !calls.get(currentFloor).isEmpty();
	}

	public int countGoToAbove(int currentFloor) {
		int countGotosUp = 0;
		for (int floor : gotos.keySet()) {
			if (floor > currentFloor) {
				countGotosUp += gotos.get(floor).size();
			}
		}
		return countGotosUp;
	}

	public int countGoToBelow(int currentFloor) {
		int countGotosDown = 0;
		for (int floor : gotos.keySet()) {
			if (floor < currentFloor) {
				countGotosDown += gotos.get(floor).size();
			}
		}
		return countGotosDown;
	}

	public void forgetOldestGoToAtFloor(int currentFloor) {
		if (!gotos.get(currentFloor).isEmpty()) {
			gotos.get(currentFloor).remove();
		}
	}

	public void forgetOldestCallAtFloor(int currentFloor) {
		if (!calls.get(currentFloor).isEmpty()) {
			calls.get(currentFloor).remove();
		}
	}
}
