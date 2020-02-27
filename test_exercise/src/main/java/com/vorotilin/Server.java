package com.vorotilin;

import org.apache.logging.log4j.LogManager;
import org.ini4j.Ini;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.*;

import org.apache.logging.log4j.Logger;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Logger log = LogManager.getLogger(Server.class);

    //создание сервера и in и out каналов
    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port); //создание сервер сокета
            clientSocket = serverSocket.accept(); // ожидание подключения клиента к сокету сервера
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Проблема с подключением к серверу, IOexception error");
        }
        System.out.println("Server is running");
        log.info("Сервер включен");
    }

    //вывод юзеру привествие
    public void greetUser() {
        out.println("Hello client, it is server number1" + "\n" +
                "Please enter your firstName, lastName and message you want to share"); //приветствие клиента
        log.info("Приветствие передано");
    }

    //получение информации от юзера
    public User getClientInfo() {
        User clientUser = new User();
        try {
            String clientInfo = in.readLine(); //считывание информации о клиенте
            JAXBContext jaxbUserContext = JAXBContext.newInstance(User.class);
            Unmarshaller jaxbUserUnmarshaller = jaxbUserContext.createUnmarshaller();
            StringReader reader = new StringReader(clientInfo);
            clientUser = (User) jaxbUserUnmarshaller.unmarshal(reader);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("IOexception при получении данных от клиента");
        } catch (JAXBException e) {
            e.printStackTrace();
            log.error("JAXBException при получении данных от клиента");
        }
        log.info("Данные от юзера получены");
        return clientUser;
    }

    //серелизация респонса для юзера
    public StringWriter getResponseToUser(User user) {
        StringWriter sw = new StringWriter();
        try {
            Response responseToUser = new Response(user); //формирование ответа юзеру
            JAXBContext jaxbContext2 = JAXBContext.newInstance(Response.class);
            Marshaller jaxbMarshaller = jaxbContext2.createMarshaller();
            sw = new StringWriter();
            jaxbMarshaller.marshal(responseToUser, sw);
        } catch (JAXBException e) {
            e.printStackTrace();
            log.error("JAXBException при получении ответа от клиента");
        }
        return sw;
    }

    //отправление информации о принятом сообщении
    public void sendInfoToClient(StringWriter sw) {
        out.println(sw);
        log.info("Информация о принятом сообщении передана");
    }

    public static void main(String[] args) {
        Logger log = LogManager.getLogger(Server.class);
        Ini ini = new Ini();
        try {
            ini = new Ini(new File("src/main/resources/properties.ini"));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Exception при считывание ini файла");
        }
        int port = Integer.parseInt(ini.get("serverProperties", "port")); //Получаем порт из проперти (properties.ini)
        Server server = new Server();
        server.startServer(port);
        server.greetUser();
        User clientUser = server.getClientInfo();
        StringWriter sw = server.getResponseToUser(clientUser);
        server.sendInfoToClient(sw);
    }
}