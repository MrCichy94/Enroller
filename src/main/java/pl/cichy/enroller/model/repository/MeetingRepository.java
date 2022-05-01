package pl.cichy.enroller.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.cichy.enroller.model.Meeting;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository {

    List<Meeting> findAll();

    Page<Meeting> findAll(Pageable page);

    Optional<Meeting> findById(Integer id);

    Meeting save(Meeting entity);

    void deleteById(Integer id);

}
