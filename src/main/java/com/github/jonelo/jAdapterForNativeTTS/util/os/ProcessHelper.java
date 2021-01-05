package com.github.jonelo.jAdapterForNativeTTS.util.os;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessHelper {
    /**
     * Start application.
     *
     * @param executable the executable
     * @throws IOException the io exception
     */
    public static void startApplication(String executable) throws IOException {
        startApplication(executable, (String)null);
    }

    private static ProcessBuilder getProcessBuilder(String executable, String... args) {
        // Build a command to start the app
        final ArrayList<String> command = new ArrayList<>();
        command.add(executable);
        if (args != null) {
            command.addAll(Arrays.asList(args));
        }
        return new ProcessBuilder(command);
    }

    /**
     * Starts an application in the background
     * @return returns the process reference
    */
    public static Process startApplication(String executable, String... args) throws IOException {
        final ProcessBuilder builder = getProcessBuilder(executable, args);
        return builder.inheritIO().start();
    }


    // The correct way to get the output of the command is to start the
    // process, read its output, wait for the process' end and then continue the
    // call flow dependent on the exit value.
    public static ArrayList<String> startApplicationAndGetOutput(String executable, String... args) throws IOException,  InterruptedException {
        // Execute it
        final ProcessBuilder builder = getProcessBuilder(executable, args);
        Process process = builder.start();

        // Read the output of the command
        ArrayList<String> output = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
                //System.out.println(line);
            }
        }
        process.waitFor();
        //System.out.printf("Process ended with exit code %d\n", process.exitValue());
        return output;
    }
}
