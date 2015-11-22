package bd_conexao;
import com.mongodb.client.model.Sorts.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;
import static java.util.Arrays.asList;

import java.net.UnknownHostException;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;

import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Projections;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class Conexao {

	public static void main(String[] args) throws ParseException {
		MongoClient mongoclient = new MongoClient();
		MongoDatabase db = mongoclient.getDatabase("bd");

		Scanner input = new Scanner(System.in);
		System.out.println("Digite seu nome: ");
		String nome = input.nextLine();
		String idpessoa, livro;
	
		
		MongoCollection<Document> col_pessoas = db.getCollection("pessoa");
		
		idpessoa = getPessoaSemelhante(col_pessoas, nome);
		//ArrayList livros_lidos = (ArrayList)cursor.next().get("livros");
		
		livro = getIndicacaoLivro(col_pessoas, idpessoa,nome);

		System.out.println(livro);
		//((Document)((ArrayList) cursor.next().get("ids")).get(0)).get("fator")
		mongoclient.close();		
	}
	
	public static String getPessoaSemelhante(MongoCollection<Document> col_pessoas, String nome){
		
		MongoCursor<Document> cursor = col_pessoas.find(new Document("nome", nome)).iterator();
		ArrayList list_semelhante = (ArrayList) cursor.next().get("ids");
		ComparatorDoc comparator = new ComparatorDoc();
		Collections.sort(list_semelhante, comparator);
		return (String) ((Document)list_semelhante.get(0)).get("idp");
	
	}
	public static String getIndicacaoLivro(MongoCollection<Document> col_pessoas, String idpessoa, String nome){
		MongoCursor<Document> cursor = col_pessoas.find(new Document("nome", nome)).iterator();
		ArrayList livros_lidos = (ArrayList)cursor.next().get("livro");
		
		MongoCursor<Document> cursor_semelhante = col_pessoas.find(new Document("id", idpessoa)).iterator();
		ArrayList list_livro = (ArrayList) cursor_semelhante.next().get("livro");
		ComparatorNota comparatorlivro = new ComparatorNota();
		Collections.sort(list_livro, comparatorlivro);
	
		for(int i = 0; i < list_livro.size(); i++){
			if(!livros_lidos.contains((String) ((Document)list_livro.get(i)).get("titulo"))){
				return (String) ((Document)list_livro.get(i)).get("titulo");
			}
		}
		return null;

	  
		
	}
	public static String getIndicacaoFilme(MongoCollection<Document> col_pessoas, String idpessoa){
		
		MongoCursor<Document> cursor = col_pessoas.find(new Document("id", idpessoa)).iterator();
		ArrayList list_filme = (ArrayList) cursor.next().get("filme");
		ComparatorNota comparatorfilme = new ComparatorNota();
		Collections.sort(list_filme, comparatorfilme);
		return (String) ((Document)list_filme.get(0)).get("titulo");
		
	}
	
}


