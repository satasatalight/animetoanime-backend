package li.sata.animetoanime.jaikanmodels;

import com.google.gson.annotations.SerializedName;
import pw.mihou.jaikan.models.images.MediaTypes;

public class JaikanShortAnime {
    @SerializedName("mal_id")
    public int id;
    public String url;
    public MediaTypes images;
    public String title;
}
