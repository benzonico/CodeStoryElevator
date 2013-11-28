package com.bzn.codestory.elevator;

public class GoTo extends Order implements Comparable<GoTo> {

	public GoTo(int floor, Direction direction) {
		super(floor, direction);
	}

	@Override
	public int compareTo(GoTo other) {
		int comparison = floor.compareTo(other.floor);
		if (comparison == 0) {
			comparison = direction.compareTo(other.direction);
		}
		return comparison;
	}
}
