package com.github.jonelo.jAdapterForNativeTTS.engines;

import com.github.jonelo.jAdapterForNativeTTS.engines.linux.SpeechEngineLinux;
import com.github.jonelo.jAdapterForNativeTTS.engines.macos.SpeechEngineMacOS;
import com.github.jonelo.jAdapterForNativeTTS.engines.windows.SpeechEngineWindows;
import com.github.jonelo.jAdapterForNativeTTS.engines.exceptions.NotSupportedOperatingSystemException;

import java.util.Locale;

/**
 * The Speech engine generator.
 */
public class SpeechEngineNative {

    private static SpeechEngine engine = null;

    public static SpeechEngine getInstance() throws NotSupportedOperatingSystemException {
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
                throw new NotSupportedOperatingSystemException(String.format("Operating System '%s' is not supported.", osName));
            }
        }

        return engine;
    }
}
