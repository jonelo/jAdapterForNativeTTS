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
            String osNameForComparison = osName.toLowerCase(Locale.US);
            if (osNameForComparison.startsWith("windows")) {
                engine = new SpeechEngineWindows();
            } else
            if (osNameForComparison.startsWith("mac os x")) {
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
