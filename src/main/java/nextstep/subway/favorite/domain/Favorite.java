package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Station target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    protected Favorite() {
    }

    public Favorite(Station departureStation, Station arrivalStation, Member member) {
        validate(departureStation, arrivalStation, member);
        this.source = departureStation;
        this.target = arrivalStation;
        this.member = member;
    }

    private void validate(Station departureStation, Station arrivalStation, Member member) {
        if (departureStation == null || arrivalStation == null || member == null) {
            throw new IllegalArgumentException("필수값 누락입니다.");
        }
    }

    public void changeMember(Member member) {
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return StationResponse.of(source);
    }

    public StationResponse getTarget() {
        return StationResponse.of(target);
    }

    public Member getMember() {
        return member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

