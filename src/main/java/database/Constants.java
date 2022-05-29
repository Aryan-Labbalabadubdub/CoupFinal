package database;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class Constants {
    public static final int incomeValue = 1;
    public static final int foreignAidValue = 2;
    public static final int coupPrice = 7;
    public static final int exchangePrice = 1;
    public static final int taxValue = 3;
    public static final int assassinationPrice = 3;
    public static final int stealValue = 2;

    public static final float cardW = 12;
    public static final float cardH = 17.16f;
    public static final float cardMaxW = 35;
    public static final float cardMaxH = 50.05f;
    public static final float stateMultiplier = 1.25f;

    public static final LookAndFeel theme = new FlatDarculaLaf();
    public static final float actionMultiplier = .9f;
    public static final Color formColor = new Color(59, 119, 188);
    public static final Color darkenColor = new Color(0f, 0f, 0f, 0.85f);
    public static final Color bloodRed = new Color(0.5412f, 0.0117f, 0.0117f, 1f);
    public static final Color violet = new Color(0.451f, 0.03f, 0.451f, 0.7f).brighter();
    public static final Color acidGreen = new Color(0.192f, 0.349f, 0f, 0.7f).brighter();
    public static final int frameRefreshRateMS = 4;
    public static final int BANK_CREDIT = 42;
    public static final String pathToBackground = "src/main/java/database/images/palace.png";
    public static final String pathToInfoCard = "src/main/java/database/images/infoCard.png";
    public static final String pathToCaptainCard = "src/main/java/database/images/Captain.png";
    public static final String pathToAmbassadorCard = "src/main/java/database/images/Ambassador.png";
    public static final String pathToAssassinCard = "src/main/java/database/images/Assassin.png";
    public static final String pathToCondessaCard = "src/main/java/database/images/Condessa.png";
    public static final String pathToDukeCard = "src/main/java/database/images/Duke.png";
    public static final String pathToCoinCard = "src/main/java/database/images/Coin.png";
    public static final String pathToCoupCard = "src/main/java/database/images/Coup.png";
    public static final String pathToCouperCard = "src/main/java/database/images/Couper.png";
    public static final String pathToExchangeCard = "src/main/java/database/images/Exchange.png";
    public static final String pathToForeignAidCard = "src/main/java/database/images/ForeignAid.png";
    public static final String pathToGrandMasterCard = "src/main/java/database/images/GrandMaster.png";
    public static final String pathToImposterCard = "src/main/java/database/images/Imposter.png";
    public static final String pathToIncomeCard = "src/main/java/database/images/Income.png";
    public static final String pathToParanoidCard = "src/main/java/database/images/Paranoid.png";
    public static final String pathToBackCard = "src/main/java/database/images/Back.png";
    public static final String pathToPlayerCard = "src/main/java/database/images/Player.png";
    public static final int cardCopies = 3;
    public static final String imagesFolderPath = "src/main/java/database/images";
    public static final String ostPath = "src/main/java/database/ost";
    public static final int imageFolderSizeInf = 29000000;
    public static final int imageFolderSizeSup = 30000000;
    public static final int ostSizeInf = 24500000;
    public static final int ostSizeSup = 25000000;
    public static final String imagesFolderURL = "https://www.dropbox.com/s/8zekfcqn9qvrgt6/images.zip?raw=1";
    public static final String ostURL = "https://www.dropbox.com/s/r04l7liwvgeyeop/Percival_Schuttenbach_The_Musty_Scent_Of_Fresh_P%C3%A2t%C3%A9.wav?raw=1";
    public static final int OPACITY = 50;
    public static final String vivaldiUrl = "https://www.webpagepublicity.com/free-fonts/v/Vivaldi%20Italic.ttf";
    public static final Font publicFont = new Font("SansSerif", Font.PLAIN, 12);
    public static Map<String, Integer> actionCost = Map.ofEntries(
            Map.entry("Income", 0), Map.entry("ForeignAid", 0), Map.entry("Coup", coupPrice),
            Map.entry("Exchange", exchangePrice), Map.entry("Ambassador", 0), Map.entry("Assassin", assassinationPrice),
            Map.entry("Captain", 0), Map.entry("Condessa", 0), Map.entry("Duke", 0)
    );
    public static Map<String, Integer> cardRisk = Map.ofEntries(
            Map.entry("Ambassador", 45), Map.entry("Assassin", 40),
            Map.entry("Captain", 50), Map.entry("Condessa", 25), Map.entry("Duke", 50)
    );
    public static Font vivaldi;

    static {
        try {
            vivaldi = Font.createFont(Font.TRUETYPE_FONT, new URL(vivaldiUrl).openStream());
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(vivaldi);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static final Font vivaldiResized = vivaldi.deriveFont(Font.BOLD, 20);
    public static final Font vivaldiTiny = vivaldi.deriveFont(Font.PLAIN, 18);
    public static final Font vivaldiHuge = vivaldi.deriveFont(Font.BOLD, 24);

}

