package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.Member;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "section_id")
    private Section section;

    public Favorite() {
    }

    private Favorite(Member member, Section section) {
        this.member = member;
        this.section = section;
    }

    public static Favorite of(Member member, Section section) {
        return new Favorite(member, section);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Section getSection() {
        return section;
    }
}
