package com.fuzzylist.common;

import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;

/**
 * Generate string messages based on a given template. This class provide implementation similar to
 * {@link String#format(String, Object...)}, e.g.:<p>
 * <pre>
 *      String message = String.format("The sum of %d and %d is: %d", a, b, a+b);
 * </pre>
 * However, this implementation require specifying parameter by name and not by location, e.g.:
 * <pre>
 *      String message = TemplateString.create("The sum of ${a} and ${b} is: ${theSum}")
 *                                     .with("a", a)
 *                                     .with("b", b)
 *                                     .with("theSum", a+b)
 *                                     .render();
 * </pre>
 * or:
 * <pre>
 *      Map<String, Object> context = .....
 *      String message = TemplateString.create("The sum of ${a} and ${b} is: ${theSum}")
 *                                     .with(context)
 *                                     .render();
 * </pre>
 * <p>
 * This allows construction of a context containing various variables and render an anonymous template (a
 * template which we do not know its contents ahead).
 *
 * @author Guy Raz Nir
 * @since 2024/02/04
 */
public class TemplateString {

    /**
     * Template to render.
     */
    private String template;

    /**
     * All parameters.
     */
    private final Map<String, Object> parameters = new HashMap<>();

    /**
     * Placeholder prefix.
     */
    private static final String PREFIX = "${";

    /**
     * Placeholder suffix.
     */
    private static final String SUFFIX = "}";

    /**
     * Class constructor.
     *
     * @param template Template to use. May be {@code null}.
     */
    private TemplateString(String template) {
        this.template = template;
    }

    /**
     * Construct a new {@code TemplateString} instance without a template.
     *
     * @return A new {@code TemplateString}.
     */
    public static TemplateString create() {
        return new TemplateString(null);
    }

    /**
     * Construct a new {@code TemplateString} instance with a template.
     *
     * @param template Template to use. May be {@code null}.
     * @return A new {@code TemplateString}.
     */
    public static TemplateString create(String template) {
        return new TemplateString(template);
    }

    /**
     * Generate a string from a given <i>template</i> and <i>parameters</i>.
     *
     * @param template   Template to render.
     * @param parameters Parameters required for <i>template</i>.
     * @return Rendered string.
     */
    public static String format(String template, Map<String, ?> parameters) {
        return format(template, parameters, PREFIX, SUFFIX);
    }

    /**
     * Set a template to this renderer.
     *
     * @param template Template to use. May be {@code null}.
     * @return This instance.
     */
    public TemplateString withTemplate(String template) {
        this.template = template;
        return this;
    }

    /**
     * Add/update parameter.
     *
     * @param parameterName  Parameter name.
     * @param parameterValue Parameter value.
     * @return This instance.
     */
    public TemplateString with(String parameterName, Object parameterValue) {
        parameters.put(parameterName, parameterValue);
        return this;
    }

    /**
     * Add a collection of parameters.
     *
     * @param map Source map to read parameters from.
     * @return This instance.
     */
    public TemplateString with(Map<String, Object> map) {
        if (map != null) {
            this.parameters.putAll(map);
        }
        return this;
    }

    /**
     * Render the template into a string.
     *
     * @return Rendered string or {@code null} if template is {@code null}.
     */
    public String format() {
        return format(template, parameters, PREFIX, SUFFIX);
    }

    /**
     * @return Rendered string. See {@link #format()}.
     */
    public String toString() {
        return format();
    }

    /**
     * Renders a template and a given parameters into a string.
     *
     * @param template   Template to render.
     * @param parameters Parameters map that may be required by <i>template</i>.
     * @param prefix     Prefix of placeholder.
     * @param suffix     Suffix of placeholder.
     * @return String generated from <i>template</i> and <i>parameters</i>.
     */
    protected static String format(String template, Map<String, ?> parameters, String prefix, String suffix) {
        return template != null ? StringSubstitutor.replace(template, parameters, PREFIX, SUFFIX) : null;
    }

}
