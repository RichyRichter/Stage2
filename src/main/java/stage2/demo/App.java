package stage2.demo;

import stage2.practice.DataBase.ConnectionManager;
import stage2.practice.DataBase.Controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        Controller controller = new Controller(ConnectionManager.getStatement());
        StringBuilder res = new StringBuilder();
        System.out.println(controller.findAllCenters());
        System.out.println("Выберите действие:\n" +
                "1 - Создать/удалить пункт или улицу\n" +
                "2 - Получить информацию о населенном пункте\n" +
                "3 - Получить информацию о наличии улицы\n" +
                "4 - Получить информацию по критериям поиска\n");


        switch (scanner.nextInt()) {
            case 1 -> changePlace();
            case 2 -> showCenterInfo(scanner, controller, res);
            case 3 -> showStreetInfo(scanner, controller, res);
            case 4 -> infoByParameter(scanner, controller, res);
        }
        ConnectionManager.closeConnection();
    }

    private static void infoByParameter(Scanner scanner, Controller controller, StringBuilder res) throws SQLException, IOException {
        System.out.println("Напишите критерий поиска, условие и значение через пробел. " +
                "Примеры: Население >= 100000.\nВозможные критерии: Население, Год, Площадь.");
        res.append(controller.findCenterByCondition(scanner.next(), scanner.next(), scanner.nextInt()));
        System.out.println(res);
        writeFile(scanner, res);
    }

    private static void showStreetInfo(Scanner scanner, Controller controller, StringBuilder res) throws SQLException, IOException {
        System.out.println("Напишите название или часть названия улицы. Примеры: Сол, Солнечная\n");
        res.append(controller.findStreetInCenter(scanner.next()));
        System.out.println(res);
        writeFile(scanner, res);
    }

    private static void showCenterInfo(Scanner scanner, Controller controller, StringBuilder res) throws SQLException, IOException {
        System.out.println("Напишите название населенного пункта\n");
        res.append(controller.findCenterInfo(scanner.next()));
        System.out.println(res);
        writeFile(scanner, res);
    }

    private static void writeFile(Scanner scanner, StringBuilder res) throws IOException {
        System.out.println("Сохранить данные в файл?\n" +
                "1 - Да 2 - Нет");
        if (scanner.nextInt() == 1) {
            writeFile(res);
        }
    }

    private static void writeFile(StringBuilder res) throws IOException {
        File result = new File("src/main/java/stage2/practice/DataBase/result.txt");
        if (!result.exists()) result.createNewFile();
        PrintWriter printWriter = new PrintWriter("src/main/java/stage2/practice/DataBase/result.txt");
        printWriter.print(res.toString());
        printWriter.close();
    }

    private static void changePlace() throws SQLException {
        Controller controller = new Controller(ConnectionManager.getStatement());
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите действие:\n" +
                "1 - Создать населенный пункт\n" +
                "2 - Создать улицу\n" +
                "3 - Удалить населенный пункт или улицу\n" +
                "4 - Добавить улицу в город");
        switch (scanner.nextInt()) {
            case 1 -> {
                System.out.println("Введите данные о населенном пунте через пробел. Пример - Самара Город 541 1144000 1586");
                controller.addCenter(scanner.next(), scanner.next(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
            }
            case 2 -> {
                System.out.println("Введите данные о улице через пробел. Пример - Солнечная Улица. Виды улиц: Улица, " +
                        "Бульвар, Проспект, Аллея, Набережная. ");
                controller.addStreet(scanner.next(), scanner.next());
            }
            case 3 -> {
                System.out.println("Введите данные для удаления через пробел. Примеры: Москва Пункт, Длинная Улица");
                controller.deletePlace(scanner.next(), scanner.next());
            }
            case 4 -> {
                System.out.println(controller.findAllStreets());
                System.out.println("Укажите название города и ID улицы через пробел. Пример: Москва 2");
                controller.addStreetToCenter(scanner.next(), scanner.nextInt());
            }
        }
    }
}
