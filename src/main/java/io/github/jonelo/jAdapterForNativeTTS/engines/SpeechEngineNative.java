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

package io.github.jonelo.jAdapterForNativeTTS.engines;

import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.SpeechEngineCreationException;
import io.github.jonelo.jAdapterForNativeTTS.engines.linux.SpeechEngineLinux;
import io.github.jonelo.jAdapterForNativeTTS.engines.macos.SpeechEngineMacOS;
import io.github.jonelo.jAdapterForNativeTTS.engines.windows.SpeechEngineWindows;

import java.util.Locale;

/**
 * The Speech engine generator.
 */
public class SpeechEngineNative {

    private static SpeechEngine engine = null;

    public static SpeechEngine getInstance() throws SpeechEngineCreationException {
        if (engine == null) {
            String osName = System.getProperty("os.name");
            String osNameForComparison = osName.replaceAll("\\s", "").toLowerCase(Locale.US);
            if (osNameForComparison.startsWith("windows")) {
                engine = new SpeechEngineWindows();
            } else
            // In Big Sur (11.1) the tool called sw_vers returns "macOS" as the ProductName, but
            // the name returned by the OpenJDK 11 is still "Mac OS X" as it was with any Mac OS X 10.x
            // Nonetheless we compare against the prefix "macos" (after blanks have been removed),
            // because that comparison string is much safer for future JDK releases, and it keeps
            // compatibility with older Mac OS X versions.
            if (osNameForComparison.startsWith("macos")) {
                engine = new SpeechEngineMacOS();
            } else
            if (osNameForComparison.startsWith("linux")) {
                engine = new SpeechEngineLinux();
            } else {
                throw new SpeechEngineCreationException(String.format("Operating System '%s' is not supported.", osName));
            }
        }
        return engine;
    }
}
