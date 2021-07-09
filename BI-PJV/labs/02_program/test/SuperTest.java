import cz.kvetinac97.entity.Notebook;
import static cz.kvetinac97.Category.*;

// Run test
class SuperTest {

    public static void main (String[] args) {
        Notebook[] notebooks = new Notebook[]{
            new Notebook("Lenovo Yoga", 2000, BASIC).setPrice(12000),
            new Notebook("HP Bussiness", 50000, PROFESSIONAL),
            new Notebook("Dell XP", 40000, GAMING).setPrice(35600)
        };

        for (Notebook notebook : notebooks)
            System.out.printf("%s %d %s\n", notebook.getName(),
                    notebook.getPrice(), notebook.getCategory());
    }

}
