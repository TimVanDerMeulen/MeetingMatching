package service;

import com.google.gson.Gson;
import data.AttendeeList;
import data.Config;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataService {

    private final Config config;
    private Gson jsonMapper = new Gson();

    public DataService(Config config) {
        this.config = config;
    }

    public void createAllNeededFilesAndFolders() {
        assurePathExists(config.getWorkingDirectory(), false);
        if (assurePathExists(config.getOldDataFile(), true))
            saveData(new HashMap<>());
    }

    public Map<String, List<String>> loadOldData() {
        try {
            return jsonMapper.fromJson(Files.readString(getOldDataFile().toPath()), HashMap.class);
        } catch (IOException e) {
            ErrorService.error(e, "Fehler beim Laden der alten Daten: ");
        }
        return new HashMap<>();
    }

    public void saveData(Map<String, List<String>> data) {
        try {
            PrintWriter printWriter = new PrintWriter(getOldDataFile());
            printWriter.println(jsonMapper.toJson(data));

            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            ErrorService.error(e, "Fehler beim Speichern der Daten: ");
        }
    }

    public FileTime dateOfLastSave() {
        try {
            return Files.getLastModifiedTime(getOldDataFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AttendeeList readCsv(File file) {
        AttendeeList attendees = new AttendeeList();
        try {
            boolean isFirstLine = true;

            Reader in = new FileReader(file);
            for (CSVRecord record : CSVFormat.DEFAULT.withDelimiter(';').parse(in)) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                attendees.add(record.get(0), record.get(1));
            }
        } catch (IOException e) {
            ErrorService.error(e, "Fehler beim Einlesen der Datei! Wurde vielleicht kein ';' als Trennzeichen angegeben? Fehlermeldung: ");
        }
        return attendees;
    }

    public void saveAsCsv(String[] header, String[][] rows) throws IOException {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
        jFileChooser.showOpenDialog(null);

        if (jFileChooser.getSelectedFile() == null)
            throw new IllegalStateException("Canceled!");

        File target = jFileChooser.getSelectedFile();
        if (!target.getAbsolutePath().endsWith(".csv"))
            target = new File(target.getAbsolutePath() + ".csv");

        System.out.println("Saving csv to " + target.getAbsolutePath().toString());

        BufferedWriter writer = Files.newBufferedWriter(target.toPath());

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader(header));

        for (String[] row : rows)
            csvPrinter.printRecord(row);

        csvPrinter.flush();
        writer.close();
    }

    private File getOldDataFile() {
        return new File(config.getOldDataFile());
    }

    // returns if the path had to be created
    private boolean assurePathExists(String path, boolean isFile) {
        File target = new File(path);
        try {
            return isFile ? target.createNewFile() : target.mkdirs();
        } catch (IOException e) {
            ErrorService.error(e, "Fehler beim Erstellen der benötigten Pfade: ");
        }
        return false;
    }
}
