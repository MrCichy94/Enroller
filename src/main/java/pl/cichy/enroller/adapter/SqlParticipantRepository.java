package pl.cichy.enroller.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.cichy.enroller.model.Participant;
import pl.cichy.enroller.model.repository.ParticipantRepository;

@Repository
public interface SqlParticipantRepository extends ParticipantRepository, JpaRepository<Participant, Integer> {

    @Override
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM PARTICIPANTS where LOGIN=:login")
    void deleteByLogin(@Param("login") String login);
}
