package com.bzn.codestory.elevator;

import static spark.Spark.get;
import static spark.Spark.setPort;
import spark.Request;
import spark.Response;
import spark.Route;

public class ElevatorEngine {

	private final static Elevator elevator = new Elevator();

	public static void main(String[] args) {
		ElevatorEngine.initServices(Integer.parseInt(System
				.getProperty("app.port")));
	}

	public static void initServices(int port) {
		setPort(port);
		get(new Route("call") {
			@Override
			public Object handle(Request req, Response resp) {
				int floor = Integer.valueOf(req.queryParams("atFloor"));
				Direction direction = Direction.valueOf(req.queryParams("to"));
				elevator.call(floor, direction);
				resp.status(200);
				return resp;
			}
		});
		get(new Route("go") {
			@Override
			public Object handle(Request req, Response resp) {
				elevator.goTo(Integer.valueOf(req.queryParams("floorToGo")));
				resp.status(200);
				return resp;
			}
		});
		get(new Route("userHasEntered") {
			@Override
			public Object handle(Request req, Response resp) {
				elevator.userEntered();
				resp.status(200);
				return resp;
			}
		});
		get(new Route("userHasExited") {
			@Override
			public Object handle(Request req, Response resp) {
				elevator.userExited();
				resp.status(200);
				return resp;
			}
		});
		get(new Route("reset") {
			@Override
			public Object handle(Request req, Response resp) {
				elevator.reset();
				resp.status(200);
				return resp;
			}
		});
		get(new Route("nextCommand") {

			@Override
			public Object handle(Request req, Response resp) {
				resp.status(200);
				return elevator.nextCommand();
			}
		});
	}

}
