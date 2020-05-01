/**
 * module-info
 */
module de.carne {
	requires transitive java.logging;
	requires transitive java.prefs;

	requires org.apache.logging.log4j;
	requires org.eclipse.jdt.annotation;
	requires org.slf4j;

	exports de.carne.boot;
	exports de.carne.io;
	exports de.carne.nio.file;
	exports de.carne.nio.file.attribute;
	exports de.carne.text;
	exports de.carne.util;
	exports de.carne.util.cmdline;
	exports de.carne.util.function;
	exports de.carne.util.logging;
	exports de.carne.util.prefs;
	exports de.carne.util.stream;
	exports de.carne.util.validation;
}
