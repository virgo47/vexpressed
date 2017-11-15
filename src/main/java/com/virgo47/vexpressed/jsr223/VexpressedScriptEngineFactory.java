package com.virgo47.vexpressed.jsr223;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public class VexpressedScriptEngineFactory implements ScriptEngineFactory {

	private static final String VERSION = "1.0";
	private static final String SHORT_NAME = "vexpressed";
	private static final String LANGUAGE_NAME = "Vexpressed";

	private static final List<String> EXTENSIONS = Arrays.asList("vexp", "vxp");
	private static final List<String> NAMES = Arrays.asList(SHORT_NAME, LANGUAGE_NAME);
	private static final List<String> MIME_TYPES =
		Collections.singletonList("application/x-vexpressed");

	@Override
	public String getEngineName() {
		return "Vexpressed Scripting Engine";
	}

	@Override
	public String getEngineVersion() {
		return VERSION;
	}

	/**
	 * This is also different than scripting.dev.java.net which used an
	 * initial lowercase.  But these are proper names and should be capitalized.
	 */
	@Override
	public String getLanguageName() {
		return LANGUAGE_NAME;
	}

	public String getLanguageVersion() {
		return VERSION;
	}

	public List<String> getExtensions() {
		return EXTENSIONS;
	}

	public List<String> getMimeTypes() {
		return MIME_TYPES;
	}

	public List<String> getNames() {
		return NAMES;
	}

	@Override
	public Object getParameter(String key) {
		if (ScriptEngine.NAME.equals(key)) {
			return SHORT_NAME;
		} else if (ScriptEngine.ENGINE.equals(key)) {
			return getEngineName();
		} else if (ScriptEngine.ENGINE_VERSION.equals(key)) {
			return VERSION;
		} else if (ScriptEngine.LANGUAGE.equals(key)) {
			return LANGUAGE_NAME;
		} else if (ScriptEngine.LANGUAGE_VERSION.equals(key)) {
			return VERSION;
		} else {
			throw new IllegalArgumentException("Invalid key");
		}
	}

	@Override
	public String getMethodCallSyntax(String obj, String m, String... args) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getOutputStatement(String toDisplay) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getProgram(String... statements) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return new VexpressedScriptEngineImpl(this);
	}
}
