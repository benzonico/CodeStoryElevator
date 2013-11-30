package com.bzn.codestory.elevator;

import static spark.Spark.get;
import static spark.Spark.setPort;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ElevatorEngine {

	private static Elevator[] elevators = new Elevator[1];
	private static Logger logger = LoggerFactory
			.getLogger(ElevatorEngine.class);

	static {
		elevators[0] = new Elevator();
	}

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
				Elevator picked = new RouteCall().route(elevators, floor,
						direction);
				picked.call(floor, direction);
				resp.status(200);
				return resp;
			}
		});
		get(new Route("go") {
			@Override
			public Object handle(Request req, Response resp) {
				int cabin = Integer.valueOf(req.queryParams("cabin"));
				elevators[cabin].goTo(Integer.valueOf(req
						.queryParams("floorToGo")));
				resp.status(200);
				return resp;
			}
		});
		get(new Route("userHasEntered") {
			@Override
			public Object handle(Request req, Response resp) {
				int cabin = Integer.valueOf(req.queryParams("cabin"));
				elevators[cabin].userEntered();
				resp.status(200);
				return resp;
			}
		});
		get(new Route("userHasExited") {
			@Override
			public Object handle(Request req, Response resp) {
				int cabin = Integer.valueOf(req.queryParams("cabin"));
				elevators[cabin].userExited();
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
				int cabinCount = Integer.valueOf(req.queryParams("cabinCount"));

				logger.info("RESET : cause : " + req.queryParams("cause"));

				elevators = new Elevator[cabinCount];
				for (int cabin = 0; cabin < cabinCount; cabin++) {
					elevators[cabin] = new Elevator();
					elevators[cabin].reset(lower, higher, cabinSize);
				}
				resp.status(200);
				return resp;
			}
		});
		get(new Route("nextCommands") {
			@Override
			public Object handle(Request req, Response resp) {
				resp.status(200);
				String[] commands = new String[elevators.length];
				for (int cabin = 0; cabin < elevators.length; cabin++) {
					commands[cabin] = elevators[cabin].nextCommand().toString();
				}
				String returnedCommands = StringUtils.join(commands, "\n");
				logger.info("nextCommands : " + Arrays.toString(commands));
				return returnedCommands;
			}
		});
		get(new Route("status") {
			@Override
			public Object handle(Request req, Response resp) {
				resp.type("application/json");
				try {
					ObjectMapper mapper = new ObjectMapper();
					return mapper.writeValueAsString(elevators[0].getStatus());
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return "";
				}
			}
		});
	}
}
