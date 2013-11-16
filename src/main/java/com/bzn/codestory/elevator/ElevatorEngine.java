package com.bzn.codestory.elevator;

import static spark.Spark.get;
import static spark.Spark.setPort;
import spark.Request;
import spark.Response;
import spark.Route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
				int lower = Integer.valueOf(req.queryParams("lowerFloor"));
				int higher = Integer.valueOf(req.queryParams("higherFloor"));
				int cabinSize = Integer.valueOf(req.queryParams("cabinSize"));
				
				System.out.println("RESET : cause : "
						+ req.queryParams("cause"));

				elevator.reset(lower, higher, cabinSize);
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
		get(new Route("status") {
			@Override
			public Object handle(Request req, Response resp) {
				resp.type("application/json");
				try {
					ObjectMapper mapper = new ObjectMapper();
					return mapper.writeValueAsString(elevator.getStatus());
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return "";
				}
			}
		});
	}

}
