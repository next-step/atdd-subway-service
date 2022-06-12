package nextstep.subway.favorite.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long memberId;
    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    private Station source;
    @ManyToOne(fetch = FetchType.EAGER,optional = false)
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
