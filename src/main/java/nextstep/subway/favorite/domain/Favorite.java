package nextstep.subway.favorite.domain;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Station target;


    public Favorite() {

    }

    public Favorite(Member member, Station source, Station target) {
        validInputCheck(member, source, target);
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Member member, Station source, Station target) {
        return new Favorite(member, source, target);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public boolean isSameMember(Member inputMember) {
        return this.member.equals(inputMember);
    }

    private void validInputCheck(Member member, Station source, Station target) {
        if (isNullInputDatas(member, source, target)) {
            throw new InputDataErrorException(InputDataErrorCode.THE_MEMBER_OR_SOURCE_OR_TARGET_IS_NULL);
        }

        if (isSameStations(source, target)) {
            throw new InputDataErrorException(InputDataErrorCode.THERE_IS_SAME_STATIONS);
        }
    }

    private boolean isSameStations(Station source, Station target) {
        return source.equals(target);
    }

    private boolean isNullInputDatas(Member member, Station source, Station target) {
        return member == null || source == null || target == null;
    }
}
