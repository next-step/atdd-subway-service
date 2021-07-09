package nextstep.subway.station.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.domain.Section;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "downStation")
    private List<Section> upSections = new ArrayList<>();

    @OneToMany(mappedBy = "upStation")
    private List<Section> downSections = new ArrayList<>();

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) &&
                Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public List<Section> getUpSections() {
        return upSections;
    }

    public void addUpSection(Section upSection) {
        if (!upSections.contains(upSection)) {
            this.upSections.add(upSection);
        }
    }

    public List<Section> getDownSections() {
        return downSections;
    }

    public void setDownSection(Section downSection) {
        if (!downSections.contains(downSection)) {
            this.downSections.add(downSection);
        }
    }
}
