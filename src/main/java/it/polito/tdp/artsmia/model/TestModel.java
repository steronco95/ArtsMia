package it.polito.tdp.artsmia.model;

import org.jgrapht.graph.DefaultWeightedEdge;

public class TestModel {

	public static void main(String[] args) {
		Model model = new Model();
		
		model.creaGrafo();
		
		for(DefaultWeightedEdge s : model.getGrafo().edgeSet()) {
			
			System.out.println(s.toString());
			
		}

	}

}
