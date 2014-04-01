package com.gestioni.test;

import com.complexible.stardog.StardogException;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.jena.SDJenaFactory;
import com.complexible.stardog.protocols.snarl.SNARLProtocolConstants;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author Davide Palmisano (davide.palmisano@peerindex.com)
 */
public class Test {

    public static void main(String args[]) throws StardogException {

        Connection aConn = ConnectionConfiguration
                .to("deprin")
                .server("http://192.168.1.252:5820")
                .credentials("admin", "admin")
                .connect();
        Model aModel = SDJenaFactory.createModel(aConn);
        String aQueryString = "SELECT * WHERE {?s ?p ?o}";

        // Create a query...
        Query aQuery = QueryFactory.create(aQueryString);

        // ... and run it
        QueryExecution aExec = QueryExecutionFactory.create(aQuery, aModel);

        // now print the results
        ResultSetFormatter.out(aExec.execSelect(), aModel);
    }

}
