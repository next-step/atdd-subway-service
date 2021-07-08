package nextstep.subway.line.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Sections findByUpStationOrDownStation(Station upStation, Station downStation) {
        List<Section> sections = sectionRepository.findByUpStationOrDownStation(upStation, downStation);
        return new Sections(sections);
    }
}
