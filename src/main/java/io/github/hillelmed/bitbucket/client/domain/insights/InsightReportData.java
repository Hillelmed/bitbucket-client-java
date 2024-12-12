package io.github.hillelmed.bitbucket.client.domain.insights;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * The type Insight report data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InsightReportData {
    private String title;
    private DataType type;
    private Object value;

    /**
     * Create boolean insight report data.
     *
     * @param title the title
     * @param value the value
     * @return the insight report data
     */
    public static InsightReportData createBoolean(final String title, final boolean value) {
        return new InsightReportData(title, DataType.BOOLEAN, value);
    }

    /**
     * Create date insight report data.
     *
     * @param title the title
     * @param value the value
     * @return the insight report data
     */
    public static InsightReportData createDate(final String title, final LocalDate value) {
        return createDate(title, value.atStartOfDay());
    }

    /**
     * Create date insight report data.
     *
     * @param title the title
     * @param value the value
     * @return the insight report data
     */
    public static InsightReportData createDate(final String title, final LocalDateTime value) {
        final long epochMilli = value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return createDate(title, epochMilli);
    }

    /**
     * Create date insight report data.
     *
     * @param title      the title
     * @param epochMilli the epoch milli
     * @return the insight report data
     */
    public static InsightReportData createDate(final String title, final long epochMilli) {
        return new InsightReportData(title, DataType.DATE, epochMilli);
    }

    /**
     * Create duration insight report data.
     *
     * @param title    the title
     * @param duration the duration
     * @return the insight report data
     */
    public static InsightReportData createDuration(final String title, final Duration duration) {
        return new InsightReportData(title, DataType.DURATION, duration.toMillis());
    }

    /**
     * Create duration insight report data.
     *
     * @param title  the title
     * @param millis the millis
     * @return the insight report data
     */
    public static InsightReportData createDuration(final String title, final long millis) {
        return new InsightReportData(title, DataType.DURATION, millis);
    }

    /**
     * Create link insight report data.
     *
     * @param title the title
     * @param href  the href
     * @return the insight report data
     */
    public static InsightReportData createLink(final String title, final String href) {
        return createLink(title, href, href);
    }

    /**
     * Create link insight report data.
     *
     * @param title    the title
     * @param href     the href
     * @param linkText the link text
     * @return the insight report data
     */
    public static InsightReportData createLink(final String title,
                                               final String href,
                                               final String linkText) {
        final InsightReportDataLink link = new InsightReportDataLink(linkText, href);
        return new InsightReportData(title, DataType.LINK, link);
    }

    /**
     * Create number insight report data.
     *
     * @param title  the title
     * @param number the number
     * @return the insight report data
     */
    public static InsightReportData createNumber(final String title, final long number) {
        return new InsightReportData(title, DataType.NUMBER, number);
    }

    /**
     * Create percentage insight report data.
     *
     * @param title      the title
     * @param percentage the percentage
     * @return the insight report data
     */
    public static InsightReportData createPercentage(final String title, final byte percentage) {
        return new InsightReportData(title, DataType.PERCENTAGE, percentage);
    }

    /**
     * Create text insight report data.
     *
     * @param title the title
     * @param text  the text
     * @return the insight report data
     */
    public static InsightReportData createText(final String title, final String text) {
        return new InsightReportData(title, DataType.TEXT, text);
    }

    /**
     * The enum Data type.
     */
    public enum DataType {
        /**
         * Boolean data type.
         */
        BOOLEAN,
        /**
         * Date data type.
         */
        DATE,
        /**
         * Duration data type.
         */
        DURATION,
        /**
         * Link data type.
         */
        LINK,
        /**
         * Number data type.
         */
        NUMBER,
        /**
         * Percentage data type.
         */
        PERCENTAGE,
        /**
         * Text data type.
         */
        TEXT
    }


}
