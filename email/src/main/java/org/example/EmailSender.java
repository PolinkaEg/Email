import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Класс EmailSender используется для отправки электронных писем.
 */
public class EmailSender {

    private static final Logger logger = LogManager.getLogger(EmailSender.class);

    /**
     * Читает шаблон сообщения из файла.
     *
     * @param filePath Путь к файлу шаблона сообщения.
     * @return Строка, содержащая шаблон сообщения.
     */
    public static String readMessageTemplate(String filePath) {
        StringBuilder message = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                message.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.error("Ошибка при чтении файла шаблона сообщения: ", e);
        }
        return message.toString();
    }

    /**
     * Отправляет электронное письмо на указанный адрес.
     *
     * @param recipient Адрес получателя.
     * @param subject Тема письма.
     * @param content Содержимое письма.
     */
    public static void sendEmail(String recipient, String subject, String content) {
        final String username = "coursework05@mail.ru";
        final String password = "TgwNkhZT7vajRdGSmcBM";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.mail.ru");
        prop.put("mail.smtp.port", "465"); // Порт для SSL
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true"); // Использование SSL

        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("coursework05@mail.ru"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

            logger.info("Письмо успешно отправлено на: " + recipient);
        } catch (MessagingException e) {
            logger.error("Ошибка при отправке письма: ", e);
        }
    }

    /**
     * Точка входа в программу для тестирования класса EmailSender.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        String emailFilePath = "C:\\Users\\egoro\\КР\\email\\src\\emails.txt";
        String messageFilePath = "C:\\Users\\egoro\\КР\\email\\src\\message.txt";

        Map<String, String> emails = EmailReader.readEmailsFromFile(emailFilePath);
        String messageTemplate = readMessageTemplate(messageFilePath);

        for (Map.Entry<String, String> entry : emails.entrySet()) {
            String email = entry.getKey();
            String name = entry.getValue();
            String personalizedMessage = messageTemplate.replace("[name]", name);
            sendEmail(email, "Тестовое сообщение", personalizedMessage);
        }
    }
}
