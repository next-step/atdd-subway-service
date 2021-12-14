package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import sun.rmi.runtime.Log;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "favorite")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source")
    private Long source;

    @Column(name = "target")
    private Long target;

    @Column(name = "member_id")
    private Long memberId;

    protected Favorite() {

    }

    private Favorite(Long source, Long target, Long memberId) {
        validateCorrectSourceAndTarget(source, target);
        this.source = source;
        this.target = target;
        this.memberId = memberId;
    }

    public static Favorite of(Long source, Long target, Long memberId) {
        Favorite favorite = new Favorite(source, target, memberId);
        return favorite;
    }

    private void validateCorrectSourceAndTarget(Long source, Long target) {
        if(source == target) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public List<Long> getStations() {
        return Arrays.asList(source, target);
    }

}
