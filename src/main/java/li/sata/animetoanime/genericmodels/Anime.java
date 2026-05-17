package li.sata.animetoanime.genericmodels;

public class Anime {
    public String title;
    public String imageUrl;
    public int id;

    Anime() {}

    public Anime(pw.mihou.jaikan.models.Anime jaikanAnime) {
        this.title = jaikanAnime.title;
        this.imageUrl = jaikanAnime.images.firstDefault();
        this.id = jaikanAnime.id;
    }
}
