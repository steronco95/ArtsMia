package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<ArtObject,DefaultWeightedEdge> grafo;
	private Map<Integer,ArtObject> idMap;
	
	public Model() {
		
		idMap = new HashMap<>();
		
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//creo istanza DAO
		
		ArtsmiaDAO dao = new ArtsmiaDAO();
		dao.listObjects(idMap);
		//da questo punto in avanti nella mappa idMap sono presenti tutti gli oggetti ArtObject
		
		
		//Aggiungo i vertici ---- sono tutti gli oggetti, ovvero tutte le righe della tabella object, presenti nella mappa idMap
		
		Graphs.addAllVertices(this.grafo,idMap.values());
		
		
		// Aggiungo gli archi ----- 
		
		//APPROCCIO 1 --- doppio ciclo for sui vertici, -> dati due vertici controllo con il Dao se sono collegati;
		
		
		//NON FUNZIONA BENE PERCHE' POCO PERFORMANTE CIO E DOVUTO AL FATTO CHE OGNI QUERY CI IMPIEGA DEL TEMPO ED E ESEGUITA ALL'INTERNO DI UN DOPPIO CICLO FOR
		//PERCIO' QUANDO CI SONO TANTI OGGETTI DA ITERARE IL TEMPO DIVENTA INFINITO!!  POSSO METTERLO IN PRATICA SOLO SE IL NUMERO DI VERTICI E' BASSO!
		
		/*for(ArtObject a1 : this.grafo.vertexSet()) {
			for(ArtObject a2 : this.grafo.vertexSet()) {
				
				//devo collegare a1 con a2??
				//controllo a priori se non esiste gia l'arco
				
					int peso = dao.getPeso(a1,a2);
				
				if(!this.grafo.containsEdge(a1, a2)) {
					
					if(peso > 0) {
						//inserisco il mio arco 
						
						Graphs.addEdge(this.grafo, a1, a2, peso);
			
			
				}
					}
						}
							}
		
		System.out.println(String.format("Grafo creato! #vertici %d, #Archi %d", this.grafo.vertexSet().size(), this.grafo.edgeSet().size()));
		
		*/
		
		//APPROCCIO 2 --- faccio una query più complicata che mi restituisce un po piu di informazioni
		//MI FACCIO DARE LA LISTA DEGLI ARCHI DIRETTAMENTE DAL DAO ---- ADIACENZE
		
		
		for(Adiacenza a : dao.getAdiacenze()) {
			
			if(a.getPeso()>0) {
				Graphs.addEdge(this.grafo, idMap.get(a.getObj1()), idMap.get(a.getObj2()), a.getPeso());
			}
			
			
		}
		
		
		System.out.println(String.format("Grafo creato! #vertici %d, #Archi %d", this.grafo.vertexSet().size(), this.grafo.edgeSet().size()));
		
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Graph<ArtObject,DefaultWeightedEdge> getGrafo() {
		
		return this.grafo;
	}
	
	
	
	
	
	
}
