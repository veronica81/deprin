package com.gestioni.ms;

import java.util.ArrayList;
import java.util.List;

import org.openrdf.model.Statement;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

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
	public double calcolaSoglia(String tipoTerreno, String tipoColtivazione) {
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

	@Override
	public double calcolaVolumeH2O(String tipoTerreno, String tipoColtivazione, double u) {
		Model aModel = null;
		 try {
			aModel = SDJenaFactory.createModel(conn);
			
		}  catch (StardogException e) {
			throw new RuntimeException(e);
		}
		 
		 Statement statement = new StatementImpl(
	                new URIImpl("http://www.deprin.owl#igrometro"),
	                new URIImpl("http://www.deprin.owl#lettura"),
	                new LiteralImpl(String.valueOf(u), new URIImpl("http://www.w3.org/2001/XMLSchema#double"))
	        );
		 try {
			 conn.begin();
			conn.add().statement(statement);
			conn.commit();
		} catch (StardogException e1) {
			throw new RuntimeException(e1);
		}

	        String aQueryString = "PREFIX www:<http://www.deprin.owl#> " +
	        		"SELECT ?coltivazione ?th ?volume (?u <= ?th AS ?richiedeIrrigazione) " +
	        		"WHERE {" +
	                    "?coltivazione a www:coltivazione." +
	                    "?coltivazione www:tipo %s." +
	                    "?coltivazione www:ha_coltura %s." +
	                    "?coltivazione www:th ?th." +
	                    "?coltivazione www:volume ?volume." +
	                    "?coltivazione www:monitorata_da www:igrometro." +
	                    "<http://www.deprin.owl#igrometro> <http://www.deprin.owl#lettura> ?u." +
	                "}";

	      
	        Query aQuery = QueryFactory.create(String.format(aQueryString, tipoTerreno, tipoColtivazione));

	        // ... and run it
	        QueryExecution aExec = QueryExecutionFactory.create(aQuery, aModel);





	        
	        // ... and run it
	        ResultSet result = aExec.execSelect();
	        double volume = 0;
	        if(result.hasNext()){
	        	QuerySolution next = result.next();
	        	Literal l = next.getLiteral("volume");
	        	volume = l.getDouble();
	        }
	        
	        try {
	        	conn.begin();
	        	conn.remove().statement(statement);
	        	conn.commit();
	        } catch (StardogException e) {
	        	throw new RuntimeException(e);
	        }
	       
	       try {
	    	   return volume;
		} finally {
//			aModel.close();
			aExec.close();
		}
	}

}
