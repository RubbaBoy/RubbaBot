package com.uddernetworks.rubbabot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MessageFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageFilter.class);

    private static final String ID = "249962392241307649";
    private static final List<String> COMMAND_PREFIXES = Arrays.asList("!", "`", "/", "=", "$");

    public static void main(String[] args) throws IOException {
        new MessageFilter().start(args);
    }

    public void start(String[] args) throws IOException {
        var in = new File(args.length > 0 ? args[0] : "input");
        var out = new File(args.length > 1 ? args[0] : "output.txt");
        LOGGER.info("Parsing CSV files from {} to {}", in.getAbsolutePath(), out.getAbsolutePath());

        Files.writeString(out.toPath(),
                Arrays.stream(Objects.requireNonNull(in.listFiles((dir, name) -> name.endsWith(".csv")))).map(file -> {
                    try {
                        return Files.readAllLines(file.toPath(), StandardCharsets.ISO_8859_1);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Collections.<String>emptyList();
                    }
                }).flatMap(List::stream)
                        .map(line -> line.replaceAll("[^\\p{ASCII}]", "")) // Remove all non-ASCII
                        .map(line -> line.split(";")) // Split into CSV columns
                        .filter(csv -> csv.length >= 4) // Remove all that don't have 4 columns
                        .map(csv -> Arrays.stream(csv).map(this::removeQuotes).toArray(String[]::new)) // Remove quotes
                        .filter(csv -> csv[0].equals(ID) && !csv[3].isBlank() && !isCommand(csv[3]) && !csv[3].startsWith("http")) // Keep all by RubbaBoy
                        .map(csv -> csv[3]) // Keep message
                        .collect(Collectors.joining("\n")));
    }

    private static boolean isCommand(String input) {
        return COMMAND_PREFIXES.stream().anyMatch(input::startsWith);
    }

    private String removeQuotes(String input) {
        if (input.length() <= 2) return "";
        if (input.startsWith("\"")) input = input.substring(1);
        if (input.endsWith("\"")) input = input.substring(0, input.length() - 1);
        return input;
    }

}
