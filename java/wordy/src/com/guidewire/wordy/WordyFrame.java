package com.guidewire.wordy;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

/**
 * This code was auto-generated from JFormDesigner, so you can ignore the entire file, especially the portions within the
 * "DO NOT MODIFY" sections. :-)
 */
public class WordyFrame extends JFrame {

  private IWordy _wordy;
  private List<JLabel> _cellLabels;

  public WordyFrame(IWordy wordy) {
    _wordy = wordy;
    initComponents();
    initCellLabelList();
    generateNewBoard();
  }

  /**
   * Store the JLabels in list for easy access by the board-filling method
   */
  private void initCellLabelList() {
    _cellLabels = Arrays.asList(_cell1, _cell2, _cell3, _cell4, _cell5, _cell6, _cell7, _cell8, _cell9, _cell10,
            _cell11, _cell12, _cell13, _cell14, _cell15, _cell16);
  }

  private void generateNewBoard() {
    IBoard board = _wordy.generateNewBoard();
    fillBoardWith(board);
  }

  private void fillBoardWith(IBoard board) {
    for (int row = 0; row < 4; row++) {
      for (int column = 0; column < 4; column++) {
        _cellLabels.get(row * 4 + column).setText(" " + board.getCell(row, column));
      }
    }
  }

  private void computeScore() {
    int score = _wordy.scoreWords(parseWords());
    _pointsLabel.setText(score + " Point(s)");
  }

  private List<String> parseWords() {
    String wordText = _wordListTextArea.getText();
    return Arrays.asList(wordText.split("\n"));
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    _gridLabel = new JLabel();
    _wordListLabel = new JLabel();
    _boardPanel = new JPanel();
    _cell1 = new JLabel();
    _cell2 = new JLabel();
    _cell3 = new JLabel();
    _cell4 = new JLabel();
    _cell5 = new JLabel();
    _cell6 = new JLabel();
    _cell7 = new JLabel();
    _cell8 = new JLabel();
    _cell9 = new JLabel();
    _cell10 = new JLabel();
    _cell11 = new JLabel();
    _cell12 = new JLabel();
    _cell13 = new JLabel();
    _cell14 = new JLabel();
    _cell15 = new JLabel();
    _cell16 = new JLabel();
    _wordListScrollPane = new JScrollPane();
    _wordListTextArea = new JTextArea();
    _scorePanel = new JPanel();
    _yourScoreLabel = new JLabel();
    _pointsLabel = new JLabel();
    _generateNewBoard = new JButton();
    _computeScore = new JButton();

    //======== this ========
    setTitle("It's Wordy!");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());
    ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{0, 9, 0, 0};
    ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{12, 5, 0, 5, 0, 5, 0, 5, 0, 0};
    ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{0.0, 0.0, 1.0, 1.0E-4};
    ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

