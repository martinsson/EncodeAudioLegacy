package com.sncf.pscs.templating;

import java.util.Map;

public interface TemplateEngine {

	String compile(String template, Map<String, String> vals);

}
