package nextstep.subway.favorite.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {
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

    public Favorite(Station source, Station target, Member member) {
        validateStation(source);
        validateStation(target);
        validateMember(member);
        this.source = source;
        this.target = target;
        this.member = member;
    }

    private void validateStation(Station station) {
        if (Objects.isNull(station)) {
            throw new IllegalArgumentException("역 데이터가 존재하지 않습니다.");
        }
    }

    private void validateMember(Member member) {
        if (Objects.isNull(member)) {
            throw new IllegalArgumentException("사용자 데이터가 존재하지 않습니다.");
        }
    }

    public void validateSameMember(Member member) {
        if (this.member != member) {
            throw new RuntimeException("사용자가 일치하지 않습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public Member getMember() {
        return member;
    }
}