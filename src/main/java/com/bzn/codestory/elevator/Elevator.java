package com.bzn.codestory.elevator;

import static com.bzn.codestory.elevator.Command.CLOSE;
import static com.bzn.codestory.elevator.Command.DOWN;
import static com.bzn.codestory.elevator.Command.NOTHING;
import static com.bzn.codestory.elevator.Command.UP;

import java.util.List;

import com.google.common.annotations.VisibleForTesting;

public class Elevator {

	private static final int DEFAULT_FLOORS = 20;

	private static final int DEFAULT_CABIN_SIZE = 30;

	int currentFloor;
	int balanceFloor;
	private int cabinSize;
	private int lower;
	private int higher;
	private Users users;
	private boolean open;
	private Direction currentDirection;

	private CrowdedElevatorAlgorithm crowdedElevatorAlgorithm;
	private OneDirectionElevatorAlgorithm oneDirectionElevatorAlgorithm;

	private Clock clock;

	public Elevator() {
		this(0);
	}

	@VisibleForTesting
	Elevator(int startFloor) {
		this(startFloor, DEFAULT_FLOORS / 2);
	}

	@VisibleForTesting
	Elevator(int startFloor, int balanceFloor) {
		reset(0, DEFAULT_FLOORS - 1, DEFAULT_CABIN_SIZE, new Clock());
		this.balanceFloor = balanceFloor;
		currentFloor = startFloor;
		crowdedElevatorAlgorithm = new CrowdedElevatorAlgorithm(this);
		oneDirectionElevatorAlgorithm = new OneDirectionElevatorAlgorithm(this);
	}

	public void reset(int lower, int higher, int cabinSize, Clock horloge) {
		this.lower = lower;
		this.higher = higher;
		currentFloor = 0;
		balanceFloor = newBalanceFloor();
		this.cabinSize = cabinSize;
		this.clock = horloge;
		reset();
	}

	private void reset() {
		currentFloor = 0;
		users = new Users(lower, higher);
		open = false;
		currentDirection = Direction.NIL;
	}

	public Command nextCommand() {
		ElevatorAlgorithm algorithm = chooseAlgorithm();
		if (open) {
			if (algorithm.shouldClose()) {
				return close();
			} else {
				return stayOpen();
			}
		}
		if (algorithm.shouldOpen()) {
			return open();
		} else if (users.hasOrder()) {
			if (algorithm.shouldGoUp()) {
				return up();
			} else {
				return down();
			}
		}
		return idle();
	}

	private ElevatorAlgorithm chooseAlgorithm() {
		if (isCabinFull()) {
			return crowdedElevatorAlgorithm;
		} else {
			return oneDirectionElevatorAlgorithm;
		}
	}

	private boolean isCabinFull() {
		return users.countUsersInCabin() == cabinSize;
	}

	private Command down() {
		currentFloor--;
		currentDirection = Direction.DOWN;
		return DOWN;
	}

	private Command up() {
		currentFloor++;
		currentDirection = Direction.UP;
		return UP;
	}

	private Command close() {
		open = false;
		return CLOSE;
	}

	private Command open() {
		open = true;
		users.emptyCallsForFloorAndDirection(currentFloor, currentDirection);
		return Command.open(currentDirection);
	}

	private Command idle() {
		Command idleCommand = doNothing();
		if (currentFloor > balanceFloor) {
			idleCommand = down();
		} else if (currentFloor < balanceFloor) {
			idleCommand = up();
		}
		return idleCommand;
	}
	
	private int newBalanceFloor() {
		return (int) (lower + Math.random() * (higher - lower + 1));
	}

	private Command doNothing() {
		currentDirection = Direction.NIL;
		return NOTHING;
	}
	
	private Command stayOpen() {
		users.emptyCallsForFloorAndDirection(currentFloor, currentDirection);
		return NOTHING;
	}

	public void call(int floor, Direction direction) {
		users.userCalls(new Call(floor, direction), clock);
	}

	public void goTo(int floor) {
		Direction dir = Direction.UP;
		if (floor < currentFloor) {
			dir = Direction.DOWN;
		}
		users.userWantsToGoTo(new GoTo(floor, dir), currentFloor);
	}

	public void userEntered(User userEntering) {
		users.enter(userEntering, currentFloor, clock);
	}

	public void userExited() {
		users.userExitsAt(currentFloor);
	}

	public ElevatorStatus getStatus() {
		return new ElevatorStatus(currentFloor, usersInCabin(), cabinSize, open, currentDirection, users);
	}

	public Direction getCurrentDirection() {
		return currentDirection;
	}

	public Users getUsers() {
		return users;
	}

	public int getLower() {
		return lower;
	}

	public int getHigher() {
		return higher;
	}

	public void setCurrentDirection(Direction direction) {
		this.currentDirection = direction;
	}

	private int getRemainingPlacesInCabin() {
		return cabinSize - users.countUsersInCabin();
	}

	public int getPotentialScoreForCurrentFloor() {
		return getPotentialScoreForFloor(currentFloor);
	}

	public int getPotentialScoreForFloor(int floor) {
		List<User> usersToInspect = users.getUsersThatCanBeTakenInCabinAtFloor(floor, getRemainingPlacesInCabin());
		int floorPotentialScore = 0;
		for (User user : usersToInspect) {
			floorPotentialScore = floorPotentialScore
					+ user.getPotentialMaxScore(currentFloor, currentDirection, lower, higher, clock);
		}
		return floorPotentialScore;
	}

	public boolean isAtTop() {
		return currentFloor == higher;
	}

	public boolean isAtBottom() {
		return currentFloor == lower;
	}

	public int usersInCabin() {
		return users.countUsersInCabin();
	}

	public boolean hasUsersCallingFromCurrentFloor() {
		return users.hasUsersCallingFrom(currentFloor);
	}

	public boolean hasUsersCallingFromCurrentFloorGoingInSameDirection() {
		return users.hasUsersCallingFrom(currentFloor, currentDirection);
	}

	public boolean hasUsersGoingToCurrentFloor() {
		return users.hasUsersGoingTo(currentFloor);
	}

	public int countUsersAboveCurrentFloor() {
		return users.countUsersAbove(currentFloor);
	}

	public int countUsersBelowCurrentFloor() {
		return users.countUsersBelow(currentFloor);
	}

	public int countUsersGoingAbove() {
		return users.countGoToAbove(currentFloor);
	}

	public int countUsersGoingBelow() {
		return users.countGoToBelow(currentFloor);
	}

	boolean floorIsInSameDirection(int floor) {
		int diff = currentFloor - floor;
		if (getCurrentDirection().isUp()) {
			diff = -1 * diff;
		}
		return diff > 0;
	}

	boolean hasMoreUsersInCabinThan(Elevator elevator) {
		return users.countUsersInCabin() > elevator.users.countUsersInCabin();
	}

}
