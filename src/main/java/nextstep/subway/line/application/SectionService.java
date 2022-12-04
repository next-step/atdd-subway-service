package nextstep.subway.line.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.common.exception.NotFoundException;
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

	public Section findSectionByDownStation(Long stationId) {
		return sectionRepository.findByUpStationId(stationId)
			.orElseThrow(() -> new NotFoundException("해당하는 구간이 없습니다."));
	}

	public Section findSectionByUpStation(Long stationId) {
		return sectionRepository.findByDownStationId(stationId)
			.orElseThrow(() -> new NotFoundException("해당하는 구간이 없습니다."));
	}
}
