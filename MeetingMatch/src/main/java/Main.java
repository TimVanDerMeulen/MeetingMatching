import com.google.gson.Gson;
import data.Config;
import service.DataService;
import ui.MainWindow;

public class Main {

    public static void main(String[] args) {
        Gson gson = new Gson();

        Config config = new Config();
        System.out.println(config);

        DataService dataService = new DataService(config);
        dataService.createAllNeededFilesAndFolders();

        new MainWindow(config, dataService);
    }

}
