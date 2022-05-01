package pl.cichy.enroller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.cichy.enroller.exception.ParticipantExceptions;
import pl.cichy.enroller.model.Participant;
import pl.cichy.enroller.service.ParticipantService;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping("/participants")
public class ParticipantController {

    @Autowired
    ParticipantService participantService;

    private static final java.util.logging.Logger logger = Logger.getLogger("ParticipantController DebugLog: ");

    @GetMapping("")
    public ResponseEntity<?> getAllParticipants() {
        Collection<Participant> participants = participantService.getAllParticipants();
        logger.info("Get all participants.");
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

    @GetMapping("/{participantLogin}")
    public ResponseEntity<?> getParticipantById(@PathVariable("participantLogin") String participantLogin) {
        try {
            Optional<Participant> participant = participantService.getByLogin(participantLogin);
            logger.info("Get participant with login: " + participantLogin);
            return new ResponseEntity<>(participant, HttpStatus.OK);
        } catch (ParticipantExceptions e) {
            logger.warning("Participant with login: " + participantLogin + " - DOES NOT exists!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/add")
    ResponseEntity<Participant> createNewParticipant(@RequestBody Participant newParticipantToAdd) {
        try {
            participantService.addNewParticipant(newParticipantToAdd);
            logger.info("New participant with login: " + newParticipantToAdd.getLogin() + " was created!");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ParticipantExceptions e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{participantLogin}")
    ResponseEntity<Participant> deleteParticipant(@PathVariable String participantLogin) {
        try {
            participantService.deleteParticipantByLogin(participantLogin);
            logger.info("Participant with login: " + participantLogin + " was deleted!");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ParticipantExceptions e) {
            logger.warning("Participant with login: " + participantLogin + " DOES NOT exists!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
