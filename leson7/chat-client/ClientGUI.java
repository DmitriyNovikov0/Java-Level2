package leson7.chat.client;

import leson7.chat.common.Library;
import leson7.network.SocketThread;
import leson7.network.SocketThreadListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import static leson7.chat.common.Library.*;

/*
1 ещё раз быстро пройтись по той части кода, которая отвечает за сетевое взаимодействие,
пояснить коротко, что от чего зависит

2 подробнее рассказать про оператор synchronized

3 как работает метод sendMessage внутри SocketThread

4 Почему DataInputStream, а не просто InputStream

5 в основном проблема с пониманием интерфейсов и как ими пользоваться (если честно то и ООП в целом)

6 Сложно перестроиться с процедурного программирования

7 зачем классу КлиентГУИ переопределять методы СокетТредЛистнера, в то время, как они уже переопределены
в классе ЧатСервер

8 ещё раз о цикле отправки/приёма сообщения

9 Сервер и клиент создают по сокету и общаются через них? то есть сервер что то отправил в свой сокет,
тот отправляет что то в сокет клиента итд?

10 с каждым сокетом и серверсокетом работа ведётся в отдельном потоке?

11 серверсокет.сетСоТаймаут нам нужен только для исключения и как следствие выхода из цикла?
и что будет выполняться после континью (строка 34 в сокетТреде)

12 в серверГУИ в экшнПефомд мы задаём номер порта 8189 - это просто случайный свободный порт?

14 СерверСокетТред строка 31 сокет = серверСокет.эксепт() этой строкой, как я понимаю, мы заставляем сервер
ждать сокет клиента, но не понял, указываем ли мы где то сокету клиента на сокет сервера
(в двух словах - где связываются два сокета)

15 Как стало ясно - метод эксепт вешает весь серверСокет пока не поймает клиентский Сокет.
А если к разным портам одного и того же серверСокет будут добавлены слушатели? как этот момент будет решаться

16 Мы синхронайзд добавили на конкретный порт на разные сокеты. а если будет с разных портов поступать?
и возможно ли это в принципе?

17 почему в конструкторе СерверСокетТред передаются именно такие параметры, листнер особенно

18 Листнеры - почему именно такие параметры передаются в методах? как понять что нужно передать?

19 при использовании потоков всегда нужно использовать интерфейсы? или только при использовании потоков
в работе серверов, чат-серверов?

20 как выделить методы, которые нужно использовать в интерфейсе?

21 почему методы сокета мы засинхронили, а серверсокета нет?

22 зачем в сокетТред при закрытии на 59 строке вызывать сокет.клоуз, ведь ранее используется интеррапт,
который приводит к закрытию сокета в 36й строке, не приведёт ли это к попытке закрыть закрытый сокет?

23 зачем синхронизировать методы сокеттредлистнера? что будет если этого не сделать?

24 аналогично для сокеттреда - что будет если клоуз и сендмессадж будут не синхронизированы?
* */

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, SocketThreadListener {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;
    private static final String TITLE = "Chat Client";

    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss: ");

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Alwayson top");
    private final JTextField tfLogin = new JTextField("ivan");
    private final JPasswordField tfPassword = new JPasswordField("123");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private final JList<String> userList = new JList<>();
    private final String[] EMPTY = new String[0];
    private boolean shownIoErrors = false;
    private SocketThread socketThread;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });
    }

    private ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);

        log.setEditable(false);
        log.setLineWrap(true);
        JScrollPane scrollLog = new JScrollPane(log);
        JScrollPane scrollUsers = new JScrollPane(userList);
        scrollUsers.setPreferredSize(new Dimension(100, 0));
        cbAlwaysOnTop.addActionListener(this);
        btnLogin.addActionListener(this);
        btnSend.addActionListener(this);
        tfMessage.addActionListener(this);
        btnDisconnect.addActionListener(this);
        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        panelBottom.add(btnDisconnect, BorderLayout.WEST);
        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        panelBottom.setVisible(false);
        add(panelTop, BorderLayout.NORTH);
        add(scrollLog, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);
        add(scrollUsers, BorderLayout.EAST);
        setVisible(true);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        showException(e);
        System.exit(1);
    }

    private void showException(Throwable e) {
        e.printStackTrace();
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0)
            msg = "Empty Stacktrace";
        else {
            msg = e.getClass().getCanonicalName() + ": " + e.getMessage() +
                    "\n\t at " + ste[0];
        }
        JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        } else if (src == btnLogin || src == tfIPAddress || src == tfLogin || src == tfPassword || src == tfPort) {
            connect();
        } else if (src == btnSend || src == tfMessage) {
            sendMessage();
        } else if (src == btnDisconnect) {
            socketThread.close();
        } else {
            throw new RuntimeException("Unknown source: " + src);
        }
    }

    private void connect() {
        Socket socket = null;
        try {
            socket = new Socket(tfIPAddress.getText(), Integer.parseInt(tfPort.getText()));
        } catch (IOException e) {
            log.append("Exception: " + e.getMessage());
        }
        socketThread = new SocketThread(this, "Client thread", socket);
    }

    void sendMessage() {
        String msg = tfMessage.getText();
        if ("".equals(msg)) return;
        tfMessage.setText(null);
        tfMessage.requestFocusInWindow();
        socketThread.sendMessage(Library.getClientBcast(msg));
    }

    private void wrtMsgToLogFile(String msg, String username) {
        try (FileWriter out = new FileWriter("log.txt", true)) {
            out.write(username + ": " + msg + "\n");
            out.flush();
        } catch (IOException e) {
            if (!shownIoErrors) {
                shownIoErrors = true;
                JOptionPane.showMessageDialog(this, "File write error", "Exception", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void putLog(String msg) {
        if ("".equals(msg)) return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    /**
     * Socket Thread Events
     */

    @Override
    public void onSocketThreadStart(SocketThread thread, Socket socket) {
        putLog("Connection Established");
    }

    @Override
    public void onSocketThreadStop(SocketThread thread) {
        putLog("Connection lost");
        panelBottom.setVisible(false);
        panelTop.setVisible(true);
        setTitle(TITLE);
        userList.setListData(EMPTY);
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        handleMessage(msg);
    }

    @Override
    public void onSocketThreadReady(SocketThread thread, Socket socket) {
        panelBottom.setVisible(true);
        panelTop.setVisible(false);
        String login = tfLogin.getText();
        String password = new String(tfPassword.getPassword());
        thread.sendMessage(Library.getAuthRequest(login, password));
    }

    @Override
    public void onSocketThreadException(SocketThread thread, Exception e) {
        // исключение, например, будет возникать, когда мы будем отключать клиента от чата
        // в этом случае, исключение можно считать штатным, и просто логгировать
        // Исключение создаётся поскольку внутри SocketThread мы находимся в блокирующем
        // методе readUTF() и выходим из него только по факту отключения сокета,
        // то есть исключения
        showException(e);
    }

    private void handleMessage(String value) {
        String[] arr = value.split(Library.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case Library.AUTH_ACCEPT:
                setTitle(TITLE + " logged in as: " + arr[1]);
                break;
            case Library.AUTH_DENIED:
                putLog(value);
                break;
            case Library.MSG_FORMAT_ERROR:
                putLog(value);
                socketThread.close();
                break;
            case Library.TYPE_BROADCAST:
                putLog(dateFormat.format(Long.parseLong(arr[1])) +
                        arr[2] + ": " + arr[3]);
                break;
            case Library.USER_LIST:
                String users = value.substring(Library.USER_LIST.length() + Library.DELIMITER.length());
                String[] userArray = users.split(Library.DELIMITER);
                Arrays.sort(userArray);
                userList.setListData(userArray);
                break;
            default:
                throw new RuntimeException("Unknown message type: " + value);
        }
        //        wrtMsgToLogFile(msg, username);

    }
}