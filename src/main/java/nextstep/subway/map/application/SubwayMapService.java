package nextstep.subway.map.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.map.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubwayMapService {

    private final SectionRepository sectionRepository;

    public SubwayMapService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public SubwayMap getMap() {
        List<Section> sections = sectionRepository.findAll();
        return SubwayMap.of(sections);
    }
}
