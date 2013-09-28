package com.bzn.codestory.elevator;

import static com.bzn.codestory.elevator.Command.CLOSE;
import static com.bzn.codestory.elevator.Command.DOWN;
import static com.bzn.codestory.elevator.Command.NOTHING;
import static com.bzn.codestory.elevator.Command.OPEN;
import static com.bzn.codestory.elevator.Command.UP;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ElevatorTest {

	private Elevator elevator;

	@Before
	public void setUp() {
		elevator = new Elevator();
	}

	@Test
	public void should_stay_idle_when_no_stimulus() throws Exception {
		checkNextCommands(NOTHING);
	}

	@Test
	public void should_open_doors_if_call_at_initial_floor() throws Exception {
		elevator.call(0, Direction.UP);
		checkNextCommands(OPEN);
	}

	@Test
	public void should_close_doors_if_opened() throws Exception {
		elevator.call(0, Direction.UP);
		checkNextCommands(OPEN, CLOSE);
	}

	@Test
	public void oneUser_calling_at_0_and_going_to_1() throws Exception {
		elevator.call(0, Direction.UP);
		checkNextCommands(OPEN);
		elevator.userEntered();
		assertThat(elevator.usersInCabin()).isEqualTo(1);
		elevator.goTo(1);
		checkNextCommands(CLOSE, UP, OPEN);
		elevator.userExited();
		assertThat(elevator.usersInCabin()).isEqualTo(0);
		checkNextCommands(CLOSE, NOTHING);
	}

	@Test
	public void oneUser_calling_at_0_and_going_to_2() throws Exception {
		elevator.call(0, Direction.UP);
		checkNextCommands(OPEN);
		elevator.userEntered();
		assertThat(elevator.usersInCabin()).isEqualTo(1);
		elevator.goTo(2);
		checkNextCommands(CLOSE, UP, UP, OPEN);
		elevator.userExited();
		assertThat(elevator.usersInCabin()).isEqualTo(0);
		checkNextCommands(CLOSE, NOTHING);
	}

	@Test
	public void oneUser_calling_at_1_and_going_to_0() throws Exception {
		elevator.call(1, Direction.DOWN);
		checkNextCommands(UP, OPEN);
		elevator.userEntered();
		assertThat(elevator.usersInCabin()).isEqualTo(1);
		elevator.goTo(0);
		checkNextCommands(CLOSE, DOWN, OPEN);
		elevator.userExited();
		assertThat(elevator.usersInCabin()).isEqualTo(0);
		checkNextCommands(CLOSE, NOTHING);
	}

	@Test
	public void oneUser_calling_at_2_with_elevator_at_three_and_going_to_five()
			throws Exception {
		elevator = new Elevator(3);
		elevator.call(2, Direction.UP);
		checkNextCommands(DOWN, OPEN);
		elevator.userEntered();
		assertThat(elevator.usersInCabin()).isEqualTo(1);
		elevator.goTo(5);
		checkNextCommands(CLOSE, UP, UP, UP, OPEN);
		elevator.userExited();
		assertThat(elevator.usersInCabin()).isEqualTo(0);
		checkNextCommands(CLOSE, NOTHING);
	}

	private void checkNextCommands(Command... commands) {
		for (Command expected : commands) {
			assertThat(elevator.nextCommand()).isEqualTo(expected);
		}
	}
}
