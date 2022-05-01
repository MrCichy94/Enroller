package pl.cichy.enroller.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import pl.cichy.enroller.model.Meeting;
import pl.cichy.enroller.model.Participant;

import java.util.List;
import java.util.Optional;

public interface MeetingService {

    List<Meeting> getAllMeetings();

    Optional<Meeting> getMeetingById(int meetingId);

    void addNewMeeting(Meeting newMeetingToAdd);

    void addParticipantToMeeting(int meetingId, String newParticipantToAdd);

    List<Participant> getParticipantsByMeetingId(int meetingId);

    void deleteMeetingById(int meetingId);

    void updateMeetingById(int meetingId, JsonPatch meetingToUpdate) throws JsonPatchException, JsonProcessingException;

    void removeParticipantFromMeeting(int meetingId, String participantLogin);
}
