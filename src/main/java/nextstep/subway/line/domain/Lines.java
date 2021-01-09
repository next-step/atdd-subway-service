package nextstep.subway.line.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Lines {
    private List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    public Sections findSections() {
        Sections sections = Sections.of();
        lines.stream()
                .flatMap(line -> line.getSections().stream())
                .forEach(sections::add);
        return sections;
    }

    public Station findStation(Long stationId) {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .filter(station -> station.getId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 출발역이나 도착역입니다."));
    }

    public Fee calculatedFee(LoginMember loginMember, Distance distance) {
        Fee maxFee = findMaxFee();
        Fee fee = maxFee.calculateBasicFee(distance);
        if (isEmpty(loginMember)) {
            return fee;
        }
        if (loginMember.isChild()) {
            return fee.calculateChildFee();
        }
        if (loginMember.isTeenager()) {
            return fee.calculateTeenagerFee();
        }
        return fee;
    }

    public List<Line> get() {
        return lines;
    }

    private Fee findMaxFee() {
        Optional<Fee> max = lines.stream().map(Line::getFee)
                .max(Comparator.comparingInt(Fee::get));
        return max.orElseGet(() -> Fee.ofWithOverFare(0));
    }

    private boolean isEmpty(LoginMember loginMember) {
        return loginMember == null;
    }
}
