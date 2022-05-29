package database;

import player.NeutralPlayer;

import javax.swing.*;
import java.util.List;

import static database.Constants.publicFont;
import static gui.guitools.Convert.dimensionConverter;

public class Logger {
    private static String log = "";

    public static void log(Acts act, List<NeutralPlayer> operators) {
        String actLog = "";
        switch (act) {
            case coup -> actLog = operators.get(0).getClass().getSimpleName() + " couped against " + operators.get(1).getClass().getSimpleName();
            case block -> actLog = operators.get(0).getClass().getSimpleName() + " claimed to block incoming act";
            case challenge -> actLog = operators.get(0).getClass().getSimpleName() + " challenged " + operators.get(1).getClass().getSimpleName();
            case income -> actLog = operators.get(0).getClass().getSimpleName() + " received income";
            case refund -> actLog = operators.get(0).getClass().getSimpleName() + " received refund";
            case exchange -> actLog = operators.get(0).getClass().getSimpleName() + " exchanged cards";
            case eliminate -> actLog = operators.get(0).getClass().getSimpleName() + " eliminated one of his cards";
            case foreignAid -> actLog = operators.get(0).getClass().getSimpleName() + " claims foreign aid";
            case dukeAct -> actLog = operators.get(0).getClass().getSimpleName() + " received tax";
            case assassinAct -> actLog = operators.get(0).getClass().getSimpleName() + " assassinated " + operators.get(1).getClass().getSimpleName();
            case captainAct -> actLog = operators.get(0).getClass().getSimpleName() + " ransomed " + operators.get(1).getClass().getSimpleName();
            case ambassadorAct -> actLog = operators.get(0).getClass().getSimpleName() + " changed cards";
        }
        if (log.equals("")) {
            log = actLog;
        } else {
            log = log + "\n" + actLog;
        }
    }

    public static JScrollPane getTextScrollable() {
        JTextPane textPane = new JTextPane();
        textPane.setText(log);
        textPane.setEditable(false);
        textPane.setFocusable(false);
        textPane.setFont(publicFont);
        textPane.setPreferredSize(dimensionConverter(30, 15));

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        return scrollPane;
    }
}
