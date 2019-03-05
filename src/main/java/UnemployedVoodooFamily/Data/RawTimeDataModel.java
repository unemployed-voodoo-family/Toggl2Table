package UnemployedVoodooFamily.Data;

import javafx.beans.property.SimpleStringProperty;

public class RawTimeDataModel {

    private SimpleStringProperty project;
    private SimpleStringProperty description;
    private SimpleStringProperty startDate;
    private SimpleStringProperty startTime;
    private SimpleStringProperty endDate;
    private SimpleStringProperty endTime;
    private SimpleStringProperty duration;

    /**
     * Creates a RawTimeDataModel object
     * Used to structure data for the corresponding TableView
     * @param project String with project name
     * @param description String with description
     * @param startDate String with start date
     * @param startTime String with start time
     * @param endDate String with end date
     * @param endTime String with end time
     * @param duration String with duration
     */
    public RawTimeDataModel(String project, String description, String startDate, String startTime,
                            String endDate, String endTime, String duration) {
        this.project = new SimpleStringProperty(project);
        this.description = new SimpleStringProperty(description);
        this.startDate = new SimpleStringProperty(startDate);
        this.startTime = new SimpleStringProperty(startTime);
        this.endDate = new SimpleStringProperty(endDate);
        this.endTime = new SimpleStringProperty(endTime);
        this.duration = new SimpleStringProperty(duration);
    }

    /**
     * Returns the project name
     * @return the project name
     */
    public String getProject() {
        return project.get();
    }

    /**
     * Returns the description
     * @return the description
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Returns the start date as a string
     * @return the start date as a string
     */
    public String getStartDate() {
        return startDate.get();
    }

    /**
     * Returns the start time as a string
     * @return the start time as a string
     */
    public String getStartTime() {
        return startTime.get();
    }

    /**
     * Returns the end date as a string
     * @return the end date as a string
     */
    public String getEndDate() {
        return endDate.get();
    }

    /**
     * Returns the end time as a string
     * @return the end time as a string
     */
    public String getEndTime() {
        return endTime.get();
    }

    /**
     * Returns the duration as a string
     * @return the duration as a string
     */
    public String getDuration() {
        return duration.get();
    }
}
