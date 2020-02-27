package com.vorotilin;

import org.ini4j.Ini;

import javax.xml.bind.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

//Client class
public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    //метод для подключения к серверу
    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (ConnectException e) {
            System.out.println("Can't connect to the server, please try again");
            System.exit(1);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Неправильное имя хоста при подключении к серверу");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключению к серверу");
        }
    }

    //Метод считывания приветствия от сервера
    public void readGreetingsFromServer() {
        try {
            String resp = in.readLine();
            System.out.println(resp);
            String resp2 = in.readLine();
            System.out.println(resp2);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при считывание приветствия от сервера");
        }
    }

    //метод сериализации юзера в xml
    public StringWriter userMarshalling(User user) {
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(User.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            sw = new StringWriter();
            jaxbMarshaller.marshal(user, sw);
        } catch (JAXBException e) {
            e.printStackTrace();
            System.out.println("Ошибка при серилизации юзера");
        }
        return sw;
    }

    //метод отправки xml на сервер
    public void sendUserToServer(StringWriter sw) {
        out.println(sw);
    }

    //метод десерелизации респонса из xml
    public Response getResponseFromServer() {
        Response givenResponse = new Response();
        try {
            String response3 = in.readLine(); //получение респонса от сервера в xml
            StringReader reader2 = new StringReader(response3);
            JAXBContext jaxbContext2 = JAXBContext.newInstance(Response.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext2.createUnmarshaller();
            givenResponse = (Response) jaxbUnmarshaller.unmarshal(reader2);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при получении ответа от сервера");
        } catch (JAXBException e) {
            e.printStackTrace();
            System.out.println("Ошибка при получении ответа от сервера");
        }
        return givenResponse;
    }

    public static void main(String[] args) {
        Ini ini = new Ini();
        try {
            ini = new Ini(new File("src/main/resources/properties.ini"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при считывании .ini файла");
        }
        String host = ini.get("serverProperties", "host"); //Получаем хост из проперти (properties.ini)
        int port = Integer.parseInt(ini.get("serverProperties", "port")); //Получаем порт из проперти (properties.ini)
        Scanner scn = new Scanner(System.in);
        Client testClient = new Client();
        testClient.startConnection(host, port);
        testClient.readGreetingsFromServer();
        User testUser = new User(scn.nextLine(), scn.nextLine(), scn.nextLine()); //формирования юзера из консоли путём ввода
        StringWriter sw = testClient.userMarshalling(testUser);
        testClient.sendUserToServer(sw); //отправка xml на сервер
        Response givenResponse = testClient.getResponseFromServer();
        System.out.println(givenResponse.getMessage()); //вывод респонса от сервера
    }
}


