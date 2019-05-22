package hangmanshared.models.enums;

import java.io.Serializable;

public enum Difficulty implements Serializable {

    EASY(15),
    MEDIUM(15),
    HARD(5);

    int mistakes;

    Difficulty(int mistakes) {
        this.mistakes = mistakes;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }
}