    //---- _gridLabel ----
    _gridLabel.setText("The Wordy Grid of Letters");
    contentPane.add(_gridLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));

    //---- _wordListLabel ----
    _wordListLabel.setText("Words You've Found:");
    contentPane.add(_wordListLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 5, 0, 0), 0, 0));

    //======== _boardPanel ========
    {
      _boardPanel.setBorder(new CompoundBorder(
              new EtchedBorder(),
              new EmptyBorder(4, 4, 4, 4)));
      _boardPanel.setLayout(new GridLayout(4, 4, 5, 5));

      //---- _cell1 ----
      _cell1.setText("text");
      _cell1.setAlignmentX(0.5F);
      _cell1.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell1.setFont(_cell1.getFont().deriveFont(
              _cell1.getFont().getStyle() | Font.BOLD, _cell1.getFont().getSize() + 4f));
      _boardPanel.add(_cell1);

      //---- _cell2 ----
      _cell2.setText("text");
      _cell2.setAlignmentX(0.5F);
      _cell2.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell2.setFont(_cell2.getFont().deriveFont(
              _cell2.getFont().getStyle() | Font.BOLD, _cell2.getFont().getSize() + 4f));
      _boardPanel.add(_cell2);

      //---- _cell3 ----
      _cell3.setText("text");
      _cell3.setAlignmentX(0.5F);
      _cell3.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell3.setFont(_cell3.getFont().deriveFont(
              _cell3.getFont().getStyle() | Font.BOLD, _cell3.getFont().getSize() + 4f));
      _boardPanel.add(_cell3);

      //---- _cell4 ----
      _cell4.setText("text");
      _cell4.setAlignmentX(0.5F);
      _cell4.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell4.setFont(_cell4.getFont().deriveFont(
              _cell4.getFont().getStyle() | Font.BOLD, _cell4.getFont().getSize() + 4f));
      _boardPanel.add(_cell4);

      //---- _cell5 ----
      _cell5.setText("text");
      _cell5.setAlignmentX(0.5F);
      _cell5.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell5.setFont(_cell5.getFont().deriveFont(
              _cell5.getFont().getStyle() | Font.BOLD, _cell5.getFont().getSize() + 4f));
      _boardPanel.add(_cell5);

      //---- _cell6 ----
      _cell6.setText("text");
      _cell6.setAlignmentX(0.5F);
      _cell6.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell6.setFont(_cell6.getFont().deriveFont(
              _cell6.getFont().getStyle() | Font.BOLD, _cell6.getFont().getSize() + 4f));
      _boardPanel.add(_cell6);

      //---- _cell7 ----
      _cell7.setText("text");
      _cell7.setAlignmentX(0.5F);
      _cell7.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell7.setFont(_cell7.getFont().deriveFont(
              _cell7.getFont().getStyle() | Font.BOLD, _cell7.getFont().getSize() + 4f));
      _boardPanel.add(_cell7);

      //---- _cell8 ----
      _cell8.setText("text");
      _cell8.setAlignmentX(0.5F);
      _cell8.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell8.setFont(_cell8.getFont().deriveFont(
              _cell8.getFont().getStyle() | Font.BOLD, _cell8.getFont().getSize() + 4f));
      _boardPanel.add(_cell8);

      //---- _cell9 ----
      _cell9.setText("text");
      _cell9.setAlignmentX(0.5F);
      _cell9.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell9.setFont(_cell9.getFont().deriveFont(
              _cell9.getFont().getStyle() | Font.BOLD, _cell9.getFont().getSize() + 4f));
      _boardPanel.add(_cell9);

      //---- _cell10 ----
      _cell10.setText("text");
      _cell10.setAlignmentX(0.5F);
      _cell10.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell10.setFont(_cell10.getFont().deriveFont(
              _cell10.getFont().getStyle() | Font.BOLD, _cell10.getFont().getSize() + 4f));
      _boardPanel.add(_cell10);

      //---- _cell11 ----
      _cell11.setText("text");
      _cell11.setAlignmentX(0.5F);
      _cell11.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell11.setFont(_cell11.getFont().deriveFont(
              _cell11.getFont().getStyle() | Font.BOLD, _cell11.getFont().getSize() + 4f));
      _boardPanel.add(_cell11);

      //---- _cell12 ----
      _cell12.setText("text");
      _cell12.setAlignmentX(0.5F);
      _cell12.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell12.setFont(_cell12.getFont().deriveFont(
              _cell12.getFont().getStyle() | Font.BOLD, _cell12.getFont().getSize() + 4f));
      _boardPanel.add(_cell12);

      //---- _cell13 ----
      _cell13.setText("text");
      _cell13.setAlignmentX(0.5F);
      _cell13.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell13.setFont(_cell13.getFont().deriveFont(
              _cell13.getFont().getStyle() | Font.BOLD, _cell13.getFont().getSize() + 4f));
      _boardPanel.add(_cell13);

      //---- _cell14 ----
      _cell14.setText("text");
      _cell14.setAlignmentX(0.5F);
      _cell14.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell14.setFont(_cell14.getFont().deriveFont(
              _cell14.getFont().getStyle() | Font.BOLD, _cell14.getFont().getSize() + 4f));
      _boardPanel.add(_cell14);

      //---- _cell15 ----
      _cell15.setText("text");
      _cell15.setAlignmentX(0.5F);
      _cell15.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell15.setFont(_cell15.getFont().deriveFont(
              _cell15.getFont().getStyle() | Font.BOLD, _cell15.getFont().getSize() + 4f));
      _boardPanel.add(_cell15);

      //---- _cell16 ----
      _cell16.setText("text");
      _cell16.setAlignmentX(0.5F);
      _cell16.setBorder(new CompoundBorder(
              new BevelBorder(BevelBorder.RAISED),
              new EmptyBorder(4, 4, 4, 4)));
      _cell16.setFont(_cell16.getFont().deriveFont(
              _cell16.getFont().getStyle() | Font.BOLD, _cell16.getFont().getSize() + 4f));
      _boardPanel.add(_cell16);
    }
    contentPane.add(_boardPanel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));

    //======== _wordListScrollPane ========
    {
      _wordListScrollPane.setViewportView(_wordListTextArea);
      _wordListTextArea.setFont(_wordListTextArea.getFont().deriveFont(
              _wordListTextArea.getFont().getStyle() | Font.BOLD));
    }
    contentPane.add(_wordListScrollPane, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

    //======== _scorePanel ========
    {
      _scorePanel.setLayout(new FlowLayout(FlowLayout.LEADING));

      //---- _yourScoreLabel ----
      _yourScoreLabel.setText("Your Score:");
      _scorePanel.add(_yourScoreLabel);

      //---- _pointsLabel ----
      _pointsLabel.setText("0 points");
      _pointsLabel.setFont(_pointsLabel.getFont().deriveFont(_pointsLabel.getFont().getStyle() | Font.BOLD));
      _scorePanel.add(_pointsLabel);
    }
    contentPane.add(_scorePanel, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));

    //---- _generateNewBoard ----
    _generateNewBoard.setText("Generate New Board");
    _generateNewBoard.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        generateNewBoard();
      }
    });
    contentPane.add(_generateNewBoard, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));

    //---- _computeScore ----
    _computeScore.setText("Score Me");
    _computeScore.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        computeScore();
      }
    });
    contentPane.add(_computeScore, new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));
    setSize(500, 450);
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JLabel _gridLabel;
  private JLabel _wordListLabel;
  private JPanel _boardPanel;
  private JLabel _cell1;
  private JLabel _cell2;
  private JLabel _cell3;
  private JLabel _cell4;
  private JLabel _cell5;
  private JLabel _cell6;
  private JLabel _cell7;
  private JLabel _cell8;
  private JLabel _cell9;
  private JLabel _cell10;
  private JLabel _cell11;
  private JLabel _cell12;
  private JLabel _cell13;
  private JLabel _cell14;
  private JLabel _cell15;
  private JLabel _cell16;
  private JScrollPane _wordListScrollPane;
  private JTextArea _wordListTextArea;
  private JPanel _scorePanel;
  private JLabel _yourScoreLabel;
  private JLabel _pointsLabel;
  private JButton _generateNewBoard;
  private JButton _computeScore;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
