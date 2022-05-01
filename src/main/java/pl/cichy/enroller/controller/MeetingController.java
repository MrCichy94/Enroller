package pl.cichy.enroller.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.cichy.enroller.exception.MeetingExceptions;
import pl.cichy.enroller.exception.ParticipantExceptions;
import pl.cichy.enroller.model.Meeting;
import pl.cichy.enroller.model.Participant;
import pl.cichy.enroller.service.MeetingService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping("/meetings")
public class MeetingController {

    @Autowired
    MeetingService meetingService;

    private static final java.util.logging.Logger logger = Logger.getLogger("MeetingController DebugLog: ");

    @GetMapping("")
    public ResponseEntity<?> getAllMeetings() {
        List<Meeting> meetings = meetingService.getAllMeetings();
        logger.info("Get all meetings.");
        return new ResponseEntity<>(meetings, HttpStatus.OK);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<?> getMeetingById(@PathVariable("meetingId") int meetingId) {
        try {
            Optional<Meeting> meeting = meetingService.getMeetingById(meetingId);
            logger.info("Get meeting with id: " + meetingId);
            return new ResponseEntity<>(meeting, HttpStatus.OK);
        } catch (MeetingExceptions e) {
            logger.warning("Meeting with id: " + meetingId + " - DOES NOT exists!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{meetingId}/participants")
    public ResponseEntity<?> getParticipants(@PathVariable("meetingId") int meetingId) {
        List<Participant> participants = meetingService.getParticipantsByMeetingId(meetingId);
        logger.info("Participants of meeting with id: " + meetingId + " requested.");
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

    @PostMapping("/add")
    ResponseEntity<Meeting> createNewMeeting(@RequestBody Meeting newMeetingToAdd) {
        try {
            meetingService.addNewMeeting(newMeetingToAdd);
            logger.info("New meeting with title" + newMeetingToAdd.getTitle() + " was created!");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (MeetingExceptions e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{meetingId}")
    ResponseEntity<Meeting> addParticipantToMeeting(@PathVariable("meetingId") int meetingId,
                                                    @RequestBody String newParticipantToAdd) {
        try{
            logger.info("Participant added to meeting with id: " + meetingId + ".");
            meetingService.addParticipantToMeeting(meetingId, newParticipantToAdd);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ParticipantExceptions | MeetingExceptions e) {
            logger.warning("Participant with login: " + newParticipantToAdd + " CAN NOT be added to meeting with id: " + meetingId + "!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{meetingId}/{participantLogin}")
    ResponseEntity<Meeting> removeParticipantFromMeeting(@PathVariable("meetingId") int meetingId,
                                                    @PathVariable("participantLogin") String participantLogin) {
        try{
            logger.info(participantLogin + " was removed from meeting with id: " + meetingId + ".");
            meetingService.removeParticipantFromMeeting(meetingId, participantLogin);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ParticipantExceptions | MeetingExceptions e) {
            logger.warning("Participant CAN NOT be added to meeting with id: " + meetingId + "!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /** JSON PATCH STRUCT
     * path  -> object variable
     * value -> in "quotes"
     * easy to doing in angular front-end.
     [{
        "op":"replace",
        "path":"",
        "value": {
            "meetingId": 1,
            "title": "my55",
            "date": "april55"
        }
     }]
     */
    @PatchMapping("/{meetingId}")
    ResponseEntity<Meeting> updateMeetingWithId(@PathVariable("meetingId") int meetingId,
                                                @RequestBody JsonPatch meetingToUpdate) {
        try {
            meetingService.updateMeetingById(meetingId, meetingToUpdate);
            logger.warning("Meeting CAN NOT be updated!");
            return ResponseEntity.ok().build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{meetingId}")
    public ResponseEntity<?> deleteMeetingById(@PathVariable("meetingId") int meetingId) {
        try {
            meetingService.deleteMeetingById(meetingId);
            logger.info("Meeting with id: " + meetingId + " was deleted!");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MeetingExceptions e) {
            logger.warning("Meeting with id: " + meetingId + " DOES NOT exists!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
