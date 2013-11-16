package com.bzn.codestory.elevator;

import static org.fest.assertions.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.BeforeClass;
import org.junit.Test;

public class ElevatorEngineTest {

	private static final HttpClient httpclient = new DefaultHttpClient();
	private static int PORT = 8000;

	@BeforeClass
	public static void setup() {
		ElevatorEngine.initServices(PORT);
	}

	@Test
	public void initServices_should_listen_on_specified_port()
			throws ClientProtocolException, IOException {
		String[] domains = { "go", "call", "reset", "userHasEntered",
				"userHasExited", "nextCommand" };
		String[] params = { "?floorToGo=1", "?atFloor=2&to=UP",
				"?lowerFloor=0&higherFloor=12&cabinSize=5&cause=toujours", "", "", "" };
		for (int i = 0; i < domains.length; i++) {
			HttpGet get = new HttpGet("http://localhost:" + PORT + "/"
					+ domains[i] + params[i]);
			HttpResponse response = httpclient.execute(get);
			// required to release the connection
			getResponseAsString(get, response.getEntity());
			int code = response.getStatusLine().getStatusCode();
			assertThat(code).isEqualTo(HttpStatus.SC_OK);
		}
	}

	private String getResponseAsString(HttpGet httpget, HttpEntity entity)
			throws IOException {
		InputStream instream = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				instream));
		String result = reader.readLine();
		instream.close();
		return result;
	}

	@Test
	public void frequencies_should_be_displayed_after_reset_and_calls()
			throws ClientProtocolException, IOException {
		String[] domains = { "reset", "call", "call" };
		String[] params = { "?lowerFloor=0&higherFloor=2&cabinSize=3&cause=toujours",
				"?atFloor=1&to=UP", "?atFloor=1&to=UP" };
		for (int i = 0; i < domains.length; i++) {
			HttpGet get = new HttpGet("http://localhost:" + PORT + "/"
					+ domains[i] + params[i]);
			HttpResponse response = httpclient.execute(get);
			// required to release the connection
			getResponseAsString(get, response.getEntity());
			int code = response.getStatusLine().getStatusCode();
			assertThat(code).isEqualTo(HttpStatus.SC_OK);
		}

		HttpGet get = new HttpGet("http://localhost:" + PORT + "/frequencies");
		HttpResponse response = httpclient.execute(get);
		// required to release the connection
		String freqHTML = getResponseAsString(get, response.getEntity());
		int code = response.getStatusLine().getStatusCode();
		assertThat(code).isEqualTo(HttpStatus.SC_OK);
		assertThat(freqHTML).isEqualTo(
				"<table>" + "<tr><td>2</td><td>0</td></tr>"
						+ "<tr><td>1</td><td>2</td></tr>"
						+ "<tr><td>0</td><td>0</td></tr></table>");

	}

}
