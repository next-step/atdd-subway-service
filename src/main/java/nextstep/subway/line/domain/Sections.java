package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();


    public void add(Line line, Station upStation, Station downStation, int distance) {
        sections.add(new Section(line, upStation, downStation, distance));
    }

    public List<Section> getSections() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return sections;
    }

    public Station findUpStation() {
        Station upStation = sections.get(0).getUpStation();
        while (upStation != null) {
            Station finalUpStation = upStation;
            Optional<Section> findSection = sections.stream()
                    .filter(section -> section.getDownStation() == finalUpStation)
                    .findFirst();
            if (!findSection.isPresent()) {
                break;
            }
            upStation = findSection.get().getUpStation();
        }

        return upStation;
    }
}
