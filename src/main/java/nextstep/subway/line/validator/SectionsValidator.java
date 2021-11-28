package nextstep.subway.line.validator;

import java.util.List;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.StreamUtils;

public class SectionsValidator {
    private static final String CAN_NOT_DELETE_STATION_WHEN_ONLY_ONE_SECTIONS_MESSAGE = "노선의 구간이 1개인 경우, 지하철 역을 삭제 할 수 없습니다.";
    private static final String CAN_NOT_DELETE_WHEN_NO_EXIST_STATION = "노선에 존재하지 않는 역입니다.";
    private static final String NOT_EXIST_STATION = "존재하지 않는 지하철 역입니다.";
    private static final String INVALID_ADDABLE_SECTION = "등록할 수 없는 구간 입니다.";
    private static final String ALREADY_HAS_SECTION = "이미 등록된 구간 입니다.";
    private static final String IS_GREATER_OR_EQUAL_DISTANCE = "새로운 구간의 길이가 기존 구간길이보다 크거나 같습니다.";
    private static final int MIN_SIZE_WHEN_DELETE_STATION= 1;

    private SectionsValidator() {}

    public static void validateAddableStations(Sections sections, Section section) {
        if (sections.isEmpty()) {
            return;
        }

        validateAlreadyContainStations(sections, section);
        validateNoRetainStations(sections, section);
    }

    public static void validateAddableSectionDistance(Section section, Section middleSection) {
        if (section.isGreaterThanOrEqualDistanceTo(middleSection)) {
            throw new IllegalArgumentException(IS_GREATER_OR_EQUAL_DISTANCE);
        }
    }

    public static void validateHasOnlyOneSectionWhenRemoveStation(int size) {
        if (size == 1) {
            throw new IllegalArgumentException(CAN_NOT_DELETE_STATION_WHEN_ONLY_ONE_SECTIONS_MESSAGE);
        }
    }

    public static void validateNotIncludeStation(Sections sections, Station station) {
        if (!hasStation(sections, station)) {
            throw new IllegalArgumentException(CAN_NOT_DELETE_WHEN_NO_EXIST_STATION);
        }
    }

    public static void validateNoExistStationWhenDeleteStation(Sections sections, Station station) {
        if (!sections.findAllStations().contains(station)) {
            throw new IllegalArgumentException(NOT_EXIST_STATION);
        }
    }

    public static void validateHasOnlyOneSection(int size) {
        if (size == MIN_SIZE_WHEN_DELETE_STATION) {
            throw new IllegalArgumentException(CAN_NOT_DELETE_STATION_WHEN_ONLY_ONE_SECTIONS_MESSAGE);
        }
    }

    private static void validateAlreadyContainStations(Sections sections, Section section) {
        if (containStations(sections, section.getStations())) {
            throw new IllegalArgumentException(ALREADY_HAS_SECTION);
        }
    }

    private static void validateNoRetainStations(Sections sections, Section section) {
        if (!hasAnyRetainStation(sections, section.getStations())) {
            throw new IllegalArgumentException(INVALID_ADDABLE_SECTION);
        }
    }

    private static boolean containStations(Sections sections, List<Station> stations) {
        return sections.findAllStations().containsAll(stations);
    }

    private static boolean hasStation(Sections sections, Station station) {
        return sections.findAllStations().contains(station);
    }

    private static boolean hasAnyRetainStation(Sections sections, List<Station> stations) {
        return StreamUtils.anyMatch(sections.findAllStations(), stations::contains);
    }
}
