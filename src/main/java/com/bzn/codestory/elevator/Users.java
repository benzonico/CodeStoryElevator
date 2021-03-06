package com.bzn.codestory.elevator;

import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Users {
	private SortedMap<Integer, List<User>> usersCalling;
	private SortedMap<Integer, List<User>> usersInCabin;

	public Users(int lower, int higher) {
		usersCalling = Maps.newTreeMap();
		usersInCabin = Maps.newTreeMap();
		for (int floor = lower; floor < higher + 1; floor++) {
			usersCalling.put(floor, Lists.<User> newArrayList());
			usersInCabin.put(floor, Lists.<User> newArrayList());
		}
	}

	public boolean hasOrder() {
		boolean hasOrders = false;
		Iterator<Integer> iterator = usersCalling.keySet().iterator();
		while (iterator.hasNext() && !hasOrders) {
			int floor = iterator.next();
			hasOrders = hasOrderTo(floor);
		}
		return hasOrders;
	}

	public int countUsersAbove(int currentFloor) {
		int countCallsUp = 0;
		for (Integer floor : usersCalling.keySet()) {
			if (floor > currentFloor) {
				countCallsUp += usersCalling.get(floor).size()
						+ usersInCabin.get(floor).size();
			}
		}
		return countCallsUp;
	}

	public int countUsersBelow(int currentFloor) {
		int countCallsDown = 0;
		for (Integer floor : usersCalling.keySet()) {
			if (floor < currentFloor) {
				countCallsDown += usersCalling.get(floor).size()
						+ usersInCabin.get(floor).size();
			}
		}
		return countCallsDown;
	}

	public void userCalls(Call order, Clock clock) {
		User newUser = new User(order, clock);
		usersCalling.get(order.floor).add(newUser);
	}

	public void userWantsToGoTo(GoTo order, int currentFloor) {
		List<User> usersToFloor = usersInCabin.get(currentFloor);
		User lastUserInCabin = usersToFloor.remove(usersToFloor.size() - 1);
		lastUserInCabin.goTo(order);
		usersInCabin.get(order.floor).add(lastUserInCabin);
	}

	public void enter(User userEntering, int currentFloor, Clock clock) {
		userEntering.enterCabin(clock);
		usersInCabin.get(currentFloor).add(userEntering);
	}

	public boolean hasOrderTo(int currentFloor, Direction dir) {
		return hasUsersCallingFrom(currentFloor, dir)
				|| hasUsersGoingTo(currentFloor, dir);
	}

	public boolean hasUsersGoingTo(int currentFloor, Direction dir) {
		boolean hasOrderTo;
		hasOrderTo = false;
		for (User user : usersInCabin.get(currentFloor)) {
			if (user.getCall().direction == dir) {
				hasOrderTo = true;
			}
		}
		return hasOrderTo;
	}

	public boolean hasUsersCallingFrom(int currentFloor, Direction dir) {
		boolean hasOrderTo = false;
		for (User user : usersCalling.get(currentFloor)) {
			if (user.getCall().direction == dir) {
				hasOrderTo = true;
			}
		}
		return hasOrderTo;
	}

	public boolean hasOrderTo(int currentFloor) {
		return hasUsersCallingFrom(currentFloor)
				|| hasUsersGoingTo(currentFloor);
	}

	public boolean hasUsersGoingTo(int currentFloor) {
		return !usersInCabin.get(currentFloor).isEmpty();
	}

	public boolean hasUsersCallingFrom(int currentFloor) {
		return !usersCalling.get(currentFloor).isEmpty();
	}

	public int countGoToAbove(int currentFloor) {
		int countGotosUp = 0;
		for (int floor : usersInCabin.keySet()) {
			if (floor > currentFloor) {
				countGotosUp += usersInCabin.get(floor).size();
			}
		}
		return countGotosUp;
	}

	public int countGoToBelow(int currentFloor) {
		int countGotosDown = 0;
		for (int floor : usersInCabin.keySet()) {
			if (floor < currentFloor) {
				countGotosDown += usersInCabin.get(floor).size();
			}
		}
		return countGotosDown;
	}

	public void userExitsAt(int currentFloor) {
		if (!usersInCabin.get(currentFloor).isEmpty()) {
			usersInCabin.get(currentFloor).remove(0);
		}
	}

	public int countUsersInCabin() {
		int countUsersInCabin = 0;
		for (int floor : usersInCabin.keySet()) {
			countUsersInCabin += usersInCabin.get(floor).size();
		}
		return countUsersInCabin;
	}

	public SortedMap<Integer, List<User>> getUsersCalling() {
		return usersCalling;
	}

	public SortedMap<Integer, List<User>> getUsersInCabin() {
		return usersInCabin;
	}

	public void emptyCallsForFloorAndDirection(int currentFloor,
			Direction currentDirection) {
		if(Direction.NIL == currentDirection){
			usersCalling.get(currentFloor).clear();
		}else{
			Iterator<User> iterator = usersCalling.get(currentFloor).iterator();
			while (iterator.hasNext()) {
				User user = iterator.next();
				if(Direction.NIL == currentDirection || currentDirection == user.getCall().getDirection()){
					iterator.remove();
				}
			}
		}
	}
}
