package com.example;

import de.larsgrefer.sass.embedded.SassCompiler;
import de.larsgrefer.sass.embedded.SassCompilerFactory;
import org.apache.commons.lang3.StringUtils;
import sass.embedded_protocol.EmbeddedSass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static String fetchSCSS() {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader;
        String line;

        try {
            reader = new BufferedReader(new FileReader("public/assets/sample.scss"));
            if (reader != null) {
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            // Throw custom exception
            // e.printStackTrace();
        }

        return builder.toString();
    }

    private static String overrideThemeValues(String scss) {
        // static values for example, Fetch from DB
        Map<String,String> theme_values = new HashMap();
        theme_values.put("heading-color", "#111");
        theme_values.put("text-color", "#222");

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String,String> entry : theme_values.entrySet()) {
            builder.append("$").append(entry.getKey()).append(":").append(entry.getValue()).append(";").append("\n");
        }

        // Replace placeholder in .scss file with custom theme values
        scss = StringUtils.replace(scss, "// #CUSTOM_THEME_VARIABLES#", builder.toString());

        return scss;
    }

    private static String compileSass(String scss) {
        String css = "";
        try {
            SassCompiler sassCompiler = SassCompilerFactory.bundled();
            // sassCompiler.setOutputStyle(EmbeddedSass.OutputStyle.COMPRESSED);
            css = sassCompiler.compileScssString(scss).getCss();
            sassCompiler.close();
        } catch (Exception e) {
            // Throw custom exception
            // e.printStackTrace();
        }
        return css;
    }

    private static void createCSSFile(String css) {
        try {
            FileWriter myWriter = new FileWriter("public/assets/sample.css");
            myWriter.write(css);
            myWriter.close();
        } catch (IOException e) {
            // Throw custom exception
            // e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        // Example to use dart-sass-java
        // dependency: https://github.com/larsgrefer/dart-sass-java

        // Get SCSS file
        String scss = fetchSCSS();

        // Optional - override variables
        scss = overrideThemeValues(scss);

        // Dart SASS compiler
        String css = compileSass(scss);

        // Optional - create .css file
        createCSSFile(css);
    }
}