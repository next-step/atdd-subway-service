package nextstep.subway.line.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.station.domain.Station;

@Service
public class SectionService {

	private final SectionRepository sectionRepository;

	public SectionService(SectionRepository sectionRepository) {
		this.sectionRepository = sectionRepository;
	}

	@Transactional(readOnly = true)
	public List<Section> findSectionsToUpdate(Station upStation, Station downStation) {
		return sectionRepository.findAllByStations(upStation, downStation);
	}
}
