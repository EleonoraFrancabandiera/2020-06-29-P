/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Arco;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Integer> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(!this.model.grafoCreato()) {
    		this.txtResult.appendText("Crea prima il grafo\n");
    		return;
    	}
    	
    	this.txtResult.appendText("Coppie con connessione massima:\n\n");
    	for(Arco a : this.model.getConnessioniMax()) {
    		this.txtResult.appendText(a.toString() + "\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	
    	int mese = 0;
    	mese = this.cmbMese.getValue();
    	if(mese==0) {
    		this.txtResult.appendText("Seleziona un mese");
    		return;
    	}
    	
    	int minuti = 0;
    	try {
    		minuti = Integer.parseInt(this.txtMinuti.getText());
    	}catch(NumberFormatException ex) {
    		txtResult.appendText("Errore: il numero di minuti deve essere un intero positivo\n");
    		return;
    	}
    	
    	this.model.creaGrafo(mese, minuti);
    	this.txtResult.appendText("Grafo creato!\n");
    	this.txtResult.appendText("# Vertici : " + this.model.nVertici() + "\n");
    	this.txtResult.appendText("# Archi : " + this.model.nArchi() + "\n");
    	
    	
    	this.cmbM1.getItems().clear();
    	this.cmbM2.getItems().clear();
    	this.cmbM1.getItems().addAll(this.model.getVertici());
    	this.cmbM2.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	if(!this.model.grafoCreato()) {
    		this.txtResult.appendText("Crea prima il grafo\n");
    		return;
    	}
    	
    	Match m1 = this.cmbM1.getValue();
    	Match m2 = this.cmbM2.getValue();
    
    	if(m1==null || m2==null) {
    		this.txtResult.appendText("Selezionare un match di partenza e uno di arrivo\n");
    		return;
    	}
    	
    	if(m1.equals(m2)) {
    		this.txtResult.appendText("Selezionare un match di partenza diverso da quello di arrivo\n");
    		return;
    	}
    	
    	
    	List<Match> percorso = this.model.calcolaPercorso(m1, m2);
    	if(percorso.size()!=0) {
    		this.txtResult.appendText("Percorso migliore:\n");
    		for(Match m : percorso) {
    			this.txtResult.appendText(m.toString() + "\n");
    		}
    		this.txtResult.appendText("Peso del percorso: " + this.model.calcolaPesoLista(percorso));
    		
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

       
    }
    
    public void setModel(Model model) {
    	this.model = model;
	   for(int i=1; i<13; i++) {
	        	this.cmbMese.getItems().add(i);
	        }
    }
    
    
}
