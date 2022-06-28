package nextstep.subway.favorite.domain;

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
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station target;

    protected Favorite() {

    }

    public Favorite(Member member, Station source, Station target) {
        validMember(member);
        validStation(source, target);
        this.member = member;
        this.source = source;
        this.target = target;
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

    public long getId() {
        return id;
    }

    public void validMember(Member member) {
        if (isEmptyMember(member)) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }
    }

    private void validStation(Station source, Station target) {
        if (isSourceOrTargetEmpty(source, target)) {
            throw new IllegalArgumentException("역이 비어있으면 즐겨찾기를 등록할 수 없습니다.");
        }

        if (source.equals(target)) {
            throw new IllegalArgumentException("역이 같은 경우에는 즐겨찾기를 등록할 수 없습니다.");
        }
    }

    private boolean isSourceOrTargetEmpty(Station source, Station target) {
        return isEmptyStation(source) || isEmptyStation(target);
    }

    private boolean isEmptyMember(Member member) {
        return member == null;
    }

    private boolean isEmptyStation(Station station) {
        return station == null;
    }
}
