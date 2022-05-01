package pl.cichy.enroller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "meetings")
public class Meeting implements Serializable {

    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int meetingId;

    private String title;

    private String date;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Participant> participants;

    public Meeting() {}

    public Meeting(String title) {
        participants = new LinkedList<>();
        this.title = title;
    }

    public Meeting(String title, String date) {
        participants = new LinkedList<>();
        this.title = title;
        this.date = date;
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }
}
