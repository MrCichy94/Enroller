package pl.cichy.enroller.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.cichy.enroller.model.Participant;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository {

    List<Participant> findAll();

    Page<Participant> findAll(Pageable page);

    Participant save(Participant entity);

    void deleteByLogin(String login);

    Optional<Participant> findByLogin(String login);
}
