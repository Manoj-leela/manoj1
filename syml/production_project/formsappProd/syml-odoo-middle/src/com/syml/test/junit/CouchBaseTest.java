package com.syml.test.junit;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.couchbase.client.CouchbaseClient;
import net.spy.memcached.internal.GetFuture;
import net.spy.memcached.internal.OperationFuture;
public class CouchBaseTest {

	 public static final int EXP_TIME = 10;
	  public static final String KEY = "spoon";
	  public static final String VALUE = "Hello World!";

	  public static void main(String args[]) {
	    // Set the URIs and get a client
	    List<URI> uris = new LinkedList<URI>();

	    Boolean do_delete = false;

	    // Connect to localhost or to the appropriate URI
	    uris.add(URI.create("http://107.23.89.76:8091/pools"));

	    CouchbaseClient client = null;
	    try {
	      client = new CouchbaseClient(uris, "syml", "symL@0115");
	    } catch (Exception e) {
	      System.err.println("Error connecting to Couchbase: "
	        + e.getMessage());
	      System.exit(0);
	    }
	    // Do a synchrononous get
	    Object getObject = client.get("f814a5a9-0f44-4939-aa3d-cede5d81f1ce");
	    System.out.println("Key is :"+getObject.toString());
	    // Do an asynchronous set
	    OperationFuture<Boolean> setOp = client.set(KEY, EXP_TIME, VALUE);
	    // Do an asynchronous get
	    GetFuture getOp = client.asyncGet(KEY);
	    // Do an asynchronous delete
	    OperationFuture<Boolean> delOp = null;
	    if (do_delete) {
	      delOp = client.delete(KEY);
	    }
	    // Shutdown the client
	    client.shutdown(1000, TimeUnit.SECONDS);
	    // Now we want to see what happened with our data
	    // Check to see if our set succeeded
	    try {
	      if (setOp.get().booleanValue()) {
	        System.out.println("Set Succeeded");
	      } else {
	        System.err.println("Set failed: "
	            + setOp.getStatus().getMessage());
	      }
	    } catch (Exception e) {
	      System.err.println("Exception while doing set: "
	          + e.getMessage());
	    }
	    // Print the value from synchronous get
	    if (getObject != null) {
	      System.out.println("Synchronous Get Suceeded: "
	          + (String) getObject);
	    } else {
	      System.err.println("Synchronous Get failed");
	    }
	    // Check to see if ayncGet succeeded
	    try {
	      if ((getObject = getOp.get()) != null) {
	        System.out.println("Asynchronous Get Succeeded: "
	            + getObject);
	      } else {
	        System.err.println("Asynchronous Get failed: "
	            + getOp.getStatus().getMessage());
	      }
	    } catch (Exception e) {
	      System.err.println("Exception while doing Aynchronous Get: "
	          + e.getMessage());
	    }
	    // Check to see if our delete succeeded
	    if (do_delete) {
	      try {
	        if (delOp.get().booleanValue()) {
	          System.out.println("Delete Succeeded");
	        } else {
	          System.err.println("Delete failed: " +
	              delOp.getStatus().getMessage());
	        }
	      } catch (Exception e) {
	        System.err.println("Exception while doing delete: "
	            + e.getMessage());
	      }
	    }
	  }
	}