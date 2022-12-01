package nextstep.subway.line.domain;

class SectionsValidator {
    private static final int MIN_SIZE = 1;

    private SectionsValidator() {
    }

    static void validateAddNew(Section section, Stations stations) {
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
        throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
    }

    private static void validateAlreadyExisted(Section section, Stations stations) {
        if (section.isUpStationNotExisted(stations)) {
            return;
        }
        if (section.isDownStationNotExisted(stations)) {
            return;
        }
        throw new IllegalArgumentException("이미 등록된 구간 입니다.");
    }

    static void validateRemoveStation(Sections sections) {
        if (sections.size() <= MIN_SIZE) {
            throw new IllegalStateException("노선에는 최소 2개 이상의 역이 있어야 합니다.");
        }
    }
}
