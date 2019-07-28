package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ClientGUI extends JFrame  implements ActionListener, Thread.UncaughtExceptionHandler, WindowListener {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top");
    private final JTextField tfLogin = new JTextField("ivan");
    private final JPasswordField tfPassword = new JPasswordField("123");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private final JList<String> userList = new JList<>();


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });
    }

    private ClientGUI() {
//чтобы отловить закрытие окна, лог писать лучше перед закрытием окна так камк при записи в файл файл нужно открыть, а держать файл открытым  мне кажется не лучшая идея
        this.addWindowListener(this);
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Client");
        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        JScrollPane scrollUsers = new JScrollPane(userList);
        String[] users = {"user1", "user2", "user3", "user4", "user5", "User_with_a_very_long_name" };
        userList.setListData(users);
        scrollUsers.setPreferredSize(new Dimension(100, 0));

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        panelBottom.add(btnDisconnect, BorderLayout.WEST);
        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);

        cbAlwaysOnTop.addActionListener(this);
        btnSend.addActionListener(this);

        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);
        add(scrollLog, BorderLayout.CENTER);
        add(scrollUsers, BorderLayout.EAST);
        setVisible(true);
    }
/*
	Отправлять сообщения в лог по нажатию кнопки или по нажатию клавиши Enter.
	Создать лог в файле (показать комментарием, где и как Вы планируете писать сообщение в файловый журнал).
    Прочитать методичку к следующему уроку

* */
        @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        } else if(src == btnSend){
/*
вероятно я ошибаюсь, но добавить текст в лог файл можно тут.
писать в файл лучше при закрытии чата, так как перед записью файл нужно открыть а держать файл открытым
все время пока общаешься в чате не очень хорошая идея, а перед закрытием чата открываем файл пишим в файл все писанину из log, закрываем файл.
WindowListener мы пока не проходили :), а хотя.... попробую
 */
            log.append(tfLogin.getText() + ": " + tfMessage.getText() + "\n");
            System.out.println("sending message:" + tfMessage.getText());
            tfMessage.setText("");
            }
        else {
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
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {

//для чего делаю? хочу узнать Вашего мнения правильно ли я делаю
        BufferedWriter fileOut = null;
        try{
            fileOut = new BufferedWriter(new FileWriter("c:\\chat.log", true));
            log.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }
}
