package scooter;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
public class whyme {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		   try {

				/**** Connect to MongoDB ****/
				// Since 2.10.0, uses MongoClient
				MongoClient mongo = new MongoClient("cluster0-shard-00-00-gcpxp.mongodb.net", 27017);

				/**** Get database ****/
				// if database doesn't exists, MongoDB will create it for you
//				DB db = mongo.getDB("testdb");
//				MongoCredential credential = MongoCredential.createCredential("dt14830", "STOCK", "Joyness@2".toCharArray());
//				MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017), Arrays.asList(credential));
				MongoDatabase db = mongo.getDatabase( "STOCK" );
				
	//			DB db = mongo.getDB("testdb");
				List<String> dbs = mongo.getDatabaseNames();
				for(String db1 : dbs){
					System.out.println(db1);
				}
				System.out.println(db.listCollectionNames());	

				/**** Done ****/
				System.out.println("Done");

			    } catch (MongoException e) {
				e.printStackTrace();
			    }
		   
}
}
