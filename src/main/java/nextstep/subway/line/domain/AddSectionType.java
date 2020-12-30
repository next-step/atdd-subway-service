package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.common.exception.AlreadyExistException;
import nextstep.subway.common.exception.NotFoundException;

@Getter
@RequiredArgsConstructor
public enum AddSectionType {
	BOTH_MATCH(true, true,
		(targetSection, sections) -> {
			throw new AlreadyExistException("이미 등록된 구간 입니다.");
		}),
	BOTH_NOT_MATCH(false, false,
		(targetSection, sections) -> {
			throw new NotFoundException("등록할 수 없는 구간 입니다.");
		}),
	UPSTATION_MATCH(true, false,
		(targetSection, sections) -> {
			sections.stream()
				.filter(it -> it.isEqualUpstation(targetSection.getUpStation()))
				.findFirst()
				.ifPresent(it -> it.updateUpStation(targetSection.getDownStation(), targetSection.getDistance()));
			sections.add(targetSection);
		}),
	DOWNSTATION_MATCH(false, true,
		(targetSection, sections) -> {
			sections.stream()
				.filter(it -> it.isEqualDownStation(targetSection.getDownStation()))
				.findFirst()
				.ifPresent(it -> it.updateDownStation(targetSection.getUpStation(), targetSection.getDistance()));
			sections.add(targetSection);
		});

	private final boolean isUpStationExisted;
	private final boolean isDownStationExisted;
	private final BiConsumer<Section, List<Section>> consumer;

	public static AddSectionType findAddSectionType(boolean isUpStation, boolean isDownStation) {
		return Arrays.stream(AddSectionType.values())
			.filter(addSectionType -> addSectionType.isUpStationExisted() == isUpStation
				&& addSectionType.isDownStationExisted() == isDownStation)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("해당되는 타입이 없습니다."));
	}

	public void addSection(Section targetSection, List<Section> sections) {
		this.consumer.accept(targetSection, sections);
	}

}
