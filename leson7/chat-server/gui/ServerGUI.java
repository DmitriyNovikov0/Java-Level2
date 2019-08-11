package leson7.chat.server.gui;

import leson7.chat.server.core.ChatServer;
import leson7.chat.server.core.ChatServerListener;
import leson7.chat.server.core.SqlClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
1 как сделать чтобы при нажатии на сервере стоп все клиенты отлогинивались
2 является ли Statement ResultSet'ом
3 рассказать о фишках идеи и пошаговом дебаге кода
4 в КлиентГУИ в методе онсокеттредстарт объект потока передаётся но он не запускается.
как он запускается или я что то упустил?
5 про Library и в принципе статические методы. Мы их используем как шаблон?
6 В двух словах про статические поля и методы, для закрепления
7 Можно ли отладчиком проверять работу потоков? у меня не срабатывает точка останова
8 join() можно только в мейне использовать? в обычном методе не срабатывает
9 как работает Library
10 как работает handleNonAuthMessage
11 как будет работать класс, если у него два интерфейса, у которых есть методы с одинаковым названием
12 как пользователям указывать ник, если есть только логин и пароль?
13 как пользоваться лямбда-выражениями?
14 влияет ли длина цепочки наследований на производительность программы?
15 есть стек и куча, есть ли ещё разделы в памяти?
16 как все эти знания закрепить и улучшить?
17 что делать если теории много, но на практике не понятно как её применять,
+ код с урока плохо укладывается в голове
18 в серверГУИ 85 строка зачем добавили свингУтилитис если и без них всё работает
*/

public class ServerGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, ChatServerListener {

    private static final int POS_X = 800;
    private static final int POS_Y = 200;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;

    private final ChatServer chatServer = new ChatServer(this);
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private final JPanel panelTop = new JPanel(new GridLayout(1, 2));
    private final JTextArea log = new JTextArea();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerGUI();
            }
        });
    }

    private ServerGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);
        log.setEditable(false);
        log.setLineWrap(true);
        JScrollPane scrollLog = new JScrollPane(log);
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatServer.stop();
            }
        });
        btnStart.addActionListener(this);
        panelTop.add(btnStart);
        panelTop.add(btnStop);
        add(panelTop, BorderLayout.NORTH);
        add(scrollLog, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnStart) {
            chatServer.start(8189);
        } else {
            throw new RuntimeException("Unknown source: " + src);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0) {
            msg = "Empty Stacktrace";
        } else {
            msg = e.getClass().getCanonicalName() + ": " +
                    e.getMessage() + "\n\t at " + ste[0];
        }
        JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    @Override
    public void onChatServerMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            log.append(msg + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }
}
