/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.domenkoder.aplikacijamatematika;

import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author domen
 */
public class Question extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Question.class.getName());

    private final List<String> operations;
    private final Random random = new Random();

    private Timer questionTimer;
    private int timeLeft = 15; // sekunde na vprašanje

    private int questionCount = 0;
    private static final int MAX_QUESTIONS = 20;

    private int a, b;
    private int wrongCount = 0, correctCount = 0;
    private String operator;
    private int correctResult;

    private final int[] correctPerLevel = new int[3]; // index 0 = level 1
    private final int[] wrongPerLevel = new int[3];
    private final int[] totalPerLevel = new int[3];

    private int level = 1;          // 1 = lahko, 2 = srednje, 3 = težko
    private int streakCorrect = 0;
    private int streakWrong = 0;

    private final int TOM_START_X = 30;
    private final int TOM_Y = 530;

    private int tomX = TOM_START_X;

    /**
     * Creates new form Question
     *
     * @param operations
     */
    public Question(List<String> operations) {
        initComponents();
        jTextField1.requestFocusInWindow();
        jLabel3.setText("Čas: 15");
        getRootPane().setDefaultButton(jButton1);
        this.operations = operations;

        generateTask();
        startTimer();

    }

    private void startTimer() {
        timeLeft = 15;
        jLabel3.setText("Čas: " + timeLeft);

        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }

        questionTimer = new Timer(1000, e -> {
            timeLeft--;
            jLabel3.setText("Čas: " + timeLeft);

            if (timeLeft <= 0) {
                questionTimer.stop();
                handleTimeout();
            }
        });

        questionTimer.start();
    }

    private void handleTimeout() {
        JOptionPane.showMessageDialog(this,
                "⏰ Čas je potekel!\nPravilen rezultat je: " + correctResult);

        wrongCount++;
        wrongPerLevel[level - 1]++;
        totalPerLevel[level - 1]++;
        streakWrong++;
        streakCorrect = 0;

        if (streakWrong == 2 && level > 1) {
            level--;
            streakWrong = 0;
        }

        nextQuestion();
    }

    private void nextQuestion() {
        questionCount++;

        if (questionCount >= MAX_QUESTIONS) {
            new Statistics(correctPerLevel, wrongPerLevel, totalPerLevel)
                    .setVisible(true);
            dispose();
            return;
        }

        generateTask();
        jTextField1.setText("");
        jTextField1.requestFocus();
        startTimer();
    }

    private int getMaxResultForLevel() {
        return switch (level) {
            case 1 ->
                20;
            case 2 ->
                50;
            default ->
                100;
        };
    }

    private void generateTask() {
        operator = operations.get(random.nextInt(operations.size()));
        int maxResult = getMaxResultForLevel();

        switch (operator) {

            case "+" -> {
                correctResult = random.nextInt(maxResult + 1);
                a = random.nextInt(correctResult + 1);
                b = correctResult - a;
            }

            case "-" -> {
                a = random.nextInt(maxResult + 1);
                b = random.nextInt(a + 1); //Da rezultat ne gre v minus
                correctResult = a - b;
            }

            case "*" -> {
                //Faktorji so omejeni da rezultat ne postane prevelik
                a = random.nextInt(level + 4) + 1;
                b = random.nextInt(maxResult / a + 1);
                correctResult = a * b;
            }

            case "/" -> {
                //Deljenje brez ostanja
                b = random.nextInt(level + 4) + 1;
                correctResult = random.nextInt(maxResult / b + 1);
                a = correctResult * b;
            }

            case "^" -> {
                int result;

                do {
                    a = random.nextInt(9) + 2;   // baza 2–10
                    b = random.nextInt(3) + 2;   // eksponent 2–4
                    result = (int) Math.pow(a, b);
                } while (result > 225);

                correctResult = result;
            }

            case "√" -> {
                // dovoljeni rezultati: 1–15 (15² = 225)
                correctResult = random.nextInt(15) + 1;
                a = correctResult * correctResult; // število pod korenom
            }

            case "!" -> {
                a = random.nextInt(6) + 1; // 1–6 (anything higher is stupidity)
                b = 0;

                int fact = 1;
                for (int i = 2; i <= a; i++) {
                    fact *= i;
                }

                correctResult = fact;
            }
        }

        switch (operator) {
            case "√" ->
                jLabel1.setText("√" + a + " = ");
            case "!" ->
                jLabel1.setText(a + "! = ");
            default ->
                jLabel1.setText(a + " " + operator + " " + b + " = ");
        }
    }

    private void checkButtonActionPerformed(java.awt.event.ActionEvent evt) {

        String input = jTextField1.getText().trim();

        int idx = level - 1;
        totalPerLevel[idx]++;

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vpiši odgovor!",
                    "Opozorilo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userAnswer;

        try {
            userAnswer = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Vnesi število!",
                    "Napaka",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userAnswer == correctResult) {
            questionTimer.stop();

            //TOM
            if (jLabelTom.getX() > TOM_START_X) {
                jLabelTom.setLocation(
                        jLabelTom.getX() - 25,
                        jLabelTom.getY()
                );
            }

            JOptionPane.showMessageDialog(rootPane, "✅ Pravilno!");
            correctCount++;

            streakCorrect++;
            streakWrong = 0;

            //za statistiko
            correctPerLevel[idx]++;

            if (streakCorrect == 3 && level < 3) {
                level++;
                streakCorrect = 0;
            }
        } else {
            questionTimer.stop();
            jLabelTom.setLocation(
                    jLabelTom.getX() + 25,
                    jLabelTom.getY()
            );
            JOptionPane.showMessageDialog(rootPane, "❌ Napačno! Pravilen rezultat je: " + correctResult);
            wrongCount++;

            streakWrong++;
            streakCorrect = 0;

            //za statistiko
            wrongPerLevel[idx]++;

            if (streakWrong == 2 && level > 1) {
                level--;
                streakWrong = 0;
            }
        }

        if (questionCount >= MAX_QUESTIONS) {
            new Statistics(
                    correctPerLevel,
                    wrongPerLevel,
                    totalPerLevel).setVisible(true);

            this.dispose();
            return;
        }

        questionTimer.stop();
        nextQuestion();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabelTom = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Seštevko | IZRAČUNAJ!");
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 100)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(155, 318, 519, 152);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("IZRAČUNAJ!");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(480, 109, 261, 64);

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 100)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        getContentPane().add(jTextField1);
        jTextField1.setBounds(686, 318, 287, 152);

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jButton1.setText("POTRDI");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(879, 556, 250, 150);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("jLabel3");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(920, 170, 150, 64);

        jLabelTom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/domenkoder/aplikacijamatematika/images/Tom_Cat.png"))); // NOI18N
        getContentPane().add(jLabelTom);
        jLabelTom.setBounds(30, 530, 200, 222);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/domenkoder/aplikacijamatematika/images/Jerry_Mouse.png"))); // NOI18N
        getContentPane().add(jLabel5);
        jLabel5.setBounds(780, 690, 50, 66);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/domenkoder/aplikacijamatematika/images/background.png"))); // NOI18N
        getContentPane().add(jLabel4);
        jLabel4.setBounds(0, -20, 1200, 800);

        jMenu1.setText("Navodila");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Vizitka");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Izhod");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(1216, 809));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        checkButtonActionPerformed(evt);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked

    }//GEN-LAST:event_jButton1MouseClicked

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        new Navodila().setVisible(true);
    }//GEN-LAST:event_jMenu1MouseClicked

    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
        JOptionPane.showMessageDialog(rootPane, "Seštevko v1.0 \n © Domen Koder, 2025 \nLicencirano pod MIT licenco.");
    }//GEN-LAST:event_jMenu2MouseClicked

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jMenu3MouseClicked

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed

    }//GEN-LAST:event_jMenu3ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed

    }//GEN-LAST:event_jTextField1KeyPressed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelTom;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
