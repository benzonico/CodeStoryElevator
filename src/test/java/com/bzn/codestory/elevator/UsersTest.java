package com.bzn.codestory.elevator;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class UsersTest {

	@Test
	public void test_call_enter_goTo_behaviour() {
		int floor_0 = 0;
		Users users = new Users(0, 5);
		Call call = new Call(floor_0, Direction.UP);
		int olderTimeUp = 1;
		users.userCalls(call, olderTimeUp);
		users.userCalls(new Call(floor_0, Direction.DOWN), 0);
		users.userCalls(call, 10);
		users.userCalls(call, 15);
		assertThat(users.getUsersCalling().get(floor_0)).isNotEmpty().hasSize(4);
		users.enter(floor_0, Direction.UP, 25);
		int floor_2 = 2;
		users.userWantsToGoTo(new GoTo(floor_2, Direction.UP), 0);
		User excluded = new User(call, olderTimeUp);
		assertThat(users.getUsersCalling().get(floor_0)).isNotEmpty().hasSize(3).excludes(excluded);
		assertThat(users.getUsersInCabin().get(floor_2)).isNotEmpty();
	}

}
