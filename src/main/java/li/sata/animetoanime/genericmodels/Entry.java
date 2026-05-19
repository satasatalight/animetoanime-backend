package li.sata.animetoanime.genericmodels;

public class Entry {
    public String name;
    public String imageUrl;
    public int id;

    Entry() {}

    public boolean equals(Entry other) {
        return this.id == other.id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Entry) {
            return equals((Entry) obj);
        }
        return super.equals(obj);
    }
}
