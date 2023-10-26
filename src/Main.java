import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.google.gson.*;
import java.util.function.Predicate;


class Composition {
    private String title;
    private String genre;
    private String artist;
    private String lyrics;
    private Date creationDate;
    private Double duration;
    private String format;
    private Map<String, Double> ratings;

    public Composition() {
        ratings = new HashMap<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Map<String, Double> getRatings() {
        return ratings;
    }

    public void addRating(String propertyName, double value) {
        ratings.put(propertyName, value);
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0;
        }

        return ratings.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    @Override
    public String toString() {
        return "Composition(Назва='" + title + "', Жанр='" + genre + "', Артист='" + artist + "', Текст='" +
                lyrics + "', Дата створення='" + creationDate + "', Тривалість=" + duration + ", Формат='" +
                format + "', Рейтинг=" + getAverageRating() + ")";
    }
}

class MyLinkedList<T> extends ArrayList<T> {
    public boolean removeIf(Predicate<? super T> filter) {
        return super.removeIf(filter);
    }
}


 class Program {
    private static MyLinkedList<Composition> compositions = new MyLinkedList<>();
    private static final String fileName = "compositions.json";

    public static void main(String[] args) {
        loadCompositions();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Меню:");
            System.out.println("1. Додайте нову композицію");
            System.out.println("2. Перегляньте список композицій");
            System.out.println("3. Знайдіть пісню");
            System.out.println("4. Редагуйте композицію");
            System.out.println("5. Сортуйте композиції за назвою");
            System.out.println("6. Сортуйте композиції за виконавцем");
            System.out.println("7. Сортуйте композиції за середнім рейтингом");
            System.out.println("8. Видаліть композицію");
            System.out.println("9. Вийдіть з програми");
            System.out.print("Виберіть опцію: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addComposition();
                    break;

                case 2:
                    displayCompositions();
                    break;

                case 3:
                    search();
                    break;

                case 4:
                    editComposition();
                    break;

                case 5:
                    compositions.sort(Comparator.comparing(Composition::getTitle, String.CASE_INSENSITIVE_ORDER));
                    System.out.println("Композиції відсортовані за назвою.");
                    break;

                case 6:
                    compositions.sort(Comparator.comparing(Composition::getArtist, String.CASE_INSENSITIVE_ORDER));
                    System.out.println("Композиції відсортовані за виконавцем.");
                    break;

                case 7:
                    compositions.sort(Comparator.comparing(Composition::getAverageRating, Comparator.reverseOrder()));
                    System.out.println("Композиції відсортовані за середнім рейтингом (у порядку спадання).");
                    break;

                case 8:
                    removeComposition();
                    break;

                case 9:
                    saveCompositions();
                    System.out.println("Програму припинено.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Невірний вибір. Спробуйте знову.");
                    break;
            }
        }
    }

    private static void removeComposition() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть назву композиції, яку бажаєте видалити: ");
        String compositionTitle = scanner.nextLine();

