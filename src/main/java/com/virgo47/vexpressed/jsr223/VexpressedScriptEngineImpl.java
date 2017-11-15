package com.virgo47.vexpressed.jsr223;

import java.io.IOException;
import java.io.Reader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.virgo47.vexpressed.VexpressedUtils;

// TODO implements Compilable as well?
public class VexpressedScriptEngineImpl extends AbstractScriptEngine {

	private final VexpressedScriptEngineFactory factory;

	VexpressedScriptEngineImpl(VexpressedScriptEngineFactory factory) {
		this.factory = factory;
	}

	public VexpressedScriptEngineImpl() {
		factory = null;
	}

	@Override
	public Object eval(String script, ScriptContext context) throws ScriptException {
		return VexpressedUtils.eval(script, context::getAttribute);
	}

	@Override
	public Object eval(Reader reader, ScriptContext context) throws ScriptException {
		return eval(readerToString(reader), context);
	}

	private String readerToString(Reader reader) throws ScriptException {
		try {
			char[] arr = new char[8 * 1024];
			StringBuilder sb = new StringBuilder();
			int numCharsRead;
			while ((numCharsRead = reader.read(arr, 0, arr.length)) != -1) {
				sb.append(arr, 0, numCharsRead);
			}
			return sb.toString();
		} catch (IOException e) {
			throw new ScriptException(e);
		}
	}

	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	@Override
	public ScriptEngineFactory getFactory() {
		return factory;
	}
}
