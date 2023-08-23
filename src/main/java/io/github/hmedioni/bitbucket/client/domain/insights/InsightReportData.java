package io.github.hmedioni.bitbucket.client.domain.insights;


import lombok.*;

import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InsightReportData {
    private String title;
    private DataType type;
    private Object value;

    public static InsightReportData createBoolean(final String title, final boolean value) {
        return new InsightReportData(title, DataType.BOOLEAN, value);
    }

    public static InsightReportData createDate(final String title, final LocalDate value) {
        return createDate(title, value.atStartOfDay());
    }

    public static InsightReportData createDate(final String title, final LocalDateTime value) {
        final long epochMilli = value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return createDate(title, epochMilli);
    }

    public static InsightReportData createDate(final String title, final long epochMilli) {
        return new InsightReportData(title, DataType.DATE, epochMilli);
    }

    public static InsightReportData createDuration(final String title, final Duration duration) {
        return new InsightReportData(title, DataType.DURATION, duration.toMillis());
    }

    public static InsightReportData createDuration(final String title, final long millis) {
        return new InsightReportData(title, DataType.DURATION, millis);
    }

    public static InsightReportData createLink(final String title, final String href) {
        return createLink(title, href, href);
    }

    public static InsightReportData createLink(final String title,
                                               final String href,
                                               final String linkText) {
        final InsightReportDataLink link = new InsightReportDataLink(linkText, href);
        return new InsightReportData(title, DataType.LINK, link);
    }

    public static InsightReportData createNumber(final String title, final long number) {
        return new InsightReportData(title, DataType.NUMBER, number);
    }

    public static InsightReportData createPercentage(final String title, final byte percentage) {
        return new InsightReportData(title, DataType.PERCENTAGE, percentage);
    }

    public static InsightReportData createText(final String title, final String text) {
        return new InsightReportData(title, DataType.TEXT, text);
    }

    public enum DataType {
        BOOLEAN,
        DATE,
        DURATION,
        LINK,
        NUMBER,
        PERCENTAGE,
        TEXT
    }


}
