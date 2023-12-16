package com.emailapp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс EmailReader отвечает за чтение списка адресов электронной почты и соответствующих имён из файла.
 */
public class EmailReader {

    /**
     * Читает адреса электронной почты и соответствующие имена из файла.
     *
     * @param filePath Путь к файлу с адресами электронной почты.
     * @return Карта, где ключ - адрес электронной почты, а значение - имя.
     */
    public static Map<String, String> readEmailsFromFile(String filePath) {
        Map<String, String> emailMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String email = parts[1].trim();
                    emailMap.put(email, name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emailMap;
    }

    /**
     * Точка входа в программу для тестирования класса EmailReader.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        String filePath = "C:\\Users\\egoro\\КР\\email\\src\\emails.txt"; // Замените на фактический путь к файлу
        Map<String, String> emails = readEmailsFromFile(filePath);
        emails.forEach((email, name) -> System.out.println("Email: " + email + ", Name: " + name));
    }
}
