package com.gestioni.test;

import com.complexible.stardog.StardogException;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.IO;
import com.complexible.stardog.api.reasoning.ReasoningConnection;
import com.complexible.stardog.jena.SDJenaFactory;
import com.complexible.stardog.reasoning.api.ReasoningType;
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

        Model aModel = SDJenaFactory.createModel(reasoningConnection);
        String aQueryString = "PREFIX www:<http://www.deprin.owl#> " +
        		"SELECT * WHERE {?s www:ru ?o}";

        // Create a query...
        Query aQuery = QueryFactory.create(aQueryString);

        // ... and run it
        QueryExecution aExec = QueryExecutionFactory.create(aQuery, aModel);

        // now print the results
        ResultSetFormatter.out(aExec.execSelect(), aModel);
    }

}
