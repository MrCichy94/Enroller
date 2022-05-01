package pl.cichy.enroller.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;
import pl.cichy.enroller.exception.MeetingExceptions;
import pl.cichy.enroller.exception.ParticipantExceptions;
import pl.cichy.enroller.model.Meeting;
import pl.cichy.enroller.model.Participant;
import pl.cichy.enroller.model.repository.MeetingRepository;
import pl.cichy.enroller.model.repository.ParticipantRepository;
import pl.cichy.enroller.service.MeetingService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequestScope
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;
    private final ObjectMapper objectMapper;

    public MeetingServiceImpl(final MeetingRepository meetingRepository,
                              final ParticipantRepository participantRepository,
                              final ObjectMapper objectMapper) {
        this.meetingRepository = meetingRepository;
        this.participantRepository = participantRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    @Override
    public Optional<Meeting> getMeetingById(int meetingId) {
        if (meetingRepository.findById(meetingId).isEmpty()) {
            throw new MeetingExceptions(HttpStatus.NOT_FOUND,
                    "Meeting with id: " + meetingId + " - not exist!",
                    new RuntimeException(),
                    meetingId);
        } else {
            return meetingRepository.findById(meetingId);
        }
    }

    @Transactional
    public void addNewMeeting(Meeting newMeetingToAdd) {
        if (meetingRepository.findById(newMeetingToAdd.getMeetingId()).isPresent()) {
            throw new MeetingExceptions(HttpStatus.BAD_REQUEST,
                    "Meeting with this id: " + newMeetingToAdd.getMeetingId() + "meetingId already exist!",
                    new RuntimeException());
        } else {
            meetingRepository.save(newMeetingToAdd);
        }
    }

    @Transactional
    public void addParticipantToMeeting(int meetingId, String newParticipantToAdd) {
        Optional<Participant> participant = participantRepository.findByLogin(newParticipantToAdd);
        Optional<Meeting> meeting = meetingRepository.findById(meetingId);
        if (participant.isEmpty()) {
            throw new ParticipantExceptions(HttpStatus.NOT_FOUND,
                    "Meeting with id: " + meetingId + " - not exist!",
                    new RuntimeException(),
                    newParticipantToAdd);
        }
        if (meeting.orElseThrow().getParticipants().contains(participant.get())) {
            throw new ParticipantExceptions(HttpStatus.BAD_REQUEST,
                    "Participant with login: " + newParticipantToAdd + " already in meeting!",
                    new RuntimeException());
        } else {
            Meeting toUpload = meeting.get();
            toUpload.addParticipant(participant.get());
            meetingRepository.save(toUpload);
        }
    }

    @Transactional
    public void removeParticipantFromMeeting(int meetingId, String participantLogin) {
        Optional<Meeting> meeting = meetingRepository.findById(meetingId);
        Optional<Participant> participant = participantRepository.findByLogin(participantLogin);
        if (meeting.isEmpty()) {
            throw new MeetingExceptions(HttpStatus.NOT_FOUND,
                    "Meeting with id: " + meetingId + " - not exist!",
                    new RuntimeException(),
                    meetingId);
        } else if (participant.isEmpty()) {
            throw new ParticipantExceptions(HttpStatus.NOT_FOUND,
                    "Participant with login: " + participantLogin + " already not in meeting!",
                    new RuntimeException(),
                    participantLogin);
        } else {
            meeting.get().getParticipants().remove(participant.get());
            meetingRepository.save(meeting.get());
        }
    }

    @Override
    public List<Participant> getParticipantsByMeetingId(int meetingId) {
        /** DEBUG
         * print participants logins from meeting's participant's list
         */
        meetingRepository.findById(meetingId).orElseThrow().getParticipants().forEach(s -> System.out.println(s.getLogin()));
        return meetingRepository.findById(meetingId).orElseThrow().getParticipants();
    }

    @Transactional
    public void deleteMeetingById(int meetingId) {
        Optional<Meeting> meeting = meetingRepository.findById(meetingId);
        if (meeting.isEmpty()) {
            throw new MeetingExceptions(HttpStatus.NOT_FOUND,
                    "CAN NOT delete meeting with id: " + meetingId + " - not exist!",
                    new RuntimeException(),
                    meetingId);
        } else {
            meetingRepository.deleteById(meetingId);
        }
    }

    @Transactional
    public void updateMeetingById(int meetingId,
                                  JsonPatch meetingToUpdate) throws JsonPatchException, JsonProcessingException {
        try {
            Meeting meetingPatched =
                applyPatchToMeeting(meetingToUpdate, meetingRepository.findById(meetingId).orElseThrow());
            meetingRepository.save(meetingPatched);
        } catch (RuntimeException noMeeting) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private Meeting applyPatchToMeeting(
            JsonPatch patch, Meeting targetMeeting) throws JsonProcessingException, JsonPatchException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetMeeting, JsonNode.class));
        return objectMapper.treeToValue(patched, Meeting.class);
    }
}
