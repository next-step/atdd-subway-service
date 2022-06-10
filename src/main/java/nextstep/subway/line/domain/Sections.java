package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Station> getStations() {
        return Arrays.asList(new Station("강남역"), new Station("양재시민의숲역"), new Station("판교역"));
    }

    public List<Section> getSections() {
        return sections;
    }

}
