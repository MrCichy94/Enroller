package pl.cichy.enroller.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.cichy.enroller.model.Meeting;
import pl.cichy.enroller.model.repository.MeetingRepository;

@Repository
public interface SqlMeetingRepository extends MeetingRepository, JpaRepository<Meeting, Integer> {
}
