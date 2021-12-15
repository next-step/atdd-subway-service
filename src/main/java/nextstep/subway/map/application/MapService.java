package nextstep.subway.map.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.map.domain.Map;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapService {

    private final SectionRepository sectionRepository;

    public MapService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Map getMap() {
        List<Section> sections = sectionRepository.findAll();
        return Map.of(sections);
    }
}
