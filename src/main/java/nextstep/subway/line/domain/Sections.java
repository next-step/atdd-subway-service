package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class Sections {
	public static final int SECTION_DELETE_MINIMUM_COUNT = 1;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private final List<Section> sections = new ArrayList<>();

	public Sections(Section section) {
		this.sections.add(section);
	}

	public static Sections of(Line line, Station upStation, Station downStation, int distance) {
		return new Sections(new Section(line, upStation, downStation, distance));
	}

	public void add(Section target) {

		boolean upStationExistStatus = isExistStation(target.getUpStation());
		boolean downStationExistStatus = isExistStation(target.getDownStation());
		validateAddSection(upStationExistStatus, downStationExistStatus);

		if (upStationExistStatus) {
			this.sections.stream()
				.filter(section -> section.isUpStation(target))
				.findFirst()
				.ifPresent(section -> section.updateUpStation(target));
			this.sections.add(target);
		}

		if (downStationExistStatus) {
			this.sections.stream()
				.filter(section -> section.isDownStation(target))
				.findFirst()
				.ifPresent(section -> section.updateDownStation(target));
			this.sections.add(target);
		}
	}

	public void delete(Station target) {
		validateDeleteSection(target);

		Section upSection = findUpSection(target);
		Section downSection = findDownSection(target);

		this.sections.remove(upSection);
		this.sections.remove(downSection);

		if (upSection != null && downSection != null) {
			this.sections.add(Section.of(upSection, downSection));
		}
	}

	public boolean isExistStation(Station station) {
		return this.sections.stream()
			.anyMatch(section -> section.contains(station));
	}

	public List<Station> stations() {
		if (this.sections.isEmpty()) {
			return Collections.emptyList();
		}

		Set<Station> stations = new LinkedHashSet<>();
		Station downStation = findUpStation();
		stations.add(downStation);

		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = this.sections.stream()
				.filter(it -> it.getUpStation() == finalDownStation)
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getDownStation();
			stations.add(downStation);
		}

		return new ArrayList<>(stations);
	}

	private Station findUpStation() {
		Station downStation = this.sections.get(0).getUpStation();
		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = this.sections.stream()
				.filter(it -> it.getDownStation() == finalDownStation)
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getUpStation();
		}

		return downStation;
	}

	private void validateAddSection(boolean upStationExistStatus, boolean downStationExistStatus) {
		if (upStationExistStatus && downStationExistStatus) {
			throw new InternalException("이미 존재하는 구간입니다.");
		}

		if (!upStationExistStatus && !downStationExistStatus) {
			throw new InternalException("등록할 수 없는 구간입니다.");
		}
	}

	private void validateDeleteSection(Station target) {
		if (this.sections.size() <= SECTION_DELETE_MINIMUM_COUNT) {
			throw new InternalException("남은 구간 하나는 제거할 수 없습니다.");
		}

		if (!stations().contains(target)) {
			throw new InternalException("노선에 등록되지 않은 전철역 입니다.");
		}
	}

	private Section findUpSection(Station target) {
		return this.sections.stream()
			.filter(section -> section.getUpStation().equals(target))
			.findFirst()
			.orElse(null);
	}

	private Section findDownSection(Station target) {
		return this.sections.stream()
			.filter(section -> section.getDownStation().equals(target))
			.findFirst()
			.orElse(null);
	}
}
