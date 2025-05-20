package com.java.gui;

import java.awt.*;
import javax.swing.*;

public class RushHourGUI extends JFrame {
    private JPanel cards;
    private MainMenuPanel mainMenu;
    private LoadingPanel  loading;
    private SolvedPanel   solved;

    public RushHourGUI() {
        super("YuukaFinder: Rush Hour Solver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cards    = new JPanel(new CardLayout());
        mainMenu = new MainMenuPanel(this);
        loading  = new LoadingPanel();
        solved   = new SolvedPanel(this);

        cards.add(mainMenu, MainMenuPanel.MAIN);
        cards.add(loading,  LoadingPanel.LOADING);
        cards.add(solved,   SolvedPanel.SOLVED);

        setContentPane(cards);
    }

    public void showMain() {
        ((CardLayout)cards.getLayout()).show(cards, MainMenuPanel.MAIN);
    }

    public void showLoading() {
        ((CardLayout)cards.getLayout()).show(cards, LoadingPanel.LOADING);
    }

    public void showSolved() {
        ((CardLayout)cards.getLayout()).show(cards, SolvedPanel.SOLVED);
    }
}
