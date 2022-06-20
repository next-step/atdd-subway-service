package nextstep.subway.section.application;

import java.util.List;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.SectionRepository;

public class SectionService {
    private final SectionRepository sectionRepository;

    public SectionService(final SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public List<Section> findAllSections() {
        return sectionRepository.findAll();
    }
}
