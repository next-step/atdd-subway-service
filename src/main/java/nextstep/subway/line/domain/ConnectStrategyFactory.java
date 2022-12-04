package nextstep.subway.line.domain;

import java.util.List;
import java.util.Optional;

import nextstep.subway.line.domain.strategy.DefaultStationConnectStrategy;
import nextstep.subway.line.domain.strategy.DownStationConnectStrategy;
import nextstep.subway.line.domain.strategy.SectionConnectStrategy;
import nextstep.subway.line.domain.strategy.UpStationConnectStrategy;

public class ConnectStrategyFactory {

	private ConnectStrategyFactory() {
	}

	public static SectionConnectStrategy decide(Section newSection, List<Section> existingSections) {
		Optional<Section> sameUpStationSection = sameUpStationSection(newSection, existingSections);
		if (sameUpStationSection.isPresent()) {
			return new UpStationConnectStrategy(sameUpStationSection.get());
		}
		Optional<Section> sameDownStationSection = sameDownStationSection(newSection, existingSections);
		if (sameDownStationSection.isPresent()) {
			return new DownStationConnectStrategy(sameDownStationSection.get());
		}
		return new DefaultStationConnectStrategy();
	}

	private static Optional<Section> sameDownStationSection(Section newSection, List<Section> existingSections) {
		return existingSections.stream()
			.filter(it -> it.isSameDownStation(newSection))
			.findFirst();
	}

		private static Optional<Section> sameUpStationSection(Section newSection, List<Section> existingSections) {
		return existingSections.stream()
			.filter(it -> it.isSameUpStation(newSection))
			.findFirst();
	}
}
