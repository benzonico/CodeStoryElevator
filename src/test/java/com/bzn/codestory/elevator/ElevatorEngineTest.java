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
import org.junit.Test;

public class ElevatorEngineTest {

	private static final HttpClient httpclient = new DefaultHttpClient();

	@Test
	public void initServices_should_listen_on_specified_port()
			throws ClientProtocolException, IOException {
		int port = 8000;
		ElevatorEngine.initServices(port);
		String[] domains = { "go", "call", "reset", "userHasEntered",
				"userHasExited", "nextCommand" };
		for (int i = 0; i < domains.length; i++) {
			HttpGet get = new HttpGet("http://localhost:" + port + "/"
					+ domains[i]);
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

}
