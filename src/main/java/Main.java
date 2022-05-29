import database.JsonOperator;
import gui.Start;

import javax.swing.*;
import java.io.IOException;

import static database.CheckMeta.checkImages;
import static database.CheckMeta.checkOst;
import static database.Constants.theme;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(theme);
        checkImages();
        checkOst();

        new JsonOperator();
        new Start();
    }
}
