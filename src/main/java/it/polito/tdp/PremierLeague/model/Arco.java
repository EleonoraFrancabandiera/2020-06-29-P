package it.polito.tdp.PremierLeague.model;

public class Arco {
	
	private Match m1;
	private Match m2;
	private int peso;
	
	public Arco(Match m1, Match m2, int peso) {
		super();
		this.m1 = m1;
		this.m2 = m2;
		this.peso = peso;
	}

	@Override
	public String toString() {
		return "[" + m1.getMatchID() + "] " + m1.teamHomeNAME + " vs. " +m1.teamAwayNAME + 
				" - [" + m2.getMatchID() + "] " + m2.teamHomeNAME + " vs. " + m2.teamAwayNAME
				+ " (" + peso + ")";
	}
	
	

}
