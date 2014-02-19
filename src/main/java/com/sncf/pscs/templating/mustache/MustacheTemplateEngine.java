package com.sncf.pscs.templating.mustache;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.sncf.pscs.templating.TemplateEngine;

public class MustacheTemplateEngine implements TemplateEngine {

	public String compile(String template, Map<String, String> vals) {
		Mustache mustache = new DefaultMustacheFactory().compile(template);
	    StringWriter writer = new StringWriter();
		try {
			mustache.execute(writer, vals ).flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return writer.toString();
	}

}
