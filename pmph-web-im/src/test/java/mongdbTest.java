import com.ai.paas.utils.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class mongdbTest {
	
	
	
	public static void main(String[] args) {
		DBCollection collection  =MongoUtil.getDBCollection("T_IM_MESSAGE_HISTORY");
		BasicDBObject doc = new BasicDBObject();  
	    collection.remove(doc);
	    
	    DBCursor cursor = collection.find();
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
		} finally {
			cursor.close();
		}
	}
	
	

}
