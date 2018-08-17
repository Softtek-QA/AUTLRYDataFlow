/**
 * 
 */
package br.lry.dataflow;

import br.lry.functions.AUTProjectsFunctions;

/**
 * 
 * Gerenciamento do fluxo de dados na automação
 * 
 * 
 * @author Softtek - QA
 *
 */
public class AUTDataFlow {

	
	
	
	/**
	 * 
	 * Recupera um conjunto de parametros padrão para todos os sistemas
	 * 
	 * @return java.util.HashMap(String,String) - Matriz de dados do projeto
	 * 
	 * 
	 */
	public java.util.HashMap<String, String> autGetParameter(){
		
		java.util.HashMap<String,String> parametersOut = new java.util.HashMap<String,String>();
		parametersOut.put("AUT_USER", "51028487");
		parametersOut.put("AUT_PASSWORD","1234");	
		
		
		return parametersOut;
	}
	
	
	
	/**
	 * 
	 * Construtor padrão da classe
	 * 
	 */
	public AUTDataFlow()
	{
		
	}	
}
