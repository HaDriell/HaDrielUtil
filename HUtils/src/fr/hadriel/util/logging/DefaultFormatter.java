package fr.hadriel.util.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DefaultFormatter extends Formatter {

    private static final DateFormat defaultFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public String format(LogRecord record) {
        StringBuilder out = new StringBuilder();
        out.append('[').append(defaultFormat.format(record.getMillis())).append("] ");
        out.append('[').append(record.getLevel()).append("] ");
        out.append('[').append(record.getSourceClassName()).append('.').append(record.getSourceMethodName()).append("] ");
        out.append(formatMessage(record)).append('\n');
        return out.toString();
    }
}