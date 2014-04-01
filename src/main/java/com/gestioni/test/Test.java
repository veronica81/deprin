package com.gestioni.test;

import java.util.List;

import com.complexible.stardog.StardogException;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.IO;
import com.complexible.stardog.api.reasoning.ReasoningConnection;
import com.complexible.stardog.jena.SDJenaFactory;
import com.complexible.stardog.reasoning.api.ReasoningType;
import com.gestioni.ms.MotoreSemantico;
import com.gestioni.ms.MotoreSemanticoImpl;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import org.openrdf.rio.RDFFormat;

/**
 * @author Davide Palmisano (davide.palmisano@peerindex.com)
 */
public class Test {

    public static void main(String args[]) throws StardogException {

        ReasoningConnection reasoningConnection = ConnectionConfiguration
                .to("deprin")
                .server("http://192.168.1.252:5820")
                .credentials("admin", "admin")
                .reasoning(ReasoningType.SL)
                .connect()
                .as(ReasoningConnection.class);
        
        MotoreSemantico motoreSemantico = new MotoreSemanticoImpl(reasoningConnection);
        List<String> list = motoreSemantico.getColture();
        for(String s : list){
        	System.out.println(s);
        }
       list = motoreSemantico.getTipiTerreno();
        for(String s : list){
        	System.out.println(s);
        }
    }

}
