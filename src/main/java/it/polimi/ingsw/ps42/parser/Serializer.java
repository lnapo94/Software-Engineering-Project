package it.polimi.ingsw.ps42.parser;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class Serializer implements JsonSerializer<Object>, JsonDeserializer<Object> {
	/*Class for Serialize/Deserialize operations that resolve problems with 
	 * Abstract classes putting the exact type of the class in the serialization process
	 * and extracting that type in the deserialization phase
	 */
	
	private static final String CLASS_KEY="CLASS_META_KEY";	

	@Override
	public Object deserialize(JsonElement el, Type type, JsonDeserializationContext context) throws JsonParseException {
		
		JsonObject jsonObj=el.getAsJsonObject();
		String tipoClasse= jsonObj.get(CLASS_KEY).getAsString();
		try{
			Class<?> classe=Class.forName(tipoClasse);
			return context.deserialize(el, classe);
			
		}catch(ClassNotFoundException e ){
			throw new JsonParseException(e);
		}	
	}
	
	@Override
	public JsonElement serialize(Object obj, Type type, JsonSerializationContext context) {
		
		JsonElement jsonObj= context.serialize(obj, obj.getClass());
		jsonObj.getAsJsonObject().addProperty(CLASS_KEY, obj.getClass().getCanonicalName());
		return jsonObj;
	}



}
