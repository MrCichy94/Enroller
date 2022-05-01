package pl.cichy.enroller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

public class MeetingExceptions extends ResponseStatusException {

    public MeetingExceptions(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
        LOGGER.warn("EXCEPTION: Meeting already exists!");
    }

    public MeetingExceptions(HttpStatus status, String reason, Throwable cause, int meetingId) {
        super(status, reason, cause);
        LOGGER.warn("EXCEPTION: No meeting found with id: " + meetingId + "!");
    }
}
