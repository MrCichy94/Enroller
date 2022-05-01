package pl.cichy.enroller.service;

import pl.cichy.enroller.model.Participant;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ParticipantService {

    List<Participant> getAllParticipants();

    Optional<Participant> getByLogin(String participantLogin);

    void addNewParticipant(Participant newParticipantToAdd);

    void deleteParticipantByLogin(String participantLoginToDelete);

}
