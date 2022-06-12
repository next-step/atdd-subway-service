package nextstep.subway.favorite.domain;

import java.util.Objects;
import nextstep.subway.station.domain.Station;

public class Favorite {
    private Long id;
    private Long memberId;
    private Station source;
    private Station target;

    protected Favorite(){
        //For JPA Entity
    }

    public Favorite(Long memberId, Station source, Station target) {
        if(Objects.isNull(memberId) || Objects.isNull(source) || Objects.isNull(target)){
            throw new IllegalArgumentException("필수값이 누락되어 즐겨찾기를 생성할 수 없습니다.");
        }
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
