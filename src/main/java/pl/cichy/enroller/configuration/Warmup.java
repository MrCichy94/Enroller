package pl.cichy.enroller.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pl.cichy.enroller.model.Meeting;
import pl.cichy.enroller.model.Participant;
import pl.cichy.enroller.model.repository.MeetingRepository;
import pl.cichy.enroller.model.repository.ParticipantRepository;

@Component
public class Warmup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(Warmup.class);

    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;

    public Warmup(final MeetingRepository meetingRepository,final ParticipantRepository participantRepository) {
        this.meetingRepository = meetingRepository;
        this.participantRepository = participantRepository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        logger.info("Application warmup after context refreshed");

        //default DB
        //password is only encrypted by the service, not class
        Participant participant1 = new Participant("Tomek", "123"); participantRepository.save(participant1);
        Participant participant2 = new Participant("Romek", "123"); participantRepository.save(participant2);
        Participant participant3 = new Participant("Atomek", "123"); participantRepository.save(participant3);

        Meeting meeting = new Meeting("Dreaming"); meetingRepository.save(meeting);
        Meeting meetingWitDate = new Meeting("Planning", "3rd April 2023"); meetingRepository.save(meetingWitDate);

    }
}
