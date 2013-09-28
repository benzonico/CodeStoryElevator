package com.bzn.codestory.elevator;

import static spark.Spark.get;
import static spark.Spark.setPort;
import spark.Request;
import spark.Response;
import spark.Route;

public class ElevatorEngine {
	
	
	
	
	public static void main(String[] args) {
		initServices(8000);
	}
	
	public static void initServices(int port){
		setPort(port);
		get(new Route("call") {
			@Override
			public Object handle(Request req, Response resp) {
				System.out.println("call event at floor "+req.params("atFloor")+" to "+req.params("to"));
				resp.status(200);
				return resp;
			}
		});
		get(new Route("go") {
			@Override
			public Object handle(Request req, Response resp) {
				System.out.println("go event ");
				resp.status(200);
				return resp;
			}
		});
		get(new Route("userHasEntered") {
			@Override
			public Object handle(Request req, Response resp) {
				System.out.println("userEntered event");
				resp.status(200);
				return resp;
			}
		});
		get(new Route("userHasExited") {
			@Override
			public Object handle(Request req, Response resp) {
				System.out.println("userExited event");
				resp.status(200);
				return resp;
			}
		});
		get(new Route("reset") {
			@Override
			public Object handle(Request req, Response resp) {
				System.out.println("reset event");
				resp.status(200);
				return resp;
			}
		});
		get(new Route("nextCommand") {
			
			int i = 1;
			String[] command = {"OPEN","CLOSE","UP",
					"OPEN","CLOSE","UP",
					"OPEN","CLOSE","UP",
					"OPEN","CLOSE","UP",
					"OPEN","CLOSE","UP",
					"OPEN","CLOSE","DOWN",
					"OPEN","CLOSE","DOWN",
					"OPEN","CLOSE","DOWN",
					"OPEN","CLOSE","DOWN",
					"OPEN","CLOSE","DOWN",
					}; 
			@Override
			public Object handle(Request req, Response resp) {
				i++;
				System.out.println("nextCommand event");
				resp.status(200);
				return command[i%command.length];
			}
		});
	}
	

}
