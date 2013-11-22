package com.bzn.codestory.elevator;

public abstract class ElevatorAlgorithm {
	private Elevator elevator;

	protected ElevatorAlgorithm(Elevator elevator) {
		this.elevator = elevator;
	}

	protected Elevator getElevator() {
		return elevator;
	}

	public abstract boolean shouldClose();

	public abstract boolean shouldOpen();

	public abstract boolean shouldChangeDirection();

	public abstract boolean shouldGoUp();
}
