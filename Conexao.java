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
	static int done;
	static String livro;
	static String filme;
	static String urlFilme;
	static String urlLivro;
	
	
	public static void init(String id) throws ParseException {
		MongoClient mongoclient = new MongoClient();
		MongoDatabase db = mongoclient.getDatabase("banco");

		Scanner input = new Scanner(System.in);
		
		//String id = input.nextLine();
		String idpessoa;
	
		
		//MongoCollection<Document> col_pessoas = db.getCollection("pessoa");
		MongoCollection<Document> col_pessoas =  db.getCollection("pessoas");
		MongoCollection<Document> col_livros =  db.getCollection("livros");
		idpessoa = getPessoaSemelhante(col_pessoas, id);
		
		urlFilme = null;
		urlLivro = null;
		
	
		done = 0;
		livro = getIndicacaoLivro(col_pessoas, idpessoa,id, col_livros);
		done = 1;
		//System.out.println(livro);
		
		filme = getIndicacaoFilme(col_pessoas,idpessoa, id);
		/*System.out.println(filme);*/
		//((Document)((ArrayList) cursor.next().get("ids")).get(0)).get("fator")
		//calculaSimilaridade(col_pessoas);
		mongoclient.close();		
	}
	public static void getLivroBaseado(MongoDatabase db, String id,MongoCollection<Document> col_pessoas){
<<<<<<< HEAD

=======
//		ArrayList final_list;
//		Document pessoa_atual = col_pessoas.find(eq("id",id)).first();
//		ArrayList filmes = (ArrayList)pessoa_atual.get("filme");
//		ComparatorNota comparatorfilme = new ComparatorNota();
//		Collections.sort(filmes, comparatorfilme);
//		
//	
//		ArrayList autores = (ArrayList)((Document) filmes.get(0)).get("autor");
//	
//		MongoCollection<Document> col_livros =  db.getCollection("livros");
//		MongoCursor<Document> cursor_livro = col_livros.find().iterator();
//		while(cursor_livro.hasNext()){
//			Document l = cursor_livro.next();
//			ArrayList livros_autores = (ArrayList)l.get("autor");
//			for(int i = 0; i < livros_autores.size(); i++){
//				if(((String)livros_autores.get(i)).equals((String)(autores.get(0)))){
//					System.out.println(l.get("titulo"));				
//				}
//			}
//		}
>>>>>>> 7a3b76dd86a9b62d275e5c65b7c6ed3a0ae9096c
		
        Document pessoa_atual = col_pessoas.find(eq("id",id)).first();
        if (pessoa_atual != null){
            // Ordena filmes por nota
            ArrayList filmes = (ArrayList)pessoa_atual.get("filme");
            Collections.sort(filmes, new ComparatorNota());
            
            //Obtendo array de autores de livros nao lidos
            ArrayList livrosDeFilmes = new ArrayList();
            
            int end ;
            if (filmes != null){
                if (filmes.size() > 10){
                    end = 5;
                } else {
                    end = filmes.size();
                }
                
                MongoCollection<Document> col_livros =  db.getCollection("livros");
                // Percorre os 5 filmes mais bem avaliados verificando se existem livros para recomendar a partir deles
                for (int i = 0; i < end; i++){
                    //imdb_id do i-esimo filme mais bem avaliado pelo usuario
                    String imdb_id_filme =  (String) ((Document)filmes.get(i)).get("imdb_id");
                    
                    //Procura o filme avaliado na colecao de filmes
                    Document filme_info =  db.getCollection("filmes").find(eq("imdb_id", imdb_id_filme)).first();
                    if (filme_info != null){
                        
                        //Obtem lista de autores do livro que originou o filme
                        ArrayList autorLivro = (ArrayList)((Document) filme_info).get("autor");
                        if (autorLivro != null && autorLivro.size() > 0){
                            //Obtem o nome do autor principal
                            String autorNome = (String) autorLivro.get(0);
                            
                            //Procura pelos livros desse autor
                            MongoCursor<Document> cursor_livro = col_livros.find().iterator();
                            while(cursor_livro.hasNext()){
                                Document l = cursor_livro.next();
                                ArrayList autores_livro = (ArrayList)l.get("autor");
                                if (autores_livro != null){
                                    for(int j = 0; i < autores_livro.size(); i++){
                                        if(((String)autores_livro.get(j)).equals(autorNome)){
                                            livrosDeFilmes.add(l);
                                            System.out.println(l.get("titulo"));				
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                }
            }
        }
		
		
	}
	public static String getPessoaSemelhante(MongoCollection<Document> col_pessoas, String id){
		//Procura por pessoa que deseja recomendacao
		Document pessoa_atual = col_pessoas.find(eq("id",id)).first();
		//Salva uma lista de todos os similares
		ArrayList list_semelhante = (ArrayList) pessoa_atual.get("similar");
		ComparatorDoc comparator = new ComparatorDoc();
		//Ordena lista de similares de maneira decrescente 
		Collections.sort(list_semelhante, comparator);
		
		//Retorna primeiro elemento da lista (mais similar)
		return (String) ((Document)list_semelhante.get(0)).get("id");
	
	}
	public static String getIndicacaoLivro(MongoCollection<Document> col_pessoas, String idpessoa, String id,MongoCollection<Document> col_livros){
		int encontrou = 0,nLivros = 0;
		ArrayList<String> LivroRecomendacao = new ArrayList();
		//Encontra pessoa com id informado
		Document pessoa_atual = col_pessoas.find(eq("id",id)).first();
		//Salva array de livros da pessoa que sera gerada a recomendacao
		ArrayList livros_lidos = (ArrayList)pessoa_atual.get("livro");

		//Encontra a pessoa similar
		Document pessoa_similar = col_pessoas.find(eq("id",idpessoa)).first();
		//Salva array de livros da pessoa similar
		ArrayList list_livro = (ArrayList)pessoa_similar.get("livro");
		
		ComparatorNota comparatorlivro = new ComparatorNota();
		//Ordena de maneira decrescente de acordo com a nota os livros da pessoa similar
		Collections.sort(list_livro, comparatorlivro);

		//Percorre os arrays de livros buscando os livros da pessoa similar que a pessoa
		//para a qual a recomendacao sera gerada e salva os que ela ainda nao leu
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
		String resp = new String();
		resp = "";
		for (String f : (ArrayList<String>) LivroRecomendacao){
			resp = resp + "- " + f + "\n";
		}

		return resp;
		//return LivroRecomendacao.toString();

	  
		
	}
	public static String getIndicacaoFilme(MongoCollection<Document> col_pessoas, String idpessoa, String id){
		int encontrou = 0, nFilmes = 0;
		ArrayList FilmeRecomendacao = new ArrayList();
		//Encontra pessoa com id informado
		Document pessoa_atual = col_pessoas.find(eq("id",id)).first();
		//Salva array de filmes da pessoa similar
		ArrayList filmes_vistos = (ArrayList)pessoa_atual.get("filme");
		

		//Encontra a pessoa similar
		Document pessoa_similar = col_pessoas.find(eq("id",idpessoa)).first();
		//Salva array de filmes da pessoa similar
		ArrayList list_filme = (ArrayList)pessoa_similar.get("filme");
	
		
		ComparatorNota comparatorlivro = new ComparatorNota();
		//Ordena de maneira decrescente de acordo com a nota os filmes da pessoa similar
		Collections.sort(list_filme, comparatorlivro);
	
		//Percorre os arrays de filmes buscando os filmes da pessoa similar que a pessoa
		//para a qual a recomendacao sera gerada e salva os que ela ainda nao viu
		for(int i = 0; i <list_filme.size(); i++){
			for(int j = 0; j < filmes_vistos.size();j++){
				if(((String) ((Document)filmes_vistos.get(j)).get("imdb_id")).equals((String) ((Document)list_filme.get(i)).get("imdb_id"))){
					encontrou = 1;
				}	
			}
			if(encontrou == 0 && nFilmes < 6){
				FilmeRecomendacao.add((String) ((Document)list_filme.get(i)).get("nome"));
				if (urlFilme == null){
					urlFilme = "www.imdb.com/title/" + (String) ((Document)list_filme.get(i)).get("imdb_id");
				}
				nFilmes++;
			}
			encontrou = 0;
		}
		String resp = new String();
		resp = "";
		for (String f : (ArrayList<String>)FilmeRecomendacao){
			resp = resp + "- " + f + "\n";
		}

		return resp;
		//System.out.println(FilmeRecomendacao.toString());
		//return null;
		//return FilmeRecomendacao.toString();
		
		
	}
	public static void calculaSimilaridade(MongoCollection<Document> col_pessoas){
		Float somaNotas =  0.0f, media = 0.0f, nfilmesuniao = 0.0f, nfilmesinter = 0.0f;
		Float fator = 0.0f;
		
		
		MongoCursor<Document> cursor = col_pessoas.find().iterator();
		//Percorre todas as pessoas da colecao
		while(cursor.hasNext()){
			Document pessoa  = cursor.next();
			//Salva pessoa para a qual sera calculado o fator de similaridade
			String id = pessoa.getString("id");
			//Salva array de filmes ja assistiu
			ArrayList filmes = (ArrayList)pessoa.get("filme");
			//numero de filmes que pessoa assistiu
			int nfilmes = filmes.size(); 
			//se o numero de filmes for diferente de zero
			if(nfilmes  != 0){
				//procura qualquer pessoa diferente da atual
				MongoCursor<Document> cursorpessoas = col_pessoas.find(ne("id",id)).iterator();
				//itera sobre ela
				while(cursorpessoas.hasNext()){
					Document proxima_pessoa = cursorpessoas.next();
					//Salva seu id
					String id_proximo = proxima_pessoa.getString("id");
					//Salva array de filmes ja vistos
					ArrayList filmes_pessoa_dois = (ArrayList)proxima_pessoa.get("filme");
					if(filmes_pessoa_dois.size() != 0){
						somaNotas = 0.0f;
						nfilmesuniao = 0.0f;
						nfilmesinter = 0.0f;
						fator = 0.0f;
						media = 0.0f;
						for(int i = 0; i < filmes.size(); i++){
							for(int j = 0; j < filmes_pessoa_dois.size();j++){
								//Procura filme que ambas as pessoas assistiram					
								if(((String) ((Document)filmes_pessoa_dois.get(j)).get("imdb_id")).equals((String) ((Document)filmes.get(i)).get("imdb_id"))){
									//Incrementa intersecao de filmes
									nfilmesinter = nfilmesinter + 1;
									//Soma as diferencas entre as notas
									somaNotas = somaNotas + Math.abs(Float.parseFloat(((String) ((Document)filmes_pessoa_dois.get(j)).get("nota"))) - Float.parseFloat((String) ((Document)filmes.get(i)).get("nota")));
								
								}	
							}
						}
						//Calcula a uniao dos filmes
						nfilmesuniao = filmes.size() + filmes_pessoa_dois.size() - nfilmesinter;
						//Calcula a media das notas
						if(nfilmesinter != 0){
							media = somaNotas/nfilmesinter;	
						}
						//Calcula fator de similaridade
						if(nfilmesuniao != 0 && nfilmesinter != 0){
							fator = (nfilmes/nfilmesuniao)*(1 - (media/10));
						}else{
							fator = 0.0f;
						}
						//Atualiza vetor similar
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


