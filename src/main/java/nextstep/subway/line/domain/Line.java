package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private OverFare overFare;

    @Embedded
    private SectionGroup sections = new SectionGroup(new ArrayList<>());

    /**
     * 생성자
     */
    protected Line() {}

    public Line(String name, String color) {
        verifyAvailable(name, color);
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, int overFare) {
        this(name, color);
        this.overFare = new OverFare(overFare);
    }

    public Line(Long id, String name, String color) {
        this(name, color);
        this.id = id;
    }

    public Line(Long id, String name, String color, int overFare) {
        this(id, name, color);
        this.overFare = new OverFare(overFare);
    }

    public Line(Long id, String name, String color, SectionGroup sections) {
        this(id, name, color);
        this.sections = sections;
    }

    private static void verifyAvailable(String name, String color) {
        if (Strings.isBlank(name) || Strings.isBlank(color)) {
            throw new IllegalArgumentException("노선 정보가 충분하지 않습니다.");
        }
    }

    /**
     * 비즈니스 메소드
     */
    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        if (Objects.isNull(section.getLine())) {
            section.setLine(this);
        }
        sections.addSection(section);
    }

    public void removeSection(Station station) {
        sections.removeSection(this, station);
    }

    public List<Station> findStationsOrderUpToDown() {
        return sections.findStationsOrderUpToDown();
    }

    /**
     * 기타 메소드
     */
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public SectionGroup getSections() { return sections; }

    public OverFare getOverFare() {
        return overFare;
    }
}
