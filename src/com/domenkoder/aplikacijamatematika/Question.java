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
        }

        jLabel1.setText(a + " " + operator + " " + b + " = ");
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Seštevko | IZRAČUNAJ!");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 100)); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 318, 519, 152));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel2.setText("IZRAČUNAJ!");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 109, -1, -1));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 100)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(686, 318, 287, 152));

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
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(879, 556, 250, 150));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel3.setText("jLabel3");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 170, -1, -1));

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
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
