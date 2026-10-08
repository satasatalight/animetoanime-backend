package li.sata.animetoanime.genericmodels;
import java.io.Serializable;

// entry ids are duplicated across anime and staff
// using a combined key allows for more intuitive retrival 
// Ex. "Get anime {entryId} that staff {sourceId} worked on"
public class EntryCompositeKey implements Serializable{
    public int entryId;
    public int sourceId;
}
