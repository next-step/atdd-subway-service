package nextstep.subway.line.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
public class SectionService {
    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public List<Section> getSections(List<Long> stationIds) {
        List<Section> result = new ArrayList<>();
        for (int i = 0; i < stationIds.size() - 1; i++) {
            result.add(sectionRepository.findByUpStation_IdAndDownStation_Id(stationIds.get(i), stationIds.get(i + 1)));
        }
        return result;
    }
}
