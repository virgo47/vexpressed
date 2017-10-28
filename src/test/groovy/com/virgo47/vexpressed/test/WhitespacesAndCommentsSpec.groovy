package com.virgo47.vexpressed.test

import spock.lang.Specification

import static com.virgo47.vexpressed.VexpressedUtils.eval

class WhitespacesAndCommentsSpec extends Specification {

	def "Whitespaces are generally ignored. Multi-line expression are allowed."() {
		expect:
		eval("1") == 1
		eval(" 1\t\n+1 ") == 2
	}

	def "Whitespaces can be used to separate double unary operator."() {
		expect:
		eval("- -1") == 1
	}

	def "Whitespaces in strings are preserved."() {
		expect:
		eval(" ' a b\tc\nd ' ") == " a b\tc\nd "
	}

	def "Block comment ignores everything between /* and */ (whitespace recommended for clarification)."() {
		expect:
		eval("1+2+3") == 6 // no comment
		eval("1 /*+2*/+3") == 4
		eval("1\t/*+2*/+3") == 4
		eval("1\r\n/*+2*/+3") == 4
		eval("1\r/*+2*/+3") == 4
		eval("/*1+2+*/3") == 3 // at the start of expression
	}

	def "Block comment can be used over multiple lines."() {
		expect:
		eval("1+ \r\n2+3") == 6 // no comment
		eval("1+ /*\r\n2+*/3") == 4
	}

	def "Line comment starts with # after a whitespace and ignores the rest of the line."() {
		expect:
		eval("1 #*2+3") == 1
		eval("1 #*2\n+3") == 4
		eval("1\r\n#*2\n+3") == 4
		eval("#1+\r\n\n3") == 3 // at the start of expression
	}
}
