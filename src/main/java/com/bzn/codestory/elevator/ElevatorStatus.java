package com.bzn.codestory.elevator;

import java.io.IOException;
import java.util.Properties;
import java.util.SortedMap;

class ElevatorStatus {

	public int currentFloor;
	public int usersInCabin;
	public int cabinSize;
	public boolean open;
	public Direction currentDirection;
	public SortedMap<Integer, Integer> frequencies;
	public String gitBranch;
	public String gitCommit;
	public String gitDate;

	ElevatorStatus(int currentFloor, int usersInCabin, int cabinSize, boolean open, Direction currentDirection, SortedMap<Integer, Integer> frequencies) {
		this.currentFloor = currentFloor;
		this.usersInCabin = usersInCabin;
		this.cabinSize = cabinSize;
		this.open = open;
		this.currentDirection = currentDirection;
		this.frequencies = frequencies;
		Properties versionProperties = new Properties();
		try {
			versionProperties.load(getClass().getResourceAsStream("/version.properties"));
			gitBranch = versionProperties.getProperty("git.branch");
			gitCommit = versionProperties.getProperty("git.commit.id");
			gitDate = versionProperties.getProperty("git.commit.time");
		} catch (IOException e) {
			gitBranch = gitCommit = gitDate = "unknown";
		}
	}

}
