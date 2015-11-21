package bd_conexao;

import java.util.Comparator;

import org.bson.Document;

public class ComparatorDoc implements Comparator<Document> {

	@Override
	public int compare(Document o1, Document o2) {
		return  (Integer.parseInt((String) o2.get("fator").toString()))- (Integer.parseInt(o1.get("fator").toString()));
	}
}





	
