package pl.cichy.enroller.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pl.cichy.enroller.exception.ParticipantExceptions;
import pl.cichy.enroller.model.Participant;
import pl.cichy.enroller.model.repository.ParticipantRepository;
import pl.cichy.enroller.service.ParticipantService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequestScope
public class ParticipantServiceImpl implements ParticipantService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ParticipantRepository participantRepository;

    public ParticipantServiceImpl(final ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @Override
    public Optional<Participant> getByLogin(String participantLogin) {
        if (participantRepository.findByLogin(participantLogin).isEmpty()) {
            throw new ParticipantExceptions(HttpStatus.NOT_FOUND,
                    "Meeting with id: " + participantLogin + " - not exist!",
                    new RuntimeException(),
                    participantLogin);
        } else {
            return participantRepository.findByLogin(participantLogin);
        }
    }

    @Transactional
    public void addNewParticipant(Participant participant) {
        if (participantRepository.findByLogin(participant.getLogin()).isPresent()) {
            throw new ParticipantExceptions(HttpStatus.BAD_REQUEST,
                    "Participant with login: " + participant.getLogin() + " already exist!",
                    new RuntimeException());
        } else {
            Participant encryptedParticipant =
                    new Participant(participant.getLogin(),passwordEncoder.encode(participant.getPassword()));
            participantRepository.save(encryptedParticipant);
        }
    }

    @Transactional
    public void deleteParticipantByLogin(String participantLogin) {
        Optional<Participant> participant = participantRepository.findByLogin(participantLogin);
        if (participant.isEmpty()) {
            throw new ParticipantExceptions(HttpStatus.NOT_FOUND,
                    "CAN NOT delete participant with login: " + participantLogin + " - not exist!",
                    new RuntimeException(),
                    participantLogin);
        } else {
            participantRepository.deleteByLogin(participantLogin);
        }
    }
}
