package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.Arrays;

public enum AddSectionType {

    UP {
        @Override
        protected boolean checkStation(Section section, Station upStation, Station downStation) {
            return section.getUpStation() == upStation;
        }

        @Override
        public void updateStation(Section section, Station upStation, Station downStation, int distance) {
            section.updateUpStation(downStation, distance);
        }
    },

    DOWN {
        @Override
        protected boolean checkStation(Section section, Station upStation, Station downStation) {
            return section.getDownStation() == downStation;
        }

        @Override
        public void updateStation(Section section, Station upStation, Station downStation, int distance) {
            section.updateDownStation(upStation, distance);
        }
    },

    NONE {
        @Override
        protected boolean checkStation(Section section, Station upStation, Station downStation) {
            return false;
        }

        @Override
        public void updateStation(Section section, Station upStation, Station downStation, int distance) {}
    };

    public static AddSectionType valueOf(Section section, Station upStation, Station downStation) {
        return Arrays.stream(values())
                .filter(addSectionType -> addSectionType.checkStation(section, upStation, downStation))
                .findFirst()
                .orElse(NONE);
    }

    protected abstract boolean checkStation(Section section, Station upStation, Station downStation);
    public abstract void updateStation(Section section, Station upStation, Station downStation, int distance);
}
