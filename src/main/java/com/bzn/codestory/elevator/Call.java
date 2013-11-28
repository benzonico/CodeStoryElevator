package com.bzn.codestory.elevator;

public class Call extends Order implements Comparable<Call> {

	public Call(Integer floor, Direction direction) {
		super(floor, direction);
	}

	@Override
	public int compareTo(Call other) {
		int comparison = floor.compareTo(other.floor);
		if(comparison == 0){
			comparison = direction.compareTo(other.direction);
		}
		return comparison;
	}

}
