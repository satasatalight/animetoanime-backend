package li.sata.animetoanime.genericmodels;

import jakarta.persistence.Embeddable;

@Embeddable
public class Character {
    public String name;
    public String imageUrl;

    public Character(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
