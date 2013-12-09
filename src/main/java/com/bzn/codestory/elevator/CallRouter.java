package com.bzn.codestory.elevator;

import java.util.SortedMap;

import com.google.common.collect.Maps;

public class CallRouter {

	public Elevator route(Elevator[] elevators, Floor floor, Direction to) {
		SortedMap<Double, Elevator> orderedElevators = Maps.newTreeMap();
		for (Elevator elevator : elevators) {
			orderedElevators.put(elevator.getDistanceToFloor(floor, to), elevator);
		}
		return orderedElevators.get(orderedElevators.firstKey());
	}
}

