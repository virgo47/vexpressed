package com.virgo47.vexpressed.test.math

import com.virgo47.vexpressed.core.ExpressionException
import spock.lang.Specification

import static com.virgo47.vexpressed.VexpressedUtils.eval

class UnaryOperatorSpec extends Specification {

	def "Unary minus negates the value of integer."() {
		expect:
		eval("-5") == -5
	}

	def "Unary minus negates the value of decimal."() {
		expect:
		eval("-5.1") == -5.1
	}

	def "Unary plus does not change the value of integer."() {
		expect:
		eval("+5") == 5
	}

	def "Unary plus does not change the value of decimal."() {
		expect:
		eval("+5.1") == 5.1
	}

	def "Double unary operator must be separated by parenthesis or whitespace."() {
		expect:
		eval(exp) == result

		where:
		exp       | result
		"-(-5.1)" | 5.1
		"-(+5.1)" | -5.1
		"+(+5.1)" | 5.1
		"+(-5.1)" | -5.1
		"- -5.1"  | 5.1
		"- +5.1"  | -5.1
		"+ +5.1"  | 5.1
		"+ -5.1"  | -5.1
	}

	def "Unseparated double unary operator collides with custom operators and throws exception."() {
		when:
		eval(exp)

		then:
		ExpressionException ex = thrown()
		ex.message =~ /(?s)Expression parse failed at 1:0.*'\Q${exp.substring(0, 2)}\E'.*/

		where:
		exp << ["--5.1", "+-5.1", "-+5.1", "++5.1"]
	}

	def "Unary operators do not work on non-numeric types"() {
		when:
		eval(exp)

		then:
		ExpressionException ex = thrown()
		ex.message.startsWith "Unary sign can be applied only to numbers, not to: "

		where:
		exp << ["-'string'", "+'string'", "-true", "+true"]
	}
}
