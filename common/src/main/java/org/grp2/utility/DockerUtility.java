package org.grp2.utility;

import java.util.Optional;

public class DockerUtility {

    public static boolean isInDocker() {
        return getBooleanEnv("inDocker");
    }

    public static String dockerValueOrDefault(String dockerValue, String defaultValue) {
        if (isInDocker()) {
            return dockerValue;
        } else {
            return defaultValue;
        }
    }

    public static String getEnvOrDefault(String envVar, String defaultValue) {
        Optional<String> env = getEnv(envVar);
        return env.orElse(defaultValue);
    }

    public static Optional<String> getEnv(String envVar) {
        return Optional.ofNullable(System.getenv(envVar));
    }

    public static Boolean getBooleanEnv(String envVar) {
        if (getEnv(envVar).isPresent()) {
            return Boolean.valueOf(getEnv(envVar).get());
        } else {
            return false;
        }
    }

}
