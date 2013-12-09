package com.bzn.codestory.elevator;

import static com.bzn.codestory.elevator.Command.CLOSE;
import static com.bzn.codestory.elevator.Command.DOWN;
import static com.bzn.codestory.elevator.Command.NOTHING;
import static com.bzn.codestory.elevator.Command.OPEN;
import static com.bzn.codestory.elevator.Command.OPEN_DOWN;
import static com.bzn.codestory.elevator.Command.OPEN_UP;
import static com.bzn.codestory.elevator.Command.UP;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ElevatorTest {

	private Elevator elevator;
	private static final Clock clock = new Clock();

	@Before
	public void setUp() {
		elevator = new Elevator();
	}

	@Test
	public void should_open_doors_if_call_at_initial_floor() throws Exception {
		elevator.call(0, Direction.UP);
		checkNextCommands(OPEN);
	}

	@Test
	public void should_close_doors_if_opened() throws Exception {
		int floor_0 = 0;
		elevator.call(floor_0, Direction.UP);
		checkNextCommands(OPEN);
		User lastUserAtFloor = getDummyUserCallingFrom(floor_0, Direction.UP);
		elevator.userEntered(lastUserAtFloor);
		checkNextCommands(CLOSE);
	}

	@Test
	public void should_do_nothing_at_balance_floor() throws Exception {
		assertThat(new Elevator(5, 5).nextCommand()).isEqualTo(NOTHING);
	}

	@Test
	public void should_drop_to_balance_floor() throws Exception {
		assertThat(new Elevator(6, 5).nextCommand()).isEqualTo(DOWN);
	}

	@Test
	public void should_climb_to_balance_floor() throws Exception {
		assertThat(new Elevator(4, 5).nextCommand()).isEqualTo(UP);
	}

	@Test
	public void oneUser_calling_at_0_and_going_to_1() throws Exception {
		elevator.call(0, Direction.UP);
		checkNextCommands(OPEN);
		int floor_0 = 0;
		User lastUserAtFloor = getDummyUserCallingFrom(floor_0, Direction.UP);
		elevator.userEntered(lastUserAtFloor);
		assertThat(elevator.usersInCabin()).isEqualTo(1);
		elevator.goTo(1);
		checkNextCommands(CLOSE, UP, OPEN);
		elevator.userExited();
		assertThat(elevator.usersInCabin()).isEqualTo(0);
		checkNextCommands(CLOSE);
	}

	@Test
	public void oneUser_calling_at_0_and_going_to_2() throws Exception {
		elevator.call(0, Direction.UP);
		checkNextCommands(OPEN);
		int floor_0 = 0;
		User lastUserAtFloor = getDummyUserCallingFrom(floor_0, Direction.UP);
		elevator.userEntered(lastUserAtFloor);
		assertThat(elevator.usersInCabin()).isEqualTo(1);
		elevator.goTo(2);
		checkNextCommands(CLOSE, UP, UP, OPEN);
		elevator.userExited();
		assertThat(elevator.usersInCabin()).isEqualTo(0);
		checkNextCommands(CLOSE);
	}

	@Test
	public void oneUser_calling_at_1_and_going_to_0() throws Exception {
		elevator.call(1, Direction.DOWN);
		checkNextCommands(UP, OPEN);
		int floor_1 = 1;
		User lastUserAtFloor = getDummyUserCallingFrom(floor_1, Direction.DOWN);
		elevator.userEntered(lastUserAtFloor);
		assertThat(elevator.usersInCabin()).isEqualTo(1);
		elevator.goTo(0);
		checkNextCommands(CLOSE, DOWN, OPEN);
		elevator.userExited();
		assertThat(elevator.usersInCabin()).isEqualTo(0);
		checkNextCommands(CLOSE);
	}

	private User getDummyUserCallingFrom(int floor, Direction direction) {
		Call call = new Call(floor, direction);
		return new User(call, clock);
	}

	@Test
	public void oneUser_calling_at_2_with_elevator_at_three_and_going_to_five()
			throws Exception {
		elevator = new Elevator(3);
		elevator.call(2, Direction.UP);
		checkNextCommands(DOWN, OPEN);
		int floor_2 = 2;
		User lastUserAtFloor = getDummyUserCallingFrom(floor_2, Direction.UP);
		elevator.userEntered(lastUserAtFloor);
		assertThat(elevator.usersInCabin()).isEqualTo(1);
		elevator.goTo(5);
		checkNextCommands(CLOSE, UP, UP, UP, OPEN);
		elevator.userExited();
		assertThat(elevator.usersInCabin()).isEqualTo(0);
		checkNextCommands(CLOSE);
	}

	@Test
	public void twoUsers_calling_at_0_and_going_to_1_and_2() throws Exception {
		elevator.call(0, Direction.UP);
		elevator.call(0, Direction.UP);
		checkNextCommands(OPEN);
		User lastUserAtFloor = getDummyUserCallingFrom(0, Direction.UP);
		elevator.userEntered(lastUserAtFloor);
		elevator.goTo(1);
		lastUserAtFloor = getDummyUserCallingFrom(0, Direction.UP);
		elevator.userEntered(lastUserAtFloor);
		elevator.goTo(2);
		assertThat(elevator.usersInCabin()).isEqualTo(2);
		checkNextCommands(CLOSE, UP, OPEN_UP);
		elevator.userExited();
		assertThat(elevator.usersInCabin()).isEqualTo(1);
		checkNextCommands(CLOSE, UP, OPEN);
		elevator.userExited();
		assertThat(elevator.usersInCabin()).isEqualTo(0);
		checkNextCommands(CLOSE);
	}

	@Test
	public void twoUsers_calling_at_0_and_1_and_going_to_2_and_3()
			throws Exception {
		elevator.call(0, Direction.UP);
		elevator.call(1, Direction.UP);
		checkNextCommands(OPEN);
		elevator.userEntered(getDummyUserCallingFrom(0, Direction.UP));
		elevator.goTo(2);
		checkNextCommands(CLOSE, UP, OPEN_UP);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.goTo(3);
		checkNextCommands(CLOSE, UP, OPEN_UP);
		elevator.userExited();
		checkNextCommands(CLOSE, UP, OPEN);
		elevator.userExited();
		checkNextCommands(CLOSE);
	}

	@Test
	public void oneUser_calling_at_0_and_one_user_calling_down_at_two()
			throws Exception {
		elevator.call(0, Direction.UP);
		elevator.call(2, Direction.DOWN);
		checkNextCommands(OPEN);
		elevator.userEntered(getDummyUserCallingFrom(0, Direction.UP));
		elevator.goTo(3);
		checkNextCommands(CLOSE, UP, UP, UP, OPEN);
		elevator.userExited();
		checkNextCommands(CLOSE, DOWN, OPEN_DOWN);
		elevator.userEntered(getDummyUserCallingFrom(2, Direction.DOWN));
		elevator.goTo(0);
		checkNextCommands(CLOSE, DOWN, DOWN, OPEN);
		elevator.userExited();
		checkNextCommands(CLOSE);
	}

	@Test
	public void oneUser_calling_at_1_and_one_user_calling_down_at_two()
			throws Exception {
		elevator.call(1, Direction.UP);
		elevator.call(2, Direction.DOWN);
		checkNextCommands(UP, OPEN_UP);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.goTo(3);
		checkNextCommands(CLOSE, UP, UP, OPEN);
		elevator.userExited();
		checkNextCommands(CLOSE, DOWN, OPEN_DOWN);
		elevator.userEntered(getDummyUserCallingFrom(2, Direction.DOWN));
		elevator.goTo(0);
		checkNextCommands(CLOSE, DOWN, DOWN, OPEN);
		elevator.userExited();
		checkNextCommands(CLOSE);
	}

	@Test
	public void should_go_6ft_under() throws Exception {
		elevator.reset(-6, 6, 6, clock);
		elevator.call(-6, Direction.UP);
		checkNextCommands(DOWN, DOWN, DOWN, DOWN, DOWN, DOWN, OPEN);
	}

	@Test
	public void should_open_for_safety_reasons() throws Exception {
		elevator.reset(0, 3, 2, clock);
		elevator.call(1, Direction.UP);
		elevator.call(1, Direction.UP);
		elevator.call(2, Direction.UP);
		checkNextCommands(UP, OPEN_UP);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.goTo(2);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.goTo(3);
		checkNextCommands(CLOSE, UP, OPEN_UP);
		elevator.userExited();
		elevator.userEntered(getDummyUserCallingFrom(2, Direction.UP));
		elevator.goTo(3);
		checkNextCommands(CLOSE, UP, OPEN);
	}

	@Test
	public void should_refuse_if_cabin_full() throws Exception {
		elevator.reset(0, 3, 2, clock);
		elevator.call(1, Direction.UP);
		elevator.call(1, Direction.UP);
		elevator.call(2, Direction.UP);
		checkNextCommands(UP, OPEN_UP);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.goTo(3);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.goTo(3);
		checkNextCommands(CLOSE, UP, UP, OPEN);
		elevator.userExited();
		elevator.userExited();
		checkNextCommands(CLOSE, DOWN, OPEN);
	}

	@Test
	public void should_skip_users_going_the_other_way() throws Exception {
		elevator.reset(0, 3, 2, clock);
		elevator.call(1, Direction.UP);
		elevator.call(2, Direction.DOWN);
		elevator.call(2, Direction.DOWN);
		elevator.call(3, Direction.DOWN);
		checkNextCommands(UP, OPEN_UP);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.goTo(2);
		checkNextCommands(CLOSE, UP, OPEN_UP);
		elevator.userExited();
		checkNextCommands(CLOSE, UP, OPEN);
		elevator.userEntered(getDummyUserCallingFrom(3, Direction.DOWN));
		elevator.goTo(0);
		checkNextCommands(CLOSE, DOWN, OPEN_DOWN);
		elevator.userEntered(getDummyUserCallingFrom(2, Direction.DOWN));
		elevator.userEntered(getDummyUserCallingFrom(2, Direction.DOWN));
		elevator.goTo(0);
		elevator.goTo(0);
		checkNextCommands(CLOSE, DOWN, DOWN, OPEN);
	}

	@Test
	public void should_not_close_doors_in_the_nose_of_users() {
		elevator.reset(0, 3, 5, clock);
		elevator.call(1, Direction.UP);
		checkNextCommands(UP, OPEN_UP);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.goTo(2);
		elevator.call(1, Direction.UP);
		checkNextCommands(NOTHING);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.goTo(2);
		checkNextCommands(CLOSE);
	}

	@Test
	public void should_change_direction_without_forgetting_users_at_floor() {
		elevator.reset(0, 3, 5, clock);
		elevator.call(1, Direction.DOWN);
		checkNextCommands(UP, OPEN);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.DOWN));
		elevator.goTo(0);
		elevator.call(1, Direction.DOWN);
		checkNextCommands(NOTHING);
	}

	@Test
	public void should_not_reopen_if_cabin_full() {
		elevator.reset(0, 3, 2, clock);
		elevator.call(1, Direction.UP);
		elevator.call(1, Direction.UP);
		checkNextCommands(UP, OPEN_UP);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.call(1, Direction.UP);
		checkNextCommands(CLOSE);
	}

	@Test
	public void should_detect_not_stop_to_take_users_if_cabin_full() {
		elevator.reset(0, 3, 2, clock);
		makeSeveralCalls(1, Direction.UP, 2);
		makeSeveralCalls(2, Direction.UP, 3);
		makeSeveralCalls(3, Direction.DOWN, 10);
		checkNextCommands(UP, OPEN_UP);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.goTo(3);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.goTo(3);
		checkNextCommands(CLOSE, UP, UP, OPEN);
	}

	@Test
	public void should_not_take_user_to_bottom_if_he_wants_to_go_up() {
		elevator.reset(0, 5, 10, clock);
		elevator.call(4, Direction.UP);
		checkNextCommands(UP, UP, UP, UP);
		elevator.call(1, Direction.DOWN);
		elevator.call(2, Direction.UP);
		checkNextCommands(OPEN_UP);
		elevator.userEntered(getDummyUserCallingFrom(4, Direction.UP));
		elevator.goTo(5);
		checkNextCommands(CLOSE, UP);
	}

	@Test
	public void elevator_should_not_stay_at_one_floor_forever() {
		elevator.reset(0, 5, 10, clock);
		elevator.call(0, Direction.UP);
		checkNextCommands(OPEN);
		elevator.userEntered(getDummyUserCallingFrom(0, Direction.UP));
		elevator.goTo(4);
		elevator.call(3, Direction.UP);
		elevator.call(3, Direction.DOWN);
		elevator.call(3, Direction.UP);
		checkNextCommands(CLOSE, UP, UP, UP, OPEN_UP);
		elevator.userEntered(getDummyUserCallingFrom(3, Direction.UP));
		elevator.goTo(5);
		elevator.userEntered(getDummyUserCallingFrom(3, Direction.UP));
		elevator.goTo(5);
		checkNextCommands(CLOSE, UP);
	}

	@Test
	public void calculateDistanceInSameDirection() throws Exception {
		elevator.reset(0, 5, 5, clock);
		elevator.call(1, Direction.UP);
		checkNextCommands(UP);
		Floor floorToGo = new Floor(2);
		assertThat(elevator.getDistanceToFloor(floorToGo, Direction.UP)).isEqualTo(1);
	}

	@Test
	public void calculateDistanceAtSameFloor() throws Exception {
		elevator.reset(0, 5, 5, clock);
		Floor floorToGo = new Floor(0);
		assertThat(elevator.getDistanceToFloor(floorToGo, Direction.UP)).isEqualTo(0);
	}

	@Test
	public void calculateDistanceInSameDirectionWithUsersInCabin() throws Exception {
		elevator.reset(0, 5, 5, clock);
		elevator.call(1, Direction.UP);
		checkNextCommands(UP, OPEN_UP);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.goTo(3);
		Floor floorToGo = new Floor(2);
		assertThat(elevator.getDistanceToFloor(floorToGo, Direction.UP)).isEqualTo(1.5);
	}

	@Test
	public void calculateDistanceInOtherDirection() throws Exception {
		elevator.reset(0, 5, 5, clock);
		elevator.call(1, Direction.UP);
		checkNextCommands(UP);
		Floor floorToGo = new Floor(2);
		assertThat(elevator.getDistanceToFloor(floorToGo, Direction.DOWN)).isEqualTo(7);
	}

	@Test
	public void calculateDistanceInOtherDirectionDown() throws Exception {
		elevator.reset(0, 5, 5, clock);
		elevator.call(5, Direction.DOWN);
		checkNextCommands(UP, UP, UP, UP, UP, OPEN);
		elevator.userEntered(getDummyUserCallingFrom(1, Direction.UP));
		elevator.goTo(3);
		checkNextCommands(CLOSE, DOWN);
		Floor floorToGo = new Floor(2);
		assertThat(elevator.getDistanceToFloor(floorToGo, Direction.UP)).isEqualTo(6.5);
	}

	@Test
	public void calculateDistanceWhenNoDirection() throws Exception {
		elevator.reset(0, 5, 5, clock);
		Floor floorToGo = new Floor(2);
		assertThat(elevator.getDistanceToFloor(floorToGo, Direction.UP)).isEqualTo(2);
		assertThat(elevator.getDistanceToFloor(floorToGo, Direction.DOWN)).isEqualTo(2);
	}


	private void checkNextCommandSomeTimes(Command command, int times) {
		for (int i = 0; i < times; i++) {
			checkNextCommands(command);
		}
	}

	private void checkNextCommands(Command... commands) {
		for (Command expected : commands) {
			assertThat(elevator.nextCommand()).isEqualTo(expected);
		}
	}

	private void makeSeveralCalls(int floor, Direction direction, int times) {
		for (int i = 0; i < times; i++) {
			elevator.call(floor, direction);
		}
	}
}
