import com.emailapp.EmailReader;
import com.emailapp.EmailSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;

/**
 * Класс EmailAppGUI представляет графический интерфейс пользователя для приложения отправки электронной почты.
 * Он позволяет пользователю выбирать файлы для адресов и текста сообщения, а также запускать процесс отправки писем.
 */
public class EmailAppGUI {

    private JFrame frame;
    private JTextField emailFilePathField;
    private JTextField messageFilePathField;
    private JTextArea logArea;
    private static final Logger logger = LogManager.getLogger(EmailAppGUI.class);

    /**
     * Конструктор класса EmailAppGUI.
     * Инициализирует и отображает графический интерфейс пользователя.
     */
    public EmailAppGUI() {
        initialize();
    }

    /**
     * Инициализирует элементы интерфейса пользователя и располагает их на экране.
     */
    private void initialize() {
        frame = new JFrame("Email Sender App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new GridLayout(3, 3, 10, 10));

        JLabel emailFileLabel = new JLabel("Email File:");
        panel.add(emailFileLabel);

        emailFilePathField = new JTextField();
        panel.add(emailFilePathField);
        emailFilePathField.setColumns(10);

        JButton emailFileButton = new JButton("Browse...");
        emailFileButton.addActionListener(e -> chooseFile(emailFilePathField));
        panel.add(emailFileButton);

        JLabel messageFileLabel = new JLabel("Message File:");
        panel.add(messageFileLabel);

        messageFilePathField = new JTextField();
        panel.add(messageFilePathField);
        messageFilePathField.setColumns(10);

        JButton messageFileButton = new JButton("Browse...");
        messageFileButton.addActionListener(e -> chooseFile(messageFilePathField));
        panel.add(messageFileButton);

        JButton sendEmailsButton = new JButton("Send Emails");
        sendEmailsButton.addActionListener(e -> sendEmails());
        panel.add(sendEmailsButton);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        frame.setMinimumSize(new Dimension(600, 400)); // Установка минимального размера окна
        frame.pack(); // Автоматически устанавливает размер окна
        frame.setLocationRelativeTo(null); // Центрирует окно на экране
    }

    /**
     * Открывает диалоговое окно для выбора файла и устанавливает выбранный путь в текстовое поле.
     *
     * @param textField Текстовое поле, в которое будет установлен путь к выбранному файлу.
     */
    private void chooseFile(JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            textField.setText(file.getAbsolutePath());
            logger.info("Выбран файл: " + file.getAbsolutePath());
        }
    }

    /**
     * Отправляет электронные письма на основе выбранных файлов адресов и текста сообщения.
     * Логирует результаты отправки в текстовую область интерфейса.
     */
    private void sendEmails() {
        String emailFilePath = emailFilePathField.getText();
        String messageFilePath = messageFilePathField.getText();
        logger.info("Начало отправки писем");
        Map<String, String> emails = EmailReader.readEmailsFromFile(emailFilePath);
        String messageTemplate = EmailSender.readMessageTemplate(messageFilePath);

        for (Map.Entry<String, String> entry : emails.entrySet()) {
            String email = entry.getKey();
            String name = entry.getValue();
            String personalizedMessage = messageTemplate.replace("[name]", name);
            EmailSender.sendEmail(email, "Test Email", personalizedMessage);
            logArea.append("Email sent to: " + email + "\n");
        }
        logger.info("Письма отправлены");
    }

    /**
     * Отображает главное окно приложения.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Точка входа в приложение.
     * Создает и отображает главное окно приложения.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                EmailAppGUI window = new EmailAppGUI();
                window.show();
            } catch (Exception e) {
                logger.error("Ошибка при запуске приложения: ", e);
            }
        });
    }
}
