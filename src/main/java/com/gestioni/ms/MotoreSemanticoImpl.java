package com.gestioni.ms;

import java.util.ArrayList;
import java.util.List;

import com.complexible.stardog.StardogException;
import com.complexible.stardog.api.reasoning.ReasoningConnection;
import com.complexible.stardog.jena.SDJenaFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class MotoreSemanticoImpl implements MotoreSemantico {
	private ReasoningConnection conn;
	
	public MotoreSemanticoImpl(ReasoningConnection conn){
		this.conn=conn;
	}

	@Override
	public List<String> getTipiTerreno() {
		Model aModel = null;
		 try {
			aModel = SDJenaFactory.createModel(conn);
			
		}  catch (StardogException e) {
			throw new RuntimeException(e);
		}
		 
		 String aQueryString = "PREFIX www:<http://www.deprin.owl#> " +
	        		"SELECT ?tipo_terreno WHERE {?tipo_terreno a www:tipo_terreno.}";
	        
	     // Create a query...
	        Query aQuery = QueryFactory.create(aQueryString);

	        // ... and run it
	        QueryExecution aExec = QueryExecutionFactory.create(aQuery, aModel);
	        ResultSet result = aExec.execSelect();
	        List<String> list = new ArrayList<String>();
	        while(result.hasNext()){
	        	QuerySolution next = result.next();
	        	Resource resource = next.getResource("tipo_terreno");
	        	
	        	list.add(resource.getLocalName());
	        }
	       try {
	    	   return list;
		} finally {
//			aModel.close();
			aExec.close();
		}
	}

	@Override
	public List<String> getColture() {
		Model aModel = null;
		 try {
			aModel = SDJenaFactory.createModel(conn);
			
		}  catch (StardogException e) {
			throw new RuntimeException(e);
		}
		 
		 String aQueryString = "PREFIX www:<http://www.deprin.owl#> " +
	        		"SELECT ?coltura WHERE {?coltura a www:coltura.}";
	        
	     // Create a query...
	        Query aQuery = QueryFactory.create(aQueryString);

	        // ... and run it
	        QueryExecution aExec = QueryExecutionFactory.create(aQuery, aModel);
	        ResultSet result = aExec.execSelect();
	        List<String> list = new ArrayList<String>();
	        while(result.hasNext()){
	        	QuerySolution next = result.next();
	        	Resource resource = next.getResource("coltura");
	        	
	        	list.add(resource.getLocalName());
	        }
	       try {
	    	   return list;
		} finally {
//			aModel.close();
			aExec.close();
		}
		
	}

	@Override
	public double calcolaSoglia(String tipoerreno, String coltura) {
		Model aModel = null;
		 try {
			aModel = SDJenaFactory.createModel(conn);
			
		}  catch (StardogException e) {
			throw new RuntimeException(e);
		}
		 
		 String aQueryString = "PREFIX www:<http://www.deprin.owl#> " +
	        		"SELECT ?coltivazione ?th WHERE {" +
	                    "?coltivazione a www:coltivazione." +
	                    "?coltivazione www:tipo %s." +
	                    "?coltivazione www:ha_coltura %s." +
	                    "?coltivazione www:th ?th" +
	                "}";

	        // Create a query...
	        String tipoTerreno = "www:argilloso";
	        String tipoColtivazione = "www:patata";
	        Query aQuery = QueryFactory.create(String.format(aQueryString, tipoTerreno, tipoColtivazione));
	        
	        // ... and run it
	        QueryExecution aExec = QueryExecutionFactory.create(aQuery, aModel);
	        ResultSet result = aExec.execSelect();
	        double th = 0;
	        if(result.hasNext()){
	        	QuerySolution next = result.next();
	        	Literal l = next.getLiteral("th");
	        	th = l.getDouble();
	        }
	        
	       
	       try {
	    	   return th;
		} finally {
//			aModel.close();
			aExec.close();
		}
	}

}
