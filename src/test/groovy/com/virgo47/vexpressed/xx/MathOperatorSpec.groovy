package com.virgo47.vexpressed.xx

import com.virgo47.vexpressed.core.ExpressionException
import spock.lang.Specification

import static com.virgo47.vexpressed.VexpressedUtils.eval

class MathOperatorSpec extends Specification {

	def "Unary minus negates the value of integer."() {
		expect:
		eval("-5") == -5
	}

	def "Unary minus negates the value of decimal."() {
		expect:
		eval("-5.1") == -5.1
	}

	def "Double unary minus must be separated by parenthesis."() {
		expect:
		eval("-(-5.1)") == 5.1
	}

	def "Double unary minus can be separated with space as well."() {
		expect:
		eval("-(-5.1)") == 5.1
	}

	def "Double minus collides with custom operators and throwns exception."() {
		when:
		eval("--5.1")

		then:
		ExpressionException ex = thrown()
		ex.message =~ /(?s)Expression parse failed at 1:0.*'--'.*/
	}
}
