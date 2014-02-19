package com.sncf.pscs.templating.mustache;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.sncf.pscs.templating.TemplateEngine;


public class MustacheTemplateEngineTest {

	TemplateEngine engine = new MustacheTemplateEngine();

	@Test
	public void does_key_value_based_replacement() throws Exception {
		@SuppressWarnings("serial")
		Map<String, String> vals = new HashMap<String, String>() {{
			put("firstname", "Johan");
			put("lastname", "Martinsson");
		}};
		
		String greeting = engine.compile("mustache/greeting.txt", vals );
		
		assertEquals("Hi Johan Martinsson", greeting);
	}
	
}
