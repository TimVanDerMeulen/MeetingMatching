package data;

public class Config {

    private String OLD_DATA_FILE_NAME = "oldData.json";

    private final String appName = "MeetingMatching";
    private final String userName = System.getProperty("user.name");
    private final String OS = System.getProperty("os.name");

    private String workingDirectory;
    private String oldDataFile;
    private String participantListFile;

    public Config() {
        init();
    }

    private void init() {

        setWorkingDirectoryForOs();
        oldDataFile = workingDirectory + OLD_DATA_FILE_NAME;
    }

    private void setWorkingDirectoryForOs() {
        String basePath;
        // Windows
        if (OS.toUpperCase().contains("WIN")) {
            basePath = System.getenv("AppData");
        }
        //Otherwise, we assume Linux or Mac
        else {
            basePath = System.getProperty("user.home");
        }
        workingDirectory = String.format("%s/%s/", basePath, appName);
    }

    public String getAppName() {
        return appName;
    }

    public String getOS() {
        return OS;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public String getUserName() {
        return userName;
    }

    public String getOldDataFile() {
        return oldDataFile;
    }

    public void setOldDataFile(String oldDataFile) {
        this.oldDataFile = oldDataFile;
    }

    public String getParticipantListFile() {
        return participantListFile;
    }

    public void setParticipantListFile(String participantListFile) {
        this.participantListFile = participantListFile;
    }

    @Override
    public String toString() {
        return "Config{" +System.lineSeparator() +
                "  userName='" + userName + '\'' + System.lineSeparator() +
                "  appName='" + appName + '\'' +System.lineSeparator() +
                "  OS='" + OS + '\'' +System.lineSeparator() +
                "  workingDirectory='" + workingDirectory + '\'' +System.lineSeparator() +
                "  oldDataFile='" + oldDataFile + '\'' +System.lineSeparator() +
                '}';
    }
}

