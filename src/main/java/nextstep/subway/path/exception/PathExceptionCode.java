package nextstep.subway.path.exception;

public enum PathExceptionCode {

    CANNOT_GENERATE_STATION_GRAPH("The station graph could not be generated."),
    CANNOT_EQUALS_SOURCE_TARGET("The source and target cannot be the same."),
    NO_LINES_CONTAINING_STATION("There are no lines containing this station."),
    NOT_CONNECTED_SOURCE_AND_TARGET("The source and target are not connected.");

    private String title = "[ERROR] ";
    private String message;

    PathExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return title + message;
    }
}
