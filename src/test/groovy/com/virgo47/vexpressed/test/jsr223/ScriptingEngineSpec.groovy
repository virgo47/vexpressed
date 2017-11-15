package com.virgo47.vexpressed.test.jsr223

import com.virgo47.vexpressed.VexpressedUtils
import com.virgo47.vexpressed.jsr223.VexpressedScriptEngineFactory
import com.virgo47.vexpressed.jsr223.VexpressedScriptEngineImpl
import spock.lang.Specification

import javax.script.SimpleBindings

class ScriptingEngineSpec extends Specification {

	def "Script Engine created without factory has null factory."() {
		when:
		def engine = new VexpressedScriptEngineImpl()

		then:
		engine.getFactory() == null
	}

	def "Script Engine created with factory returns that factory from getFactory()."() {
		when:
		def factory = new VexpressedScriptEngineFactory()
		def engine = factory.getScriptEngine()

		then:
		engine.getFactory() == factory
	}

	def "Engine eval() evaluates simple expressions just like VexpressedUtils.eval()"() {
		given:
		def factory = new VexpressedScriptEngineFactory()
		def engine = factory.getScriptEngine()

		expect:
		engine.eval(expr) == VexpressedUtils.eval(expr)

		where:
		expr << ["2", "2 + 1"]
	}

	def "Engine eval() evaluates expressions with variables using Binding"() {
		given:
		def factory = new VexpressedScriptEngineFactory()
		def engine = factory.getScriptEngine()

		when:
		def bindings = new SimpleBindings()
		bindings.put("a", 1)
		bindings.put("b", 2)

		then:
		engine.eval("a + b", bindings) == 3
	}
}