        compositions.removeIf(composition -> composition.getTitle().equalsIgnoreCase(compositionTitle));
        System.out.println("Композиція з назвою '" + compositionTitle + "' видалена.");
    }

    private static void editComposition() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть назву композиції, яку бажаєте відредагувати: ");
        String compositionTitle = scanner.nextLine();

        Composition compositionToEdit = compositions.stream()
                .filter(composition -> composition.getTitle().equalsIgnoreCase(compositionTitle))
                .findFirst()
                .orElse(null);

        if (compositionToEdit == null) {
            System.out.println("Композиція з назвою '" + compositionTitle + "' не знайдена.");
            return;
        }

        System.out.println("Введіть нові дані для композиції:");

        System.out.print("Назва: ");
        compositionToEdit.setTitle(scanner.nextLine());

        System.out.print("Жанр: ");
        compositionToEdit.setGenre(scanner.nextLine());

        System.out.print("Артист: ");
        compositionToEdit.setArtist(scanner.nextLine());

        System.out.print("Текст: ");
        compositionToEdit.setLyrics(scanner.nextLine());

        System.out.print("Дата створення (YYYY-MM-DD): ");
        Date creationDate = null;
        while (creationDate == null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = scanner.nextLine();
                creationDate = dateFormat.parse(dateString);
            } catch (ParseException e) {
                System.out.print("Недійсний формат дати. Будь ласка, використовуйте YYYY-MM-DD: ");
            }
        }
        compositionToEdit.setCreationDate(creationDate);




        System.out.print("Тривалість (у хвилинах): ");
        double duration;
        while (true) {
            try {
                duration = Double.parseDouble(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Недійсний формат тривалості. Введіть дійсний номер: ");
            }
        }
        compositionToEdit.setDuration(duration);

        System.out.print("Формат: ");
        compositionToEdit.setFormat(scanner.nextLine());

        System.out.print("Введіть новий рейтинг: ");
        double rating;
        while (true) {
            try {
                rating = Double.parseDouble(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Недійсний формат оцінки. Введіть дійсний номер: ");
            }
        }
        compositionToEdit.addRating("rating", rating);

        System.out.println("Композицію відредаговано.");
    }

    private static void loadCompositions() {
        File file = new File(fileName);
        if (file.exists()) {
            try {
                Scanner fileScanner = new Scanner(file);
                StringBuilder json = new StringBuilder();
                while (fileScanner.hasNextLine()) {
                    json.append(fileScanner.nextLine());
                }

                compositions = new MyLinkedList<>();
                compositions.addAll(Arrays.asList(JsonConverter.fromJson(json.toString(), Composition[].class)));
                System.out.println("Дані успішно завантажено з compositions.json");
            } catch (IOException e) {
                System.out.println("Помилка під час завантаження даних із compositions.json: " + e.getMessage());
            }
        } else {
            System.out.println("compositions.json не знайдено.");
        }
    }


    private static void addComposition() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть деталі композиції:");

        System.out.print("Назва: ");
        String title = scanner.nextLine();

        System.out.print("Жанр: ");
        String genre = scanner.nextLine();

        System.out.print("Артист: ");
        String artist = scanner.nextLine();

        System.out.print("Текст: ");
        String lyrics = scanner.nextLine();

        System.out.print("Дата створення (YYYY-MM-DD): ");
        Date creationDate = null;
        while (creationDate == null) {
            try {
                String dateString = scanner.nextLine();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                creationDate = dateFormat.parse(dateString);
            } catch (Exception e) {
                System.out.print("Недійсний формат дати. Будь ласка, використовуйте YYYY-MM-DD: ");
            }
        }

        System.out.print("Тривалість (у хвилинах): ");
        double duration;
        while (true) {
            try {
                duration = Double.parseDouble(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Недійсний формат тривалості. Введіть дійсний номер: ");
            }
        }

        System.out.print("Формат: ");
        String format = scanner.nextLine();

        Composition composition = new Composition();
        composition.setTitle(title);
        composition.setGenre(genre);
        composition.setArtist(artist);
        composition.setLyrics(lyrics);
        composition.setCreationDate(creationDate);
        composition.setDuration(duration);
        composition.setFormat(format);

        System.out.print("Введіть рейтинг: ");
        double rating;
        while (true) {
            try {
                rating = Double.parseDouble(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Недійсний формат оцінки. Введіть дійсний номер: ");
            }
        }

        composition.addRating("rating", rating);

        compositions.add(composition);
        System.out.println("Композиція додана.");
    }

    private static void displayCompositions() {
        compositions.forEach(System.out::println);
    }

    private static void search() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть назву пісні або текст для пошуку: ");
        String searchPattern = scanner.nextLine();

        System.out.println("Список композицій, які містять слова, пов'язані із '" + searchPattern + "':");

        List<Composition> matchingCompositions = compositions.stream()
                .filter(composition -> Pattern.compile(searchPattern, Pattern.CASE_INSENSITIVE)
                        .matcher(composition.getTitle() + composition.getLyrics())
                        .find())
                .collect(Collectors.toList());

        matchingCompositions.forEach(System.out::println);
    }

    private static void saveCompositions() {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(JsonConverter.toJson(compositions.toArray(new Composition[0])));
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Помилка під час збереження даних в compositions.json: " + e.getMessage());
        }
    }
}

class JsonConverter {
    public static String toJson(Object obj) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }
}

