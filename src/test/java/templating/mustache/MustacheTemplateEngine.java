package templating.mustache;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import templating.TemplateEngine;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;

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
