package com.bzn.codestory.elevator;

public enum Command {

	NOTHING, OPEN, OPEN_UP, OPEN_DOWN, CLOSE, UP, DOWN;

	static Command open(Direction direction) {
		switch (direction) {
		case UP:
			return OPEN_UP;
		case DOWN:
			return OPEN_DOWN;
		default:
			return OPEN;
		}
	}
}
