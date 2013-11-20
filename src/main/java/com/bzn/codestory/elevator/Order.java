package com.bzn.codestory.elevator;

public abstract class Order {

	protected Direction direction;
	protected int floor;

	protected Order(int floor, Direction direction) {
		this.floor = floor;
		this.direction = direction;
	}

	public abstract boolean isOutput();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + floor;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (direction != other.direction)
			return false;
		if (floor != other.floor)
			return false;
		return true;
	}

	public boolean isAtFloor(int currentFloor) {
		return floor == currentFloor;
	}

	public boolean isHigherThan(int currentFloor) {
		return floor > currentFloor;
	}

	public boolean isLowerThan(int currentFloor) {
		return floor < currentFloor;
	}

}
