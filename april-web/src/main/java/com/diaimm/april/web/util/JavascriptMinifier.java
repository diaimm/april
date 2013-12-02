/**
 * @fileName : JavascriptMinifier.java
 * @date : 2013. 5. 22.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

/**
 * JavaScript Minifier
 * 
 * Copyright (c) 2002 Douglas Crockford (www.crockford.com)
 * Copyright (c) 2006 John Reilly (www.inconspicuous.org)
 * 
 * @author Douglas Crockford
 * @version $Rev$, $Date$
 */
public class JavascriptMinifier {
	private static final int EOF = -1;
	private PushbackInputStream inputStream;
	private OutputStream outputStream;
	private int theA;
	private int theB;
	private int line;
	private int column;

	public JavascriptMinifier(InputStream inputStream, OutputStream outputStream) {
		this.inputStream = new PushbackInputStream(inputStream);
		this.outputStream = outputStream;
		this.line = 0;
		this.column = 0;
	}

	static boolean isAlphanum(int ch) {
		return ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || ch == '_' || ch == '$' || ch == '\\' || ch > 126);
	}

	int get() throws IOException {
		int ch = inputStream.read();

		if (ch == '\n') {
			line++;
			column = 0;
		} else {
			column++;
		}

		if (ch >= ' ' || ch == '\n' || ch == EOF) {
			return ch;
		}

		if (ch == '\r') {
			column = 0;
			return '\n';
		}

		return ' ';
	}

	int peek() throws IOException {
		int lookaheadChar = inputStream.read();
		inputStream.unread(lookaheadChar);
		return lookaheadChar;
	}

	int next() throws IOException {
		int ch = get();

		if (ch == '/') {
			switch (peek()) {
				case '/':
					for (;;) {
						ch = get();
						if (ch <= '\n') {
							return ch;
						}
					}
				case '*':
					get();
					for (;;) {
						switch (get()) {
							case '*':
								if (peek() == '/') {
									get();
									return ' ';
								}
								break;
							case EOF:
								throw new IOException("Unterminated comment at line " + line + " and column " + column);
						}
					}
				default:
					return ch;
			}

		}

		return ch;
	}

	void action(int type) throws IOException {
		switch (type) {
			case 1:
				outputStream.write(theA);
			case 2:
				theA = theB;

				if (theA == '\'' || theA == '"') {
					for (;;) {
						outputStream.write(theA);
						theA = get();

						if (theA == theB) {
							break;
						}

						if (theA <= '\n') {
							throw new IOException("Unterminated string literal at line " + line + " and column " + column);
						}

						if (theA == '\\') {
							outputStream.write(theA);
							theA = get();
						}
					}
				}
			case 3:
				theB = next();

				if (theB == '/' && (theA == '(' || theA == ',' || theA == '=' || theA == ':')) {
					outputStream.write(theA);
					outputStream.write(theB);

					for (;;) {
						theA = get();

						if (theA == '/') {
							break;
						} else if (theA == '\\') {
							outputStream.write(theA);
							theA = get();
						} else if (theA <= '\n') {
							throw new IOException("Unterminated regular expression at line " + line + " and column " + column);
						}

						outputStream.write(theA);
					}

					theB = next();
				}
		}
	}

	public void jsmin() throws IOException {
		theA = '\n';
		action(3);

		while (theA != EOF) {
			switch (theA) {
				case ' ':
					if (isAlphanum(theB)) {
						action(1);
					} else {
						action(2);
					}
					break;
				case '\n':
					switch (theB) {
						case '{':
						case '[':
						case '(':
						case '+':
						case '-':
							action(1);
							break;
						case ' ':
							action(3);
							break;
						default:
							if (isAlphanum(theB)) {
								action(1);
							} else {
								action(2);
							}
					}
					break;
				default:
					switch (theB) {
						case ' ':
							if (isAlphanum(theA)) {
								action(1);
								break;
							}
							action(3);
							break;
						case '\n':
							switch (theA) {
								case '}':
								case ']':
								case ')':
								case '+':
								case '-':
								case '"':
								case '\'':
									action(1);
									break;
								default:
									if (isAlphanum(theA)) {
										action(1);
									} else {
										action(3);
									}
							}
							break;
						default:
							action(1);
							break;
					}
			}
		}

		outputStream.flush();
	}
}
