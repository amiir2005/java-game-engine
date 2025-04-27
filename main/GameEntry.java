import java.io.IOException;
import java.util.Scanner;
import java.util.function.Consumer;

public class GameEntry {
    private final String name;
    private final Consumer<Scanner> launcher;

    public GameEntry(String name, Consumer<Scanner> launcher) {
        this.name = name;
        this.launcher = launcher;
    }

    public String getName() {
        return name;
    }

    public void launch(Scanner sc) {
        launcher.accept(sc);
    }
}
