package com.modzy.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.modzy.sdk.dto.TagWrapper;
import com.modzy.sdk.exception.ApiException;
import com.modzy.sdk.filter.LoggingFilter;
import com.modzy.sdk.model.Model;
import com.modzy.sdk.model.Tag;

import io.github.cdimascio.dotenv.Dotenv;

public class TestTagClient {
	
	private TagClient tagClient;
	
	@Before
	public void setUp() throws Exception {		
		Client client = ClientBuilder.newClient().register(LoggingFilter.class);
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		WebTarget baseTarget = client.target(dotenv.get("MODZY_BASE_URL"));			
		this.tagClient = new TagClient(baseTarget, dotenv.get("MODZY_API_KEY"));
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testGetAllTags() {
		List<Tag> tags = null;
		try {			
			tags = this.tagClient.getAllTags();			
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(tags);
		assertNotEquals(tags.size(), 0);
		for( Tag tag : tags ) {			
			assertNotNull(tag.getIdentifier());
			assertNotNull(tag.getName());
			assertNotNull(tag.getDataType());
		}
	}

	@Test
	public void testGetTagsAndModels() {
		TagWrapper tagWrapper = null;
		try {			
			tagWrapper = this.tagClient.getTagsAndModels("computer_vision");			
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(tagWrapper);
		assertNotNull(tagWrapper.getTags());
		assertNotEquals(tagWrapper.getTags().size(), 0);
		for( Tag tag : tagWrapper.getTags() ) {			
			assertNotNull(tag.getIdentifier());
			assertNotNull(tag.getName());
			assertNotNull(tag.getDataType());			
		}
		assertNotNull(tagWrapper.getModels());
		assertNotEquals(tagWrapper.getModels().size(), 0);
		for( Model model : tagWrapper.getModels() ) {
			assertNotNull(model.getIdentifier());
			assertNotNull(model.getName());
			assertNotNull(model.getTags());			
		}
	}
	
	@Test
	public void testGetModelsByInvalidTagId() {		
		TagWrapper tagWrapper = null;
		try {			
			tagWrapper = this.tagClient.getTagsAndModels("computer-vision");			
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(tagWrapper);
		assertNotNull(tagWrapper.getTags());
		assertEquals(0, tagWrapper.getTags().size());
		assertNotNull(tagWrapper.getModels());
		assertEquals(0, tagWrapper.getModels().size());		
	}
	
}
