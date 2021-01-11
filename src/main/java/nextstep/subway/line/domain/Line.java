package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.path.domain.Money;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Convert(converter = MoneyConverter.class)
    private Money additionalFare;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color, Money additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    public Line(String name, String color, Station upStation, Station downStation, Money additionalFare, int distance) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public Money getAdditionalFare() {
        return additionalFare;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeSection(Line line, Station station) {
        sections.remove(line, station);
    }

    public static class MoneyConverter implements AttributeConverter<Money, Long> {

        @Override
        public Long convertToDatabaseColumn(Money attribute) {
            return attribute.getAmountLongValue();
        }

        @Override
        public Money convertToEntityAttribute(Long dbData) {
            return Money.won(dbData);
        }
    }


}
