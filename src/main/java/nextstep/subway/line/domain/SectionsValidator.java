package nextstep.subway.line.domain;

public class SectionsValidator {
    protected static final String ALREADY_EXISTED_MESSAGE = "이미 등록된 구간 입니다.";
    protected static final String NOT_CONTAINS_ALL_MESSAGE = "등록할 수 없는 구간 입니다.";
    private static final int MIN_SIZE = 1;

    private SectionsValidator() {
    }

    public static void validateAddNew(Section section, Stations stations) {
        validateAlreadyExisted(section, stations);
        validateNotContainsAll(section, stations);
    }

    private static void validateNotContainsAll(Section section, Stations stations) {
        if (stations.isEmpty()) {
            return;
        }
        if (section.isUpStationExisted(stations)) {
            return;
        }
        if (section.isDownStationExisted(stations)) {
            return;
        }
        throw new RuntimeException(NOT_CONTAINS_ALL_MESSAGE);
    }

    private static void validateAlreadyExisted(Section section, Stations stations) {
        if (section.isUpStationNotExisted(stations)) {
            return;
        }
        if (section.isDownStationNotExisted(stations)) {
            return;
        }
        throw new RuntimeException(ALREADY_EXISTED_MESSAGE);
    }

    public static void validateRemoveStation(Sections sections) {
        if (sections.size() <= MIN_SIZE) {
            throw new RuntimeException();
        }
    }
}
