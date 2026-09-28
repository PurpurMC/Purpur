package org.purpurmc.purpur.gui.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.util.PerformanceSensitive;

import java.util.List;

@Plugin(name = "highlightGUIError", category = PatternConverter.CATEGORY)
@ConverterKeys({"highlightGUIError"})
@PerformanceSensitive("allocation")
public final class HighlightErrorConverter extends LogEventPatternConverter {
    private static final String ERROR = "\u00A74\u00A7l"; // Bold Red
    private static final String WARN = "\u00A7e\u00A7l"; // Bold Yellow

    private final List<PatternFormatter> formatters;

    private HighlightErrorConverter(List<PatternFormatter> formatters) {
        super("highlightGUIError", null);
        this.formatters = formatters;
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        Level level = event.getLevel();
        if (level.isMoreSpecificThan(Level.ERROR)) {
            format(ERROR, event, toAppendTo);
            return;
        } else if (level.isMoreSpecificThan(Level.WARN)) {
            format(WARN, event, toAppendTo);
            return;
        }
        for (PatternFormatter formatter : formatters) {
            formatter.format(event, toAppendTo);
        }
    }

    private void format(String style, LogEvent event, StringBuilder toAppendTo) {
        int start = toAppendTo.length();
        toAppendTo.append(style);
        int end = toAppendTo.length();

        for (PatternFormatter formatter : formatters) {
            formatter.format(event, toAppendTo);
        }

        if (toAppendTo.length() == end) {
            toAppendTo.setLength(start);
        }
    }

    @Override
    public boolean handlesThrowable() {
        for (final PatternFormatter formatter : formatters) {
            if (formatter.handlesThrowable()) {
                return true;
            }
        }
        return false;
    }

    public static HighlightErrorConverter newInstance(Configuration config, String[] options) {
        if (options.length != 1) {
            LOGGER.error("Incorrect number of options on highlightGUIError. Expected 1 received " + options.length);
            return null;
        }

        if (options[0] == null) {
            LOGGER.error("No pattern supplied on highlightGUIError");
            return null;
        }

        PatternParser parser = PatternLayout.createPatternParser(config);
        List<PatternFormatter> formatters = parser.parse(options[0]);
        return new HighlightErrorConverter(formatters);
    }
}