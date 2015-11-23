package bd_conexao;

import java.util.Comparator;

import org.bson.Document;

public class ComparatorDoc implements Comparator<Document> {

	@Override
	public int compare(Document o1, Document o2) {
		if(Float.parseFloat((String) o2.get("fator").toString()) < Float.parseFloat(o1.get("fator").toString())){
			return -1;
		}
		if(Float.parseFloat((String) o2.get("fator").toString()) > Float.parseFloat(o1.get("fator").toString())){
			return 1;
		}
		return 0;
		//return  (Integer.parseInt((String) o2.get("fator").toString()))- (Integer.parseInt(o1.get("fator").toString()));
	}
}





	
