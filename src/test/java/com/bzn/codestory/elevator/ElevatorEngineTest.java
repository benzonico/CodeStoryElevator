package com.bzn.codestory.elevator;

import static org.fest.assertions.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
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
				"userHasExited", "nextCommands" };
		String[] params = { "?cabin=0&floorToGo=1", "?atFloor=2&to=UP",
				"?lowerFloor=0&higherFloor=12&cabinSize=5&cause=toujours&cabinCount=1",
				"?cabin=0", "?cabin=0", "" };
		for (int i = 0; i < domains.length; i++) {
			HttpGet get = new HttpGet("http://localhost:" + PORT + "/"
					+ domains[i] + params[i]);
			HttpResponse response = httpclient.execute(get);
			// required to release the connection
			getResponseAsString(get, response.getEntity());
			int code = response.getStatusLine().getStatusCode();
			assertThat(code).as("Domain should return OK: "+domains[i]).isEqualTo(HttpStatus.SC_OK);
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
		String[] params = { "?lowerFloor=0&higherFloor=2&cabinSize=3&cause=toujours&cabinCount=1",
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

		HttpGet get = new HttpGet("http://localhost:" + PORT + "/status");
		HttpResponse response = httpclient.execute(get);
		// required to release the connection
		String statusJson = getResponseAsString(get, response.getEntity());
		int code = response.getStatusLine().getStatusCode();
		assertThat(code).isEqualTo(HttpStatus.SC_OK);
		assertThat(response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue())
				.isEqualTo(ContentType.APPLICATION_JSON.getMimeType());
		assertThat(statusJson).startsWith("{")
				.contains("\"frequencies\":{\"0\":0,\"1\":2,")
				.endsWith("}");
	}
}
