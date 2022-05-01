package pl.cichy.enroller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

public class ParticipantExceptions extends ResponseStatusException {

    public ParticipantExceptions(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
        LOGGER.warn("EXCEPTION: Participant already exists!");
    }

    public ParticipantExceptions(HttpStatus status, String reason, Throwable cause, String participantLogin) {
        super(status, reason, cause);
        LOGGER.warn("EXCEPTION: No participant found with login: " + participantLogin + "!");
    }
}
