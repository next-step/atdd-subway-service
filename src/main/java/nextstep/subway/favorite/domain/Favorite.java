package nextstep.subway.favorite.domain;

import javax.persistence.CascadeType;
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
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station target;

    public static class Builder {
        private Member member;
        private Station source;
        private Station target;


        public Builder() {
        }

        public Builder member(Member member) {
            this.member = member;
            return this;
        }

        public Builder source(Station station) {
            this.source = station;
            return this;
        }

        public Builder target(Station station) {
            this.target = station;
            return this;
        }

        public Favorite build() {
            validateBuild();
            return new Favorite(member, target, source);
        }

        private void validateBuild() {
            if (member == null) {
                throw new IllegalArgumentException("즐겨찾기에 멤버는 필수 입니다");
            }
            if (source == null) {
                throw new IllegalArgumentException("즐겨찾기에 출발역은 필수 입니다");
            }
            if (target == null) {
                throw new IllegalArgumentException("즐겨찾기에 도착역은 필수 입니다");
            }
        }
    }

    protected Favorite() {
    }

    public Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
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

}
