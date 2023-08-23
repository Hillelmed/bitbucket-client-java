package io.github.hmedioni.bitbucket.client;

import org.springframework.lang.*;

import java.lang.reflect.*;
import java.util.*;

import static io.github.hmedioni.bitbucket.client.BitbucketConstants.*;


/**
 * Collection of static methods to be used globally.
 */
public class BitbucketUtils {


    protected BitbucketUtils() {
        throw new UnsupportedOperationException("Purposefully not implemented");
    }

    /**
     * If the passed systemProperty is non-null we will attempt to query
     * the `System Properties` for a value and return it. If no value
     * was found, and environmentVariable is non-null, we will attempt to
     * query the `Environment Variables` for a value and return it. If
     * both are either null or can't be found than null will be returned.
     *
     * @param systemProperty      possibly existent System Property.
     * @param environmentVariable possibly existent Environment Variable.
     * @return found external value or null.
     */
    public static String retrieveExternalValue(@Nullable final String systemProperty,
                                               @Nullable final String environmentVariable) {

        // 1.) Search for System Property
        if (systemProperty != null) {
            final String value = System.getProperty(systemProperty);
            if (value != null) {
                return value;
            }
        }

        if (environmentVariable != null) {
            final String value = System.getenv(environmentVariable);
            return value;
        }

        return null;
    }

    /**
     * Find endpoint searching first within `System Properties` and
     * then within `Environment Variables` returning whichever has a
     * value first.
     *
     * @return endpoint or null if it can't be found.
     */
    public static String inferEndpoint() {
        final String possibleValue = BitbucketUtils
                .retrieveExternalValue(ENDPOINT_SYSTEM_PROPERTY,
                        ENDPOINT_ENVIRONMENT_VARIABLE);
        return possibleValue != null ? possibleValue : DEFAULT_ENDPOINT;
    }

    /**
     * Find credentials (Basic, Bearer, or Anonymous) from system/environment.
     *
     * @return BitbucketCredentials
     */
    public static BitbucketAuthentication inferAuthentication() {

        // 1.) Check for "Basic" auth credentials.
        final BitbucketAuthentication.Builder inferAuth = BitbucketAuthentication.builder();
        String authValue = BitbucketUtils
                .retrieveExternalValue(CREDENTIALS_SYSTEM_PROPERTY,
                        CREDENTIALS_ENVIRONMENT_VARIABLE);
        if (authValue != null) {
            inferAuth.credentials(authValue);
        }

        return inferAuth.build();
    }

    /**
     * Find jclouds overrides (e.g. Properties) first searching within System
     * Properties and then within Environment Variables (former takes precedance).
     *
     * @return Properties object with populated jclouds properties.
     */
    public static Properties inferOverrides() {
        final Properties overrides = new Properties();

        // 1.) Iterate over system properties looking for relevant properties.
        final Properties systemProperties = System.getProperties();
        final Enumeration<String> enums = (Enumeration<String>) systemProperties.propertyNames();
        while (enums.hasMoreElements()) {
            final String key = enums.nextElement();
            if (key.startsWith(BITBUCKET_REST_PROPERTY_ID)) {
                final int index = key.indexOf(JCLOUDS_PROPERTY_ID);
                final String trimmedKey = key.substring(index);
                overrides.put(trimmedKey, systemProperties.getProperty(key));
            }
        }

        // 2.) Iterate over environment variables looking for relevant variables. System
        //     Properties take precedence here so if the same property was already found
        //     there then we don't add it or attempt to override.
        for (final Map.Entry<String, String> entry : System.getenv().entrySet()) {
            if (entry.getKey().startsWith(BITBUCKET_REST_VARIABLE_ID)) {
                final int index = entry.getKey().indexOf(JCLOUDS_VARIABLE_ID);
                final String trimmedKey = entry.getKey()
                        .substring(index)
                        .toLowerCase()
                        .replace("_", ".");
                overrides.computeIfAbsent(trimmedKey, o -> overrides.put(o, entry.getValue()));
            }
        }

        return overrides;
    }

    /**
     * Add the passed environment variables to the currently existing env-vars.
     *
     * @param addEnvVars the env-vars to add.
     */
    public static void addEnvironmentVariables(final Map<String, String> addEnvVars) {
        Objects.requireNonNull(addEnvVars, "Must pass non-null Map");
        final Map<String, String> newenv = new HashMap<>(System.getenv());
        newenv.putAll(addEnvVars);
        setEnvironmentVariables(newenv);
    }

    /**
     * Remove the passed environment variables keys from the environment.
     *
     * @param removeEnvVars the env-var keys to be removed.
     */
    public static void removeEnvironmentVariables(final Collection<String> removeEnvVars) {
        Objects.requireNonNull(removeEnvVars, "Must pass non-null Collection");
        final Map<String, String> newenv = new HashMap<>(System.getenv());
        newenv.keySet().removeAll(removeEnvVars);
        setEnvironmentVariables(newenv);
    }

    /**
     * Re-set the environment variables with passed map.
     *
     * @param newEnvVars map to reset env-vars with.
     */
    public static void setEnvironmentVariables(final Map<String, String> newEnvVars) {
        Objects.requireNonNull(newEnvVars, "Must pass non-null Map");

        try {
            final Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            final Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            final Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newEnvVars);
            final Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            final Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newEnvVars);
        } catch (final ClassNotFoundException | IllegalAccessException | IllegalArgumentException |
                       NoSuchFieldException | SecurityException e) {
            final Class[] classes = Collections.class.getDeclaredClasses();
            final Map<String, String> env = System.getenv();
            for (final Class cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    try {
                        final Field field = cl.getDeclaredField("m");
                        field.setAccessible(true);
                        final Object obj = field.get(env);
                        final Map<String, String> map = (Map<String, String>) obj;
                        map.clear();
                        map.putAll(newEnvVars);
                    } catch (final NoSuchFieldException | IllegalAccessException e2) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
