package bd_conexao;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.ne;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Conexao {

	public static void main(String[] args) throws ParseException {
		MongoClient mongoclient = new MongoClient();
		MongoDatabase db = mongoclient.getDatabase("banco");

		Scanner input = new Scanner(System.in);
		System.out.println("Digite seu nome: ");
		String id = input.nextLine();
		String idpessoa, livro;
	
		
		//MongoCollection<Document> col_pessoas = db.getCollection("pessoa");
		MongoCollection<Document> col_pessoas =  db.getCollection("pessoas");
		idpessoa = getPessoaSemelhante(col_pessoas, id);
		System.out.println(idpessoa);
		//idpessoa = "279855";
	
		livro = getIndicacaoLivro(col_pessoas, idpessoa,id);

		//System.out.println(livro);
		
		getIndicacaoFilme(col_pessoas,idpessoa, id);
		/*System.out.println(filme);*/
		//((Document)((ArrayList) cursor.next().get("ids")).get(0)).get("fator")
		//calculaSimilaridade(col_pessoas);
		mongoclient.close();		
	}
	public static void getLivroBaseado(MongoDatabase db, String id,MongoCollection<Document> col_pessoas){
		ArrayList final_list;
		Document pessoa_atual = col_pessoas.find(eq("id",id)).first();
		ArrayList filmes = (ArrayList)pessoa_atual.get("filme");
		ComparatorNota comparatorfilme = new ComparatorNota();
		Collections.sort(filmes, comparatorfilme);
		
		ArrayList autores = (ArrayList)((Document) filmes.get(0)).get("autor");
	
		MongoCollection<Document> col_livros =  db.getCollection("livros");
		MongoCursor<Document> cursor_livro = col_livros.find().iterator();
		while(cursor_livro.hasNext()){
			Document l = cursor_livro.next();
			ArrayList livros_autores = (ArrayList)l.get("autor");
			for(int i = 0; i < livros_autores.size(); i++){
				if(((String)livros_autores.get(i)).equals((String)(autores.get(0)))){
					System.out.println(l.get("titulo"));				
				}
			}
		}
		
		
		
	}
	public static String getPessoaSemelhante(MongoCollection<Document> col_pessoas, String id){
		Document pessoa_atual = col_pessoas.find(eq("id",id)).first();
		System.out.println(pessoa_atual);
		ArrayList list_semelhante = (ArrayList) pessoa_atual.get("similar");
		ComparatorDoc comparator = new ComparatorDoc();
		Collections.sort(list_semelhante, comparator);
		System.out.println(list_semelhante);
	
		return (String) ((Document)list_semelhante.get(0)).get("id"); //mudar para o que tiver no documento
	
	}
	public static String getIndicacaoLivro(MongoCollection<Document> col_pessoas, String idpessoa, String id){
		int encontrou = 0,nLivros = 0;
		ArrayList<String> LivroRecomendacao = new ArrayList();
		Document pessoa_atual = col_pessoas.find(eq("id",id)).first();
		ArrayList livros_lidos = (ArrayList)pessoa_atual.get("livro");
		//System.out.println(livros_lidos);
	
		Document pessoa_similar = col_pessoas.find(eq("id",idpessoa)).first();
		ArrayList list_livro = (ArrayList)pessoa_similar.get("livro");
	
		ComparatorNota comparatorlivro = new ComparatorNota();
		Collections.sort(list_livro, comparatorlivro);
		System.out.println(list_livro);
		
		
		
		for(int i = 0; i < list_livro.size(); i++){
			for(int j = 0; j < livros_lidos.size();j++){
				if(((String) ((Document)livros_lidos.get(j)).get("isbn")).equals((String) ((Document)list_livro.get(i)).get("isbn"))){
					encontrou = 1;
				}	
			}
			if(encontrou == 0 && nLivros < 6){
				LivroRecomendacao.add((String) ((Document)list_livro.get(i)).get("nome"));
				nLivros++;
			}	
			
			encontrou = 0;
		}
		System.out.println(LivroRecomendacao);
		return null;

	  
		
	}
	public static String getIndicacaoFilme(MongoCollection<Document> col_pessoas, String idpessoa, String id){
		int encontrou = 0, nFilmes = 0;
		ArrayList FilmeRecomendacao = new ArrayList();
		Document pessoa_atual = col_pessoas.find(eq("id",id)).first();
		ArrayList filmes_vistos = (ArrayList)pessoa_atual.get("filme");
		System.out.println(filmes_vistos);
	
		Document pessoa_similar = col_pessoas.find(eq("id",idpessoa)).first();
		ArrayList list_filme = (ArrayList)pessoa_similar.get("filme");
	
		ComparatorNota comparatorlivro = new ComparatorNota();
		Collections.sort(list_filme, comparatorlivro);
		System.out.println(list_filme);
		
		
		
		for(int i = 0; i <list_filme.size(); i++){
			for(int j = 0; j < filmes_vistos.size();j++){
				if(((String) ((Document)filmes_vistos.get(j)).get("imdb_id")).equals((String) ((Document)list_filme.get(i)).get("imdb_id"))){
					encontrou = 1;
				}	
			}
			if(encontrou == 0 && nFilmes < 6){
				FilmeRecomendacao.add((String) ((Document)list_filme.get(i)).get("nome"));
				nFilmes++;
			}
			encontrou = 0;
		}
		System.out.println(FilmeRecomendacao.toString());
		return null;
		
		
	}
	public static void calculaSimilaridade(MongoCollection<Document> col_pessoas){
		Float somaNotas =  0.0f, media = 0.0f, nfilmesuniao = 0.0f, nfilmesinter = 0.0f;
		Float fator = 0.0f;
		
		
		MongoCursor<Document> cursor = col_pessoas.find().iterator();
		
		while(cursor.hasNext()){
			Document pessoa  = cursor.next();
			String id = pessoa.getString("id");
			ArrayList filmes = (ArrayList)pessoa.get("filme");
			int nfilmes = filmes.size(); //numero de filmes que pessoa viu
			if(nfilmes  != 0){
				System.out.println("idddddd" + id);
				MongoCursor<Document> cursorpessoas = col_pessoas.find(ne("id",id)).iterator();
				while(cursorpessoas.hasNext()){
					Document proxima_pessoa = cursorpessoas.next();
					String id_proximo = proxima_pessoa.getString("id");
					ArrayList filmes_pessoa_dois = (ArrayList)proxima_pessoa.get("filme");
					if(filmes_pessoa_dois.size() != 0){
						somaNotas = 0.0f;
						nfilmesuniao = 0.0f;
						nfilmesinter = 0.0f;
						fator = 0.0f;
						media = 0.0f;
						for(int i = 0; i < filmes.size(); i++){
							for(int j = 0; j < filmes_pessoa_dois.size();j++){
								//sSystem.out.println(((String) ((Document)livros_lidos.get(j)).get("nome")));
							
								if(((String) ((Document)filmes_pessoa_dois.get(j)).get("imdb_id")).equals((String) ((Document)filmes.get(i)).get("imdb_id"))){
									nfilmesinter = nfilmesinter + 1;
									//System.out.println("interseccao" + nfilmesinter);
									//System.out.println("nota de 1 "+ Float.parseFloat(((String) ((Document)filmes_pessoa_dois.get(j)).get("nota"))));
									//System.out.println("nota de 2" + Float.parseFloat((String) ((Document)filmes.get(i)).get("nota")));
									//System.out.println("soma das notas antes" + somaNotas);
									//System.out.println("diferenca" + Math.abs(Float.parseFloat(((String) ((Document)filmes_pessoa_dois.get(j)).get("nota"))) - Float.parseFloat((String) ((Document)filmes.get(i)).get("nota"))));
								
									//System.out.println("nota de 1" + Integer.parseInt((String) ((Document)filmes_pessoa_dois.get(j)).get("nota")));
									somaNotas = somaNotas + Math.abs(Float.parseFloat(((String) ((Document)filmes_pessoa_dois.get(j)).get("nota"))) - Float.parseFloat((String) ((Document)filmes.get(i)).get("nota")));
								//System.out.println("soma das notas depois" + somaNotas);
								}	
							}
							//System.out.println("somaNotas" + somaNotas);
							//System.out.println("nfilmesinter" + nfilmesinter);
						}
						nfilmesuniao = filmes.size() + filmes_pessoa_dois.size() - nfilmesinter;
						//System.out.println("nfilmesuniao" + nfilmesuniao);
						
						if(nfilmesinter != 0){
							media = somaNotas/nfilmesinter;	
						}
						//System.out.println("media" + media);
						if(nfilmesuniao != 0 && nfilmesinter != 0){
							fator = (nfilmes/nfilmesuniao)*(1 - (media/10));
						}else{
							fator = 0.0f;
						}
						//System.out.println("id "+  id + " id_proximo " + id_proximo +"fator" + fator);
						
						BasicDBObject docToInsert = new BasicDBObject("fator", fator);
						docToInsert.put("id", id_proximo);
						BasicDBObject updateQuery = new BasicDBObject("id", id);
						BasicDBObject updateCommand = new BasicDBObject("$push", new BasicDBObject("similar", docToInsert));
						col_pessoas.updateOne(updateQuery, updateCommand);
					}
				}
			}
		}
	}
}


