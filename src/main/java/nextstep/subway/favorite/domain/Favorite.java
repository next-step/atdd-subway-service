package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Favorite(Member member, Station sourceStation, Station targetStation) {
    }

    public Member getMember() {
        return null;
    }

    public Station getSource() {
        return null;
    }

    public Station getTarget() {
        return null;
    }
}
