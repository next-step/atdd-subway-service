package nextstep.subway.favorite.domain;

import nextstep.subway.common.domain.BaseEntity;
import nextstep.subway.member.domain.Member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long source;

    private Long target;

    @ManyToOne
    private Member member;

    public Long getId() {
        return id;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    protected Favorite() {
    }

    public Favorite(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public void changeMember(Member member) {
        member.getFavorites().add(this);
        this.member = member;
    }
}
