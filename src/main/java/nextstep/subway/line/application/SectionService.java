package nextstep.subway.line.application;

import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional(readOnly = true)
    public List<Section> findAllSections() {
        return sectionRepository.findAll();
    }
}
