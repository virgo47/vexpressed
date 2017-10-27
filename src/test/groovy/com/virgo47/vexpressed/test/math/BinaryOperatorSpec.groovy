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

	def "Integer plus decimal returns decimal even when whole number."() {
		expect:
		eval("1.1 + 2.9") == 4.0
	}

	// TODO minus, *, /, priority between these
}
