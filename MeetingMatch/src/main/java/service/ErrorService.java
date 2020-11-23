package service;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorService {

    private final static DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
    private final static String ERROR_LOG_PATTERN = "error_%s.log";

    public static void error(Exception e, String extraMessage) {
        e.printStackTrace();
        Exception n = new RuntimeException(extraMessage + e.getMessage());
        saveErrorLog(n);
        JOptionPane.showMessageDialog(null, n.getMessage());
    }

    private static void saveErrorLog(Exception e) {
        String fileName = String.format(ERROR_LOG_PATTERN, DATE_FORMATTER.format(new Date()));
        try {
            e.printStackTrace(new PrintWriter(new File(fileName)));
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Failed to create error log:");
            fileNotFoundException.printStackTrace();
        }
    }
}
