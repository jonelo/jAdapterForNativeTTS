/*
 * MIT License
 *
 * Copyright (c) 2021 Johann N. Löfflmann
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.jonelo.jAdapterForNativeTTS.util.os;

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
     * Starts an application in the background.
     *
     * @param executable the name of the executable
     * @param args the arguments for the executable
     * @return returns the process reference
     * @throws IOException in case of an I/O error
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
