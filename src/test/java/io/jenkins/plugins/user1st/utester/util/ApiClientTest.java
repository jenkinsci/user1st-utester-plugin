package io.jenkins.plugins.user1st.utester.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ApiClientTest {

	private String apiUrl;
	private String apiToken;

	@Before
	public void setUp() throws Exception {
		apiUrl = "";
		apiToken = "";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testApiClient() {
		ApiClient apiClient = new ApiClient(apiUrl, apiToken);
		assertNotNull(apiClient);
	}

	@Test
	public final void testPost() {

	}

	@Test
	public final void testGetTaskStatus() {

	}

	@Test
	public final void testGetTaskStatusString() {

	}

	@Test
	public final void testGetTaskResults() {

	}

	@Test
	public final void testGetTaskResultsString() {

	}

}
