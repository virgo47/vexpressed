package com.virgo47.vexpressed.test.math

import spock.lang.Specification

import static com.virgo47.vexpressed.VexpressedUtils.eval

class BinaryOperatorSpec extends Specification {

	def "Plus adds two integer values."() {
		expect:
		eval("1 + 2") == 3
	}

	def "Plus adds two decimal values."() {
		expect:
		eval("1.1 + 2.3") == 3.4
	}

	def "Integer plus decimal returns decimal."() {
		expect:
		eval("1 + 2.3") == 3.3
		eval("1.1 + 2") == 3.1
	}

	def "Decimal plus decimal returns decimal even when whole number."() {
		expect:
		eval("1.1 + 2.9") == 4.0
	}

	def "Minus subtracts two integer values."() {
		expect:
		eval("1 - 2") == -1
		eval("2 - 1") == 1
	}

	def "Minus subtracts two decimal values."() {
		expect:
		eval("1.1 - 2.3") == -1.2
		eval("2.3 - 1.1") == 1.2
	}

	def "Subtraction of integer and decimal returns decimal."() {
		expect:
		eval("1 - 2.3") == -1.3
		eval("1.1 - 1") == 0.1
	}

	def "Subtraction of decimals returns decimal even when whole number."() {
		expect:
		eval("1.1 - 0.1") == 1.0
	}

	def "Multiplication of two integer values returns integer."() {
		expect:
		eval("3 * 4") == 12
		eval("3 * -2") == -6
	}

	def "Multiplication of integer and decimal returns decimal."() {
		expect:
		eval("1 * 2.3") == 2.3
		eval("1.1 * -1") == -1.1
	}

	def "Multiplication of two decimal values returns decimal."() {
		expect:
		eval("1.1 * 2.3") == 2.53
		eval("2.0 * -3.0") == -6.0
	}

	def "Division (non-integer) always returns decimal."() {
		expect:
		eval("3 / 4") == 0.75
		eval("3 / -1.5") == -2.0
		eval("3.0 / -1.0") == -3.0
		eval("3.0 / -1") == -3.0
		eval("1.44 / 1.2") == 1.2
	}

	// TODO priority between these
}
