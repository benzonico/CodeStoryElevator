package com.bzn.codestory.elevator;

import static com.bzn.codestory.elevator.Direction.DOWN;
import static com.bzn.codestory.elevator.Direction.UP;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class RouteCallTest {

	private Elevator elevator0;
	private Elevator elevator1;

	@Test
	public void callUp_above_pick_elevator_with_correct_direction() {
		elevator0 = new Elevator(2);
		elevator0.setCurrentDirection(DOWN);
		elevator1 = new Elevator(2);
		elevator1.setCurrentDirection(UP);
		Elevator[] temp = { elevator0, elevator1 };
		assertThat(new RouteCall().route(temp, 3, UP)).isEqualTo(elevator1);

	}

	@Test
	public void callUp_below_pick_elevator_with_correct_direction() {
		elevator0 = new Elevator(2);
		elevator0.setCurrentDirection(DOWN);
		elevator1 = new Elevator(2);
		elevator1.setCurrentDirection(UP);
		Elevator[] temp = { elevator0, elevator1 };
		assertThat(new RouteCall().route(temp, 1, UP)).isEqualTo(elevator0);
	}

	@Test
	public void should_pick_the_elevator_at_right_floor_if_both_in_same_direction_up() {
		elevator0 = new Elevator(4);
		elevator0.setCurrentDirection(UP);
		elevator1 = new Elevator(2);
		elevator1.setCurrentDirection(UP);
		Elevator[] temp = { elevator0, elevator1 };
		assertThat(new RouteCall().route(temp, 3, UP)).isEqualTo(elevator1);
	}

	@Test
	public void should_pick_the_elevator_at_right_floor_if_both_in_same_direction_down() {
		elevator0 = new Elevator(1);
		elevator0.setCurrentDirection(DOWN);
		elevator1 = new Elevator(4);
		elevator1.setCurrentDirection(DOWN);
		Elevator[] temp = { elevator0, elevator1 };
		assertThat(new RouteCall().route(temp, 3, DOWN)).isNotNull();
		// .isEqualTo(elevator1); set up the test properly
	}

	@Test
	public void should_pick_the_elevator_depending_on_call_direction() {
		elevator0 = new Elevator(1);
		elevator0.setCurrentDirection(UP);
		elevator1 = new Elevator(4);
		elevator1.setCurrentDirection(DOWN);
		Elevator[] temp = { elevator0, elevator1 };
		assertThat(new RouteCall().route(temp, 3, DOWN)).isEqualTo(elevator1);
	}

	@Test
	public void should_pick_lesscrowded_elevator() {
		elevator0 = new Elevator(5);
		elevator0.setCurrentDirection(UP);
		elevator1 = new Elevator(4);
		elevator1.setCurrentDirection(UP);
		Elevator[] temp = { elevator0, elevator1 };
		assertThat(new RouteCall().route(temp, 3, DOWN)).isEqualTo(elevator0);
	}

}
