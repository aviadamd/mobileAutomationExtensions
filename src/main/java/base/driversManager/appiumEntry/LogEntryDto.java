package base.driversManager.appiumEntry;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogEntryDto {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String logType;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String logMessages;

    public LogEntryDto() {}

    public LogEntryDto(String logType, String logMessages) {
        this.logType = logType;
        this.logMessages = logMessages;
    }

    public String getLogType() { return logType; }
    public void setLogType(String logType) { this.logType = logType; }
    public String getLogMessages() { return logMessages; }
    public void setLogMessages(String logMessages) { this.logMessages = logMessages; }
}
