package Manager;

import DataClasses.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Класс, реализующий выполнение команд и управление коллекцией
 */
public class CollectionManager {

    private PriorityQueue<Product> collection;
    int ex = 1;
    boolean sign = false;
    Integer idEq;
    Integer maxId = 0;
    private Gson json;
    private Date initDate;

    {
        json = new Gson();
        collection = new PriorityQueue<>();
    }

    /**
     * Метод загружает коллекцию из файла
     *
     * @param file
     * @throws IOException
     */
    public CollectionManager(File file) throws IOException {
        this.load(file);
        this.initDate = new Date();
        if (!(collection.size() == 0)) {
            for (Product p: collection) {
                if (p.getId() > maxId) {
                    maxId = p.getId();
                }
            }
        }
    }

    /**
     * Метод выводит список доступных команд
     */
    public void help(){
        System.out.println("help : вывести справку по доступным командам");
        System.out.println("info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        System.out.println("show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        System.out.println("add {element} : добавить новый элемент в коллекцию");
        System.out.println("update id {element} : обновить значение элемента коллекции, id которого равен заданному");
        System.out.println("remove_by_id id : удалить элемент из коллекции по его id");
        System.out.println("clear : очистить коллекцию");
        System.out.println("save : сохранить коллекцию в файл");
        System.out.println("execute_script file_name : считать и исполнить скрипт из указанного файла");
        System.out.println("exit : завершить программу (без сохранения в файл)");
        System.out.println("remove_first : удалить первый элемент из коллекции");
        System.out.println("add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
        System.out.println("remove_greater {element} : удалить из коллекции все элементы, превышающие заданный");
        System.out.println("average_of_price : вывести среднее значение поля price для всех элементов коллекции");
        System.out.println("count_by_owner owner : вывести количество элементов, значение поля owner которых равно заданному");
        System.out.println("count_less_than_price price : вывести количество элементов, значение поля price которых меньше заданного");
    }

    /*
     * Метод выводит информацию о коллекции
     */
    public void info() {
        System.out.println("Тип коллекции: PriorityQueue");
        System.out.println("Размер коллекции: " + collection.size());
        System.out.println("Дата инициализации: " + initDate);

    }

    /**
     * Метод все элементы коллекции
     */
    public void show() {
        if (collection.size() != 0) {
            collection.forEach(p -> System.out.println(json.toJson(p)));
        } else {
            System.out.println("Коллекция пуста");
        }
    }

    /**
     * Метод добавляет элемент в коллекцию
     */
    public void add() {
        Scanner scanner = new Scanner(System.in);
        Product product;
        UnitOfMeasure unitOfMeasure = null;
        Color eyeColor = null;
        Country nationality = null;
        StringParser pars = new StringParser();

        String name = pars.strParse("название продукта");

        String strX;
        int x = 858;
        do {
            try {
                System.out.println("Введите значение поля координата x (значение должно быть меньше или равно 857)");
                strX = scanner.nextLine().trim();
                x = Integer.parseInt(strX);
                if (x >= 858) {
                    System.out.println("Значение не может быть больше 857. Повторите ввод");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введенное значение не является целым числом или выходит за пределы int. Повторите ввод");
            }
        } while (x >= 858);

        Double y = pars.dblParse("координата y");

        Integer price;
        do {
            price = pars.intParse("цена");
            if (price <= 0) {
                System.out.println("Цена не может быть меньше или равна 0");
            }
        } while (price <= 0);

        int length;
        String partNumber;
        do {
            partNumber = pars.strParse("номер партии");
            length = partNumber.length();
            if ((length > 85) || (length <15 )) {
                System.out.println("Номер партии не может быть длиннее 85 или короче 15. Повторите ввод");
            }
        } while ((length > 85) || (length < 15));

        String strManufactureCost;
        Long manufactureCost = null;
        do {
            try {
                System.out.println("Введите значение поля цена изготовления");
                strManufactureCost = scanner.nextLine().trim();
                if (strManufactureCost.equals("")) {
                    System.out.println("Цена изготовления не может быть null. Повторите ввод");
                } else {
                    manufactureCost = Long.parseLong(strManufactureCost);
                }
            } catch (NumberFormatException e) {
                System.out.println("Введенное значение не является или выходит за пределы long. Повторите ввод");
            }
        } while (manufactureCost == null);

        String strUnitOfMeasure;
        do {
            System.out.println("Введите одну из единиц измерения: " + Arrays.toString(UnitOfMeasure.values()));
            strUnitOfMeasure = scanner.nextLine().trim().toUpperCase();
            try {
                unitOfMeasure = UnitOfMeasure.valueOf(strUnitOfMeasure);
            } catch (IllegalArgumentException e) {
                System.out.println("Данной единицы измерения не существует. Повторите ввод");
            } catch (NullPointerException e) {
                System.out.println("Единица измерения не может быть null. Повторите ввод");
            }
        } while (unitOfMeasure == null);

        String personName = pars.strParse("имя владельца");

        Double weight;

        do {
            weight = pars.dblParse("вес");
        } while (weight <= 0);

        String strEyeColor;
        do {
            System.out.println("Введите один из цветов глаз: " + Arrays.toString(Color.values()));
            strEyeColor = scanner.nextLine().trim().toUpperCase();
            try {
                eyeColor = Color.valueOf(strEyeColor);
            } catch (IllegalArgumentException e) {
                System.out.println("Данного цвета не существует. Повторите ввод");
            } catch (NullPointerException e) {
                System.out.println("Цвет не может быть null. Повторите ввод");
            }
        } while (eyeColor == null);

        String strNationality;
        do {
            System.out.println("Введите одну из стран: " + Arrays.toString(Country.values()));
            strNationality = scanner.nextLine().trim().toUpperCase();
            try {
                nationality = Country.valueOf(strNationality);
            } catch (IllegalArgumentException e) {
                System.out.println("Данной страны не существует. Повторите ввод");
            } catch (NullPointerException e) {
                System.out.println("Страна не может быть null. Повторите ввод");
            }
        } while (nationality == null);

        Integer id = maxId+1;
        maxId = id;

        if (sign) {
                product = new Product(idEq, name, new Coordinates(x, y), price, partNumber, manufactureCost, unitOfMeasure, new Person(personName, weight, eyeColor, nationality));
            } else {
                product = new Product(id, name, new Coordinates(x, y), price, partNumber, manufactureCost, unitOfMeasure, new Person(personName, weight, eyeColor, nationality));
            }

        collection.add(product);
        System.out.println("Элемент коллекции обновлен");

    }

    /**
     * Метод обновляет элемент коллекции с указанным id
     *
     * @param strId
     */
    public void update(String strId) {
        try {
            sign = true;
            if (!(collection.size() == 0)) {
                Integer id = Integer.parseInt(strId);
                if (collection.removeIf(collection -> collection.getId().equals(id))) {
                    idEq = id;
                    add();
                } else {
                    System.out.println("Элемента с данным id не существует");
                }
            } else {
                System.out.println("Коллекция пуста");
            }
            sign = false;
        } catch (NumberFormatException e) {
            System.out.println("Введенное значение не является или выходит за пределы int. Повторите ввод");
        }
    }

    /**
     * Метод удаляет элемент коллекции с указанным id
     *
     * @param strId
     */
    public void remove_by_id(String strId) {
        try {
            if (!(collection.size() == 0)) {
                int id = Integer.parseInt(strId);
                if (collection.removeIf(collection -> collection.getId() == id)) {
                    System.out.println("Элемент удален из коллекции");
                } else System.out.println("Элемента с таким id не существует");
            } else {
                System.out.println("Коллекция пуста");
            }
        } catch (NumberFormatException e) {
            System.out.println("Введенное значение не является или выходит за пределы int. Повторите ввод");
        }
    }

    /**
     * Метод очищает коллекцию
     */
    public void clear() {
        collection.clear();
        System.out.println("Коллекция очищена");
    }

    /**
     * Метод сохраняет коллекцию в файл
     *
     * @throws IOException
     */
    public void save() throws IOException {
        File outfile = new File(System.getenv("ProductsFile"));
        BufferedWriter writter = new BufferedWriter(new FileWriter(outfile));
        String outJson = json.toJson(collection);
        writter.write(outJson);
        writter.close();
        System.out.println("Коллекция сохранена");
    }

    /**
     * Метод обеспечивает выполнение команды add в execute скрипте
     * @param strName
     * @param strX
     * @param strY
     * @param strPrice
     * @param strPartNumber
     * @param strManufactureCost
     * @param strUnitOfMeasure
     * @param strOwnerName
     * @param strWeight
     * @param strEyeColor
     * @param strNationality
     */
    public void add(String strName, String strX, String strY, String strPrice, String strPartNumber, String strManufactureCost,
                    String strUnitOfMeasure, String strOwnerName, String strWeight, String strEyeColor, String strNationality) {
        Product product;
        UnitOfMeasure unitOfMeasure;
        Color eyeColor;
        Country nationality;

        String name;
        if (strName.equals("")) {
            System.out.println("Имя продукта не может быть null. Элемент не добавлен в коллекцию");
            return;
        } else {
            name = strName;
        }

        int x;
        try {
            if ((strX.equals("")) || (Integer.parseInt(strX) > 857)) {
                System.out.println("Координата x не может быть null или больше 857. Элемент не добавлен в коллекцию");
                return;
            } else {
                x = Integer.parseInt(strX);
            }
        } catch (NumberFormatException e) {
            System.out.println("Введенное значение не является или выходит за пределы int. Элемент не добавлен в коллекцию");
            return;
        }

        double y;
        try {
            if (strY.equals("")) {
                System.out.println("Координата y не может быть null. Элемент не добавлен в коллекцию");
                return;
            } else {
                y = Double.parseDouble(strY);
            }
        } catch (NumberFormatException e) {
            System.out.println("Введенное значение не является или выходит за пределы double. Элемент не добавлен в коллекцию");
            return;
        }

        int price;
        try {
            if ((strPrice.equals("")) || (Integer.parseInt(strPrice) <= 0)) {
                System.out.println("Цена не может быть null или меньше/равна 0. Элемент не добавлен в коллекцию");
                return;
            } else {
                price = Integer.parseInt(strPrice);
            }
        } catch (NumberFormatException e) {
            System.out.println("Введенное значение не является или выходит за пределы int. Элемент не добавлен в коллекцию");
            return;
        }

        String partNumber;
        if ((strPartNumber.equals("")) || (strPartNumber.length() > 85) || (strPartNumber.length() < 15)) {
            System.out.println("Номер партии не может быть null, длиной больше 85 или меньше 15. Элемент не добавлен в коллекцию");
            return;
        } else {
            partNumber = strPartNumber;
        }

        long manufactureCost;
        try {
            if (strManufactureCost.equals("")) {
                System.out.println("Цена производства не может быть null. Элемент не добавлен в коллекцию");
                return;
            } else {
                manufactureCost = Long.parseLong(strManufactureCost);
            }
        } catch (NumberFormatException e) {
            System.out.println("Введенное значение не является или выходит за пределы long. Элемент не добавлен в коллекцию");
            return;
        }

        try {
            unitOfMeasure = UnitOfMeasure.valueOf(strUnitOfMeasure.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Введенной единицы измерения не существует. Элемент не сохранен в коллекцию");
            return;
        } catch (NullPointerException e) {
            System.out.println("Единица измерения не может быть null. Элемент не сохранен в коллекцию");
            return;
        }

        String personName;
        if (strOwnerName.equals("")) {
            System.out.println("Имя владельца не может быть null. Элемент не сохранен в коллекцию");
            return;
        } else {
            personName = strOwnerName;
        }

        double weight;
        try {
            if ((strWeight.equals("")) || (Double.parseDouble(strWeight) <= 0)) {
                System.out.println("Вес не может быть null или меньше/равен 0. Элемент не добавлен в коллекцию");
                return;
            } else {
                weight = Double.parseDouble(strWeight);
            }
        } catch (NumberFormatException e) {
            System.out.println("Введенное значение не является или выходит за пределы double. Элемент не добавлен в коллекцию");
            return;
        }

        try {
            eyeColor = Color.valueOf(strEyeColor.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Введенного цвета не существует. Элемент не сохранен в коллекцию");
            return;
        } catch (NullPointerException e) {
            System.out.println("Цвет не может быть null. Элемент не сохранен в коллекцию");
            return;
        }

        try {
            nationality = Country.valueOf(strNationality.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Введенной страны не существует. Элемент не сохранен в коллекцию");
            return;
        } catch (NullPointerException e) {
            System.out.println("Страна не может быть null. Элемент не сохранен в коллекцию");
            return;
        }

        Integer id = maxId+1;
        maxId = id;


        product = new Product(id, name, new Coordinates(x, y), price, partNumber, manufactureCost, unitOfMeasure,
                new Person(personName, weight, eyeColor, nationality));
        collection.add(product);
        System.out.println("Элемент коллекции добавлен");
    }

    /**
     * Метод обеспечивает выполнение команд из скрипта
     *
     * @param strFile
     * @throws IOException
     */
    public void execute_script(String strFile) throws IOException {
        String strCommand = "";
        String[] commands;
        try {
            BufferedInputStream script = new BufferedInputStream(new FileInputStream(strFile));
            try (Scanner scan = new Scanner(script)) {
                while (scan.hasNextLine() && !strCommand.equals("exit")) {
                    strCommand = scan.nextLine();
                    commands = strCommand.trim().split(" ", 2);
                    try {
                        switch (commands[0]) {
                            case "":
                                break;
                            case "help":
                                help();
                                break;
                            case "info":
                                info();
                                break;
                            case "show":
                                show();
                                break;
                            case "add":
                                String[] strParameters = new String[11];
                                for (int i = 0; i < strParameters.length; i++) {
                                    strCommand = scan.nextLine();
                                    strParameters[i] = strCommand;
                                }
                                add(strParameters[0], strParameters[1], strParameters[2], strParameters[3], strParameters[4], strParameters[5], strParameters[6], strParameters[7], strParameters[8], strParameters[9], strParameters[10]);
                                break;
                            case "update_id":
                                update(commands[1]);
                                break;
                            case "remove_by_id":
                                remove_by_id(commands[1]);
                                break;
                            case "clear":
                                clear();
                                break;
                            case "save":
                                try {
                                    save();
                                } catch (Exception e) {
                                    System.out.println("Отсуствуют права для записи в файл");
                                }
                                break;
                            case "execute_script":
                                ex++;
                                if (ex == 2) {
                                    ex = 0;
                                    break;
                                } else {
                                    execute_script(commands[1]);
                                }
                                break;
                            case "remove_first":
                                remove_first();
                                break;
                            case "add_if_min":
                                add_if_min();
                                break;
                            case "remove_greater":
                                remove_greater(commands[1]);
                                break;
                            case "average_price":
                                average_of_price();
                                break;
                            case "count_by_owner":
                                String[] parameters = new String[4];
                                for (int i = 0; i < parameters.length; i++) {
                                    strCommand = scan.nextLine();
                                    parameters[i] = strCommand;
                                }
                                count_by_owner(parameters[0], parameters[1], parameters[2], parameters[3]);
                                break;
                            case "count_less_than_price":
                                count_less_than_price(commands[1]);
                                break;
                            case "exit":
                                System.exit(0);
                                break;
                            default:
                                System.out.println("В скрипте указана неизвестная команда");
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("У команды отсутствует аргумент");
                    } catch (NoSuchElementException e) {
                        System.out.println("Недостаточно аргументов у команды");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файла по указанному пути не существует");
        }
    }

    /**
     * Метод удаляет первый элемент коллекции
     */
    public void remove_first(){
        if (!(collection.size() == 0)) {
            collection.remove();
            System.out.println("Первый элемент коллекции удален");
        } else {
            System.out.println("Коллекция пуста");
        }
    }

    /**
     * Метод добавляет элемент в коллекцию, если значение поля price меньше минимального значения среди всех элементов
     */
    public void add_if_min() {
        if (!(collection.size() == 0)) {
            Integer minPrice = 32767;
            for (Product p : collection) {
                if (p.getPrice() < minPrice) {
                    minPrice = p.getPrice();
                }
            }
            Integer finalMinPrice = minPrice;
            PriorityQueue<Product> collectionCopy = new PriorityQueue<>(collection);
            collection.clear();
            add();
            if (collection.removeIf(collection -> collection.getPrice() >= finalMinPrice)) {
                collection.addAll(collectionCopy);
                System.out.println("Цена элемента выше, чем минимальная цена элементов коллекции. Элемент не сохранен ");
            } else {
                collection.addAll(collectionCopy);
                System.out.println("Элемент сохранен в коллекцию");
            }
        } else {
            System.out.println("Коллекция пуста");
        }
    }

    /**
     * Метод удаляет элементы коллекции, если значение их поля price блольше указанного
     *
     * @param strEl
     */
    public void remove_greater(String strEl){
        try {
            if (!(collection.size() == 0)) {
                int oldSize = collection.size();
                Integer price = Integer.parseInt(strEl);
                if (collection.removeIf(collection -> collection.getPrice() > price)) {
                    int newSize = oldSize - collection.size();
                    if (newSize == 1) {
                        System.out.println("Был удален один элемент коллекции");
                    } else {
                        System.out.println("Было удалено " + newSize + " элемент(а/ов) коллекции");
                    }
                } else {
                    System.out.println("Коллекция не изменена, так как price всех элементов меньше введенного");
                }
            } else {
                System.out.println("Коллекция пуста");
            }
        } catch (NumberFormatException e) {
            System.out.println("Введенное значение не является или выходит за пределы int. Повторите ввод");
        }
    }

    /**
     * Метод выводит среднее значение поля price элементов коллекции
     */
    public void average_of_price(){
        double averagePrice = 0.0;
        if (!(collection.size() == 0)) {
            for (Product p: collection){
                averagePrice = averagePrice + p.getPrice();
            }
            averagePrice = averagePrice / collection.size();
            System.out.println("Среднее значение цены всех элементов коллекции - " + averagePrice);
        } else {
            System.out.println("Коллекция пуста");
        }
    }

    /**
     * Метод считает элементы коллекции, значение поля owner которых совпадает с введенным
     *
     * @param strName
     * @param strWeight
     * @param strEyeColor
     * @param strNationality
     */
    public void count_by_owner(String strName, String strWeight, String strEyeColor, String strNationality) {

        String personName;
        if (strName.equals("")) {
            System.out.println("Имя владельца не может быть null");
            return;
        } else {
            personName = strName;
        }

        double weight;
        try {
            if ((strWeight.equals("")) || (Double.parseDouble(strWeight) <= 0)) {
                System.out.println("Вес не может быть null или меньше/равен 0");
                return;
            } else {
                weight = Double.parseDouble(strWeight);
            }
        } catch (NumberFormatException e) {
            System.out.println("Введенное значение не является или выходит за пределы double");
            return;
        }

        Color eyeColor;
        try {
            eyeColor = Color.valueOf(strEyeColor.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Введенного цвета не существует");
            return;
        } catch (NullPointerException e) {
            System.out.println("Цвет не может быть null");
            return;
        }

        Country nationality;
        try {
            nationality = Country.valueOf(strNationality.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Введенной страны не существует");
            return;
        } catch (NullPointerException e) {
            System.out.println("Страна не может быть null");
            return;
        }

        Person ownerEq = new Person(personName, weight, eyeColor, nationality);

        if (!(collection.size() == 0)) {
            int count = 0;
            for (Product p: collection){
                if (p.getOwner().equals(ownerEq)){
                    count++;
                }
            }
            System.out.println("Найдено " + count + " элемент(а/ов), значение поля owner которых совпадает с введенным");
        } else {
            System.out.println("Коллекция пуста");
        }
    }

    /**
     * Метод считает элементы коллекции, значение поля price которых меньше введенного
     *
     * @param strPrice
     */
    public void count_less_than_price(String strPrice){
        int count = 0;
        if (!(collection.size() == 0)) {
            try {
                Integer price = Integer.parseInt(strPrice);
                for (Product p: collection){
                    if (p.getPrice() < price) {
                        count ++;
                    }
                }
                System.out.println("Найдено " + count + " элемент(а/ов), значение цены котор(ых/ого) меньше " + price);
            } catch (NumberFormatException e) {
                System.out.println("Введенное значение не является или выходит за пределы int. Повторите ввод");
            }
        } else {
            System.out.println("Коллекция пуста");
        }
    }

    /**
     * Метод обеспечивает загрузку и парсинг файла
     *
     * @param file
     * @throws IOException
     */
    public void load(File file) throws IOException {
        try {
            if (!file.exists()) throw new FileNotFoundException();
        } catch (FileNotFoundException ex) {
            System.out.println("Файла по указанному пути не существует");
        }
        try {
            if (!file.canRead() || !file.canWrite()) throw new SecurityException();
        } catch (SecurityException ex) {
            System.out.println("Файл защищён от чтения и/или записи. Для работы программы нужны оба разрешения");
        }
        int startSize = collection.size();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            System.out.println("Файл успешно добавлен");
            StringBuilder result = new StringBuilder();
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                result.append(nextLine);
            }
            Type collectionQueue = new TypeToken<PriorityQueue<Product>>() {
            }.getType();
            Type collectionList = new TypeToken<List<Product>>() {
            }.getType();
            try{
                List<Product> productList = json.fromJson(String.valueOf(result), collectionList);
                try{
                    for (Product p: productList) {
                        if (p.getId() == null) {
                            System.out.println("Id не может быть null");
                            return;
                        }
                        if (p.getId() <= 0) {
                            System.out.println("Id не может быть меньше или равен 0. Добавлена пустая коллекция");
                            return;
                        }
                        if (p.getName() == null) {
                            System.out.println("Имя не может быть null");
                            return;
                        }
                        if (p.getName().equals("")) {
                            System.out.println("Строка имени не может быть пустой. Добавлена пустая коллекция");
                            return;
                        }
                        if (p.getCoordinates().getX() > 857) {
                            System.out.println("Координата X не может быть больше 857. Добавлена пустая коллекция");
                            return;
                        }
                        if (p.getCoordinates().getY() == null) {
                            System.out.println("Координата Y не может быть null");
                            return;
                        }
                        if (p.getPrice() == null) {
                            System.out.println("Цена не может быть null");
                            return;
                        }
                        if (p.getPrice() <= 0) {
                            System.out.println("Цена не может быть меньше или равна 0. Добавлена пустая коллекция");
                            return;
                        }
                        if (p.getPartNumber() == null) {
                            System.out.println("Номер партии не может быть null");
                            return;
                        }
                        if ((p.getPartNumber().length() > 85) || (p.getPartNumber().length() < 15) || (p.getPartNumber().equals(""))) {
                            System.out.println("Строка с номером партии не может быть пустой и должна быть длиной от 15 до 85. Добавлена пустая коллекция");
                            return;
                        }
                        if (p.getManufactureCost() == null) {
                            System.out.println("Цена производства не может быть null");
                            return;
                        }
                        if (p.getUnitOfMeasure() == null) {
                            System.out.println("Единица измерения не может быть null");
                            return;
                        }
                        if (p.getOwner().getName() == null) {
                            System.out.println("Имя владельца не может быть null");
                            return;
                        }
                        if (p.getOwner().getName().equals("")) {
                            System.out.println("Строка имени владельца не может быть пустой. Добавлена пустая коллекция");
                            return;
                        }
                        if (p.getOwner().getWeight() == null) {
                            System.out.println("Вес владельца не может быть null");
                            return;
                        }
                        if (p.getOwner().getWeight() <= 0) {
                            System.out.println("Вес владельца не может быть меньше или равен 0. Добавлена пустая коллекция");
                            return;
                        }
                        if (p.getOwner().getEyeColor() == null) {
                            System.out.println("Цвет глаз владельца не может быть null");
                            return;
                        }
                        if (p.getOwner().getNationality() == null) {
                            System.out.println("Национальность владельца не может быть null");
                            return;
                        }
                    }
                    PriorityQueue<Product> priorityQueue = json.fromJson(result.toString(), collectionQueue);
                    for (Product p: priorityQueue) {
                        p.setCreationDate();
                        collection.add(p);
                    }
                    System.out.println("Коллекция успешно добавлена. Коллекция содержит " + (collection.size() - startSize) + " элемент(а/ов)");
                } catch (NullPointerException e) {
                    System.out.println("Добавлена пустая коллекция");
                }
            } catch (JsonSyntaxException e) {
                System.out.println("Ошибка синтаксиса Json. Коллекция не добавлена");
                System.exit(1);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
