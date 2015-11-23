package bd_conexao;

import java.util.Comparator;

import org.bson.Document;

public class ComparatorNota implements Comparator<Document> {

	@Override
	public int compare(Document o1, Document o2) {
		if(Float.parseFloat((String) o2.get("nota").toString()) < (Float.parseFloat(o1.get("nota").toString()))){
			return -1;
		}
		if(Float.parseFloat((String) o2.get("nota").toString()) < (Float.parseFloat(o1.get("nota").toString()))){
			return 1;
		}
		
		return 0;
	}
}




