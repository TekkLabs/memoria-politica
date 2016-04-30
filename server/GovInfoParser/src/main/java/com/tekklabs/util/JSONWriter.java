package com.tekklabs.util;

import java.io.StringWriter;

/**
 * Sample usage:
 * <pre>
 * Writer writer = new JSONWriter(); // this writer adds indentation
 * jsonobject.writeJSONString(writer);
 * System.out.println(writer.toString());
 * </pre>
 *
 * @author Elad Tabak
 * @author Maciej Komosinski, minor improvements, 2015
 * @since 28-Nov-2011
 * @version 0.2
 */
public class JSONWriter extends StringWriter
{
    final static String indentstring = "  "; //define as you wish
    final static String spaceaftercolon = " "; //use "" if you don't want space after colon

    private int indentlevel = 0;

    @Override
    public void write(int c)
    {
        char ch = (char) c;
        if (ch == '[' || ch == '{')
        {
            super.write(c);
            super.write('\n');
            indentlevel++;
            writeIndentation();
        } else if (ch == ',')
        {
            super.write(c);
            super.write('\n');
            writeIndentation();
        } else if (ch == ']' || ch == '}')
        {
            super.write('\n');
            indentlevel--;
            writeIndentation();
            super.write(c);
        } else if (ch == ':')
        {
            super.write(c);
            super.write(spaceaftercolon);
        } else
        {
            super.write(c);
        }

    }

    private void writeIndentation()
    {
        for (int i = 0; i < indentlevel; i++)
        {
            super.write(indentstring);
        }
    }
}

