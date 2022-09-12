package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Match, DefaultWeightedEdge> grafo;
	private Map<Integer, Match> idMap;
	private List<Match> vertici;
	private List<Match> listaMigliore;
	
	public Model() {
		this.dao= new PremierLeagueDAO();
		this.idMap= new HashMap<>();
		
		this.dao.listAllMatches(idMap);
		
	}
	
	public void creaGrafo(int mese, int minuti) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		
		//vertici
		this.vertici=this.dao.getVertici(mese, idMap);
		this.dao.aggiungiGiocatoriMatch(minuti, this.vertici);
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		//archi
		for(Match m1 : this.vertici) {
			for(Match m2 : this.vertici) {
				if(!m1.equals(m2)) {
					int peso = this.getPeso(m1, m2);
					if(peso>0) {
						Graphs.addEdge(this.grafo, m1, m2, peso);
					}
				}
			}
		}
		
		
	}
	
	public List<Arco> getConnessioniMax(){
		int max=0;
		List<Arco> result = new ArrayList<>();
		
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			int peso = (int) this.grafo.getEdgeWeight(e);
			if(peso>max) {
				max=peso;
			}
		}
		
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			int peso = (int) this.grafo.getEdgeWeight(e);
			if(peso==max) {
				Arco arco = new Arco(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), peso);
				result.add(arco);
			}
		}
		
		return result;
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public boolean grafoCreato() {
		if(this.grafo==null)
			return false;
		return true;
	}
	
	public int getPeso(Match m1, Match m2) {
		
		Set<Integer> idGiocatori = new HashSet<>();
		
		for(int g1 : m1.giocatoriMinMinuti) {
			for(int g2 : m2.giocatoriMinMinuti) {
				if(g1==g2)
					idGiocatori.add(g1);
			}
		}
		
		return idGiocatori.size();
	}
	
	public List<Match> getVertici(){
		return this.vertici;
	}
	
	//ricorsione
	
	public List<Match> calcolaPercorso(Match m1, Match m2){
		List<Match> matchValidi = new ArrayList<>();
		ConnectivityInspector<Match, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo);
		
		matchValidi.addAll(ci.connectedSetOf(m1));
		boolean trovato= false;
		
		for(Match match: matchValidi) {
			if(match.getMatchID()==m2.getMatchID()) {
				trovato = true;
				break;
			}
		}
		
		if(!trovato) {
			return null;
		}
		
		List<Match> parziale = new ArrayList<>();
		listaMigliore= new ArrayList<>();
		parziale.add(m1);
		
		cerca(parziale, matchValidi, m1, m2);
		
		return listaMigliore;	
	}
	
	
	private void cerca(List<Match> parziale, List<Match> matchValidi, Match m1, Match m2) {
		
		DefaultWeightedEdge ultimoArco = this.grafo.getEdge(parziale.get(parziale.size()-1), m2);
		if(ultimoArco!=null) {
			parziale.add(m2);
			if(this.calcolaPesoLista(parziale)>this.calcolaPesoLista(listaMigliore)) {
				listaMigliore = new ArrayList<>(parziale);
			}
			parziale.remove(parziale.size()-1);
		}
		
		for(Match m : matchValidi) {
			if(!m.equals(m2)) {
				if(!parziale.contains(m)) {
					Match ultimoAggiunto = parziale.get(parziale.size()-1);
					DefaultWeightedEdge arco = this.grafo.getEdge(ultimoAggiunto, m);
					if(arco!=null) {//esiste effettivamente un arco tra i due vertici
						//devo controllare che le squadre non siano le stesse
						if(!((m.getTeamHomeID()==ultimoAggiunto.getTeamHomeID() && m.getTeamAwayID()==ultimoAggiunto.getTeamAwayID()) ||
								(m.getTeamHomeID()==ultimoAggiunto.getTeamAwayID() && m.getTeamAwayID()==ultimoAggiunto.getTeamHomeID()))) {
							parziale.add(m);
							cerca(parziale, matchValidi, m1, m2);
							parziale.remove(parziale.size()-1);
						}
					}
				}
			}
		}
		
	}
	
	public int calcolaPesoLista(List<Match> lista) {
		int peso=0;
		for(int i=0; i<(lista.size()-1); i++ ) {
			DefaultWeightedEdge e = this.grafo.getEdge(lista.get(i),lista.get(i+1));
			peso+=this.grafo.getEdgeWeight(e);
		}
		
		return peso;
	}
	
	
	
	
	
	
	
}
