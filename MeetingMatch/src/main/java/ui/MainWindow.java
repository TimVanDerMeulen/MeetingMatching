package ui;

import data.Attendee;
import data.AttendeeList;
import data.Config;
import service.DataService;
import service.ErrorService;
import service.MatchService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class MainWindow extends JFrame {

    private final Config config;
    private final DataService dataService;
    private final MatchService matchService;

    private BorderLayout layout;

    private Map<String, List<String>> oldData;
    private AttendeeList attendees;
    private Map<String, List<String>> currentData;

    private Border thickPanelBorder = BorderFactory.createLineBorder(Color.black, 3);

    private final JScrollPane tableScroll = new JScrollPane();

    public MainWindow(Config config, DataService dataService) throws HeadlessException {
        super(config.getAppName());

        this.config = config;
        this.dataService = dataService;
        this.matchService = new MatchService();

        init();

        this.setVisible(true);
    }

    private void init() {
        initBasicSetup();
        initLayout();

        loadOldData();

        initContent();
    }

    private void initBasicSetup() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0, 0, screenSize.width, screenSize.height);
    }

    private void initLayout() {
        layout = new BorderLayout();
        layout.setHgap(5);
        layout.setVgap(2);

        this.setLayout(layout);
    }

    private void loadOldData() {
        this.oldData = dataService.loadOldData();
        if (this.oldData == null)
            this.oldData = new HashMap<>();

        this.currentData = oldData;
    }

    private void initContent() {

        initGreeting();

        displayFileSelectsAndActions();
        refreshTable();

        tableScroll.setBorder(getTitledBorder("Durchläufe"));
        Dimension minimumSize = Toolkit.getDefaultToolkit().getScreenSize();
        minimumSize.setSize(minimumSize.getWidth() / 3, minimumSize.getHeight() / 3);
        tableScroll.setMinimumSize(minimumSize);
    }

    private void initGreeting() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        topPanel.add(new JLabel("Hallo " + config.getUserName() + "!"));
        topPanel.add(new JLabel("Wilkommen bei " + config.getAppName() + "."));

        this.add(topPanel, BorderLayout.NORTH);
    }

    private void displayFileSelectsAndActions() {
        JPanel collabsablePanel = new JPanel();
        collabsablePanel.setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.VERTICAL;
        cons.weighty = 1;
        cons.gridy = 0;


        JPanel fileSelectAndActionPanel = getFileSelectionAndActionPanel();
        collabsablePanel.add(fileSelectAndActionPanel, cons);

        JToggleButton collabse = new JToggleButton("<");
        collabse.addActionListener(e -> {
            if (collabse.isSelected()) {
                collabse.setText(">");
                fileSelectAndActionPanel.setVisible(false);
            } else {
                collabse.setText("<");
                fileSelectAndActionPanel.setVisible(true);
            }
        });
        collabsablePanel.add(collabse, cons);


        this.add(collabsablePanel, BorderLayout.WEST);

    }

    private JPanel getFileSelectionAndActionPanel() {
        JPanel fileSelectAndActionPanel = new JPanel();
        fileSelectAndActionPanel.setLayout(new BoxLayout(fileSelectAndActionPanel, BoxLayout.Y_AXIS));

        fileSelectAndActionPanel.add(getFileSelects());

        fileSelectAndActionPanel.add(Box.createVerticalStrut(10));

        fileSelectAndActionPanel.add(getActions());
        Dimension dummySpace = new Dimension(0, 0);
        fileSelectAndActionPanel.add(new Box.Filler(dummySpace, dummySpace, dummySpace) {
            @Override
            public Dimension getPreferredSize() {
                return fileSelectAndActionPanel.getSize();
            }

            @Override
            public Dimension getMaximumSize() {
                Dimension size = fileSelectAndActionPanel.getSize();
                return new Dimension((int) size.getWidth(), (int) (size.getHeight() / 5));
            }
        });

        return fileSelectAndActionPanel;
    }

    private JPanel getFileSelects() {
        JPanel fileSelectPanel = new JPanel() {
            @Override
            public Dimension getMinimumSize() {
                return super.getPreferredSize();
            }
        };
        fileSelectPanel.setLayout(new BoxLayout(fileSelectPanel, BoxLayout.Y_AXIS));

        //if (!oldData.isEmpty()) {
        FileTime dateOfLastSave = dataService.dateOfLastSave();
        fileSelectPanel.add(createTitledFileChooser("Alte Daten" + (dateOfLastSave != null ? (" (vom " + dateOfLastSave + ")") : ""), e -> {
            config.setOldDataFile(((JFileChooser) e.getSource()).getSelectedFile().getAbsolutePath());
            loadOldData();
            refreshTable();
        }, config.getOldDataFile()));
        //}
        fileSelectPanel.add(Box.createVerticalStrut(10));
        fileSelectPanel.add(createTitledFileChooser("Aktuelle Teilnehmerliste", e -> {
            File selectedFile = ((JFileChooser) e.getSource()).getSelectedFile();
            config.setParticipantListFile(selectedFile.getAbsolutePath());
            attendees = dataService.readCsv(selectedFile);

            refreshTable();
        }, null));

        return fileSelectPanel;
    }

    private JPanel getActions() {
        JPanel actionsPanel = new JPanel();
        actionsPanel.setToolTipText("Nur die Speichern-Aktion überschreibt die Datei der bisherigen Durchläufe. Alle anderen Aktionen gelten nur bis zum Speichern oder Schließen.");
        //actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.weightx = 0.5;

        actionsPanel.setBorder(getTitledBorder("Aktionen"));

        cons.gridx = 0;
        cons.gridy = 0;
        JButton nextRun = new JButton("nächsten Durchlauf berechnen");
        nextRun.setToolTipText("Berechnet den nächsten Durchlauf und zeigt diesen an. Diese Aktion überschreibt keine Dateien!");
        nextRun.addActionListener(e -> {
            if (attendees == null) {
                JOptionPane.showMessageDialog(null, "Keine Teilnehmerliste geladen!");
                return;
            }
            matchService.addNextRun(currentData, attendees);
            if (!matchService.validateMatches(currentData, attendees)) {
                JOptionPane.showMessageDialog(null, "Die Validierung für den Durchlauf ist fehlgeschlagen!");
            }
            refreshTable();
        });
        actionsPanel.add(nextRun, cons);

        cons.gridx = 1;
        cons.gridy = 0;
        JButton saveAsCsv = new JButton("Speichern und als CSV exportieren");
        saveAsCsv.setToolTipText("Speichert den aktuellen Stand in der internen Datei für vergangene Durchläufe und exportiert als CSV. Der alte Stand wird dabei überschrieben!");
        saveAsCsv.addActionListener(e -> {
            try {
                String[] tableColumnHeader = getTableColumnHeader(currentData);
                String[][] rows = convertDataToRowFormat(currentData, tableColumnHeader.length);

                dataService.saveAsCsv(tableColumnHeader, rows);
                dataService.saveData(currentData);
            } catch (IllegalStateException ex) {
                // canceled by save as csv
            } catch (Exception ex) {
                ErrorService.error(ex, "Fehler beim Speichern: ");
            }
        });
        actionsPanel.add(saveAsCsv, cons);

        cons.gridx = 0;
        cons.gridy = 1;
        JButton clear = new JButton("Ganz neu beginnen");
        clear.setToolTipText("Vergisst alle geladenen Daten. Diese sind im Hintergrund jedoch noch vorhanden und können durch einen Neustart der Anwendung wiederhergestellt werden! Diese Aktion überschreibt keine Dateien!");
        clear.addActionListener(e -> {
            int i = JOptionPane.showConfirmDialog(null, "Bist du sicher? Nach dem Speichern sind alle bisherigen Daten verloren!");
            if (i == JOptionPane.OK_OPTION)
                currentData = new HashMap<>();
            refreshTable();
        });
        actionsPanel.add(clear, cons);

        cons.gridx = 1;
        cons.gridy = 1;
        JButton removeLastRun = new JButton("Letzen Durchlauf löschen");
        removeLastRun.setToolTipText("Vergisst den letzten Durchlauf. Diese Aktion überschreibt keine Dateien!");
        removeLastRun.addActionListener(e -> {
            for (String key : currentData.keySet()) {
                List<String> runs = currentData.get(key);
                if (runs != null && runs.size() > 0)
                    runs.remove(runs.size() - 1);
            }
            refreshTable();
        });
        actionsPanel.add(removeLastRun, cons);

        return actionsPanel;
    }

    private void refreshTable() {
        displayMatchingTable(attendees, currentData);
    }

    private JPanel createTitledFileChooser(String title, ActionListener onChange, String defaultValue) {
        JPanel panel = new JPanel();
        panel.setBorder(getTitledBorder(title));

        JFileChooser fileChooser = new JFileChooser(defaultValue);
        if (defaultValue != null) {
            File file = new File(defaultValue);
            if (file.exists() && file.isFile())
                fileChooser.setSelectedFile(file);
        }
        fileChooser.addActionListener(onChange);

        panel.add(fileChooser);
        return panel;
    }

    private Border getTitledBorder(String title) {
        return BorderFactory.createTitledBorder(thickPanelBorder, title);
    }

    private void displayMatchingTable(AttendeeList attendees, Map<String, List<String>> data) {
        String[] tableColumnHeader = getTableColumnHeader(data.isEmpty() ? 0 : data.values().stream().map(Collection::size).max(Integer::compareTo).get());

        String[][] rowData;
        if (attendees == null || attendees.isEmpty())
            rowData = convertDataToRowFormat(data, tableColumnHeader.length);
        else {
            if (data.keySet().size() < attendees.size()) {
                rowData = new String[attendees.size()][1];
                for (int i = 0; i < attendees.size(); i++)
                    rowData[i] = new String[]{attendees.get(i).getName()};
            } else {
                Map<String, List<String>> filteredData = new HashMap<>();
                for (Attendee attendee : attendees) {
                    List<String> oldRuns = data.get(attendee.getName());
                    filteredData.put(attendee.getName(), oldRuns != null ? oldRuns : new ArrayList<>());
                }
                rowData = convertDataToRowFormat(filteredData, tableColumnHeader.length);
            }
        }

        JTable table = new JTable(rowData, tableColumnHeader);

        tableScroll.setViewportView(table);
        this.add(tableScroll, BorderLayout.CENTER);
        table.setFillsViewportHeight(true);
    }

    private String[] getTableColumnHeader(Map<String, List<String>> data) {
        return getTableColumnHeader(data.isEmpty() ? 0 : data.values().stream().map(Collection::size).max(Integer::compareTo).get());
    }

    private String[] getTableColumnHeader(int runs) {
        String[] res = new String[runs + 1];
        res[0] = "Name";
        for (int i = 1; i <= runs; i++)
            res[i] = "D" + i;
        return res;
    }

    private String[][] convertDataToRowFormat(Map<String, List<String>> data, int columnCount) {
        String[][] res = new String[data.size()][columnCount];
        List<Map.Entry<String, List<String>>> sortedEntries = new ArrayList<>(data.entrySet());

        if (attendees != null)
            sortedEntries = sortedEntries.stream().sorted(Comparator.comparing(entry -> attendees.indexOf(attendees.find(entry.getKey())))).collect(Collectors.toList());
        else
            sortedEntries = sortedEntries.stream().sorted(Comparator.comparing(entry -> entry.getKey())).collect(Collectors.toList());

        int currentRowIndex = 0;
        for (Map.Entry<String, List<String>> entry : sortedEntries) {
            List<String> tmp = new ArrayList<>(entry.getValue());
            tmp.add(0, entry.getKey());
            // fill to max length
            while (tmp.size() < columnCount) {
                tmp.add("-");
                data.get(entry.getKey()).add("-"); // also adjust input
            }
            res[currentRowIndex++] = tmp.toArray(new String[0]);
        }
        return res;
    }
}
