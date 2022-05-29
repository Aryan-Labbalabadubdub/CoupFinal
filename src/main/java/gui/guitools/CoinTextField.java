package gui.guitools;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;

import static database.Constants.publicFont;
import static database.Constants.vivaldiTiny;
import static gui.guitools.ComponentPlace.fontAndPlace;
import static gui.guitools.Convert.dimensionConverter;

public class CoinTextField extends JTextField {
    private String title;
    private Integer content;
    private Color backgroundColor = new JList<String>().getBackground();

    public CoinTextField() {
    }

    public CoinTextField(String text) {
        super(text);
    }

    public CoinTextField(int columns) {
        super(columns);
    }

    public CoinTextField(String text, int columns, int content) {
        super(columns);
        setHorizontalAlignment(JTextField.CENTER);
        setText(String.valueOf(content));
        setTitle(text + "'s Pouch :");
        setContent(content);
    }

    public CoinTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
    }

    public PanelB packPanel() {
        PanelB container = new PanelB(null, new float[]{0, 0, 100, 100});
        container.setOpaque(true);
        container.setBackground(backgroundColor);

        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;

        JLabel title = new JLabel(getTitle());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setMinimumSize(dimensionConverter(18, 3));

        title.setFont(vivaldiTiny);
        setFont(publicFont);

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Thread updater = new Thread(() -> {
                    if (getText().length() > 1) {
                        setText(String.valueOf(getContent()));
                    } else if (getText().length() == 1) {
                        if (Character.isDigit(getText().charAt(0))) {
                            setContent(Integer.parseInt(getText()));
                        } else {
                            setText(String.valueOf(getContent()));
                        }
                    } else {
                        setContent(0);
                        setText(String.valueOf(0));
                    }
                });
                updater.setDaemon(true);
                updater.start();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        fontAndPlace(container, constraints, false, new Component[]{title, this});
        return container;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getContent() {
        return content;
    }

    public void setContent(Integer content) {
        this.content = content;
    }
}
