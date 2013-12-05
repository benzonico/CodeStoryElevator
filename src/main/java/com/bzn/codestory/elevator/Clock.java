package com.bzn.codestory.elevator;

import com.google.common.annotations.VisibleForTesting;

public class Clock {
	private int time;

	public int getTime() {
		return time;
	}

	public Clock() {
		this.time = 0;
	}
	
	@VisibleForTesting
	Clock(int time) {
		this.time = time;
	}

	public int getWaitingTime(Integer timeArrived) {
		return time - timeArrived;
	}

	public void tic() {
		this.time++;
	}

}
