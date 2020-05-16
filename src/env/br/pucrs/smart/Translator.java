package br.pucrs.smart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import cartago.array_to_list;
import eis.iilang.Function;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;
import eis.iilang.Percept;
import jason.JasonException;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.asSyntax.parser.ParseException;

public class Translator {
	
	private static Literal JsonPrimitiveToLiteral(String functor, JsonPrimitive j) throws ParseException {	
//		System.out.println("Parsing primitive "+j.toString()+" to Term");
		return ASSyntax.createLiteral(functor,JsonPrimitiveToTerm(j));		
	}
	private static Term JsonPrimitiveToTerm(JsonPrimitive j) throws ParseException {	
//		System.out.println("Parsing primitive "+j.toString()+" to Term");
		if (j.isNumber()) {
			String num = j.getAsString();
		    boolean isFloat = num.matches("[-+]?[0-9]*\\.[0-9]+");
		    if (isFloat)
		    	return ASSyntax.createNumber(j.getAsDouble());
		    else
		    	return ASSyntax.createNumber(j.getAsInt());
		}else if (j.isString()){
			return ASSyntax.parseTerm(j.getAsString());
		} 
		return null;
	}
	
	public static List<Literal> entryToLiteral(Set<Entry<String,JsonElement>> entries) throws ParseException {
//		System.out.println("Parsing object "+entries.toString()+" to Literals");
		ArrayList<Literal> list = new ArrayList<>();
		
		for (Iterator<Entry<String, JsonElement>> iterator = entries.iterator(); iterator.hasNext();) {
			Entry<String, JsonElement> e = (Entry<String, JsonElement>) iterator.next();
			Term t = null;
				
			if (e.getValue().isJsonPrimitive()) {
				JsonPrimitive jp = e.getValue().getAsJsonPrimitive();
				if (jp.isBoolean() & jp.getAsBoolean())
					list.add(ASSyntax.createAtom(e.getKey()));
				else					
					list.add(JsonPrimitiveToLiteral(e.getKey(), jp));
			} 
			else if (e.getValue().isJsonArray()) {
				JsonArray ja = e.getValue().getAsJsonArray();
				
				if (ja.iterator().hasNext() && ja.get(0).isJsonArray()) {
					list.addAll(JsonArrayToLiterals(e.getKey(), ja));
				}else if (ja.iterator().hasNext() && ja.get(0).isJsonObject()) {
					list.addAll(JsonArrayToLiterals(e.getKey(), ja));
				} else {
					list.add(JsonArrayToLiteralTerm(e.getKey(), ja)); 
				}		
			} else if (e.getValue().isJsonObject()) {
				list.add(JsonObjectToStructure(e.getKey(), e.getValue().getAsJsonObject()));
			} else if (e.getValue().isJsonNull()) {
				t = ASSyntax.createVar();
			}
			
			if (t != null)
				list.add(ASSyntax.createLiteral(e.getKey(),t));
		}	
		
		return list;
	}
	public static Structure JsonObjectToStructure(String functor, JsonObject j) throws ParseException {	
//		System.out.println("Parsing object "+j.toString()+" to Structure");
		Structure l = ASSyntax.parseStructure(functor); 
		
		Set<Entry<String,JsonElement>> properties = j.entrySet();
		for (Iterator<Entry<String, JsonElement>> iterator = properties.iterator(); iterator.hasNext();) {
			Entry<String, JsonElement> e = (Entry<String, JsonElement>) iterator.next();
			l.addTerm(decideTerm(e.getKey(), e.getValue()));
		}
		return l;
	}
	
	public static Literal JsonArrayToLiteralTerm(String functor, JsonArray j) throws ParseException {
		return ASSyntax.createLiteral(functor, JsonArrayToTerm(j));
	}
	public static Literal JsonArrayToLiteralTerms(String functor, JsonArray j) throws ParseException {
		ListTerm list = (ListTerm) JsonArrayToTerm(j);
		Term terms[] = new Term[list.size()];
		return ASSyntax.createLiteral(functor, list.toArray(terms));
	}
	private static Term JsonArrayToTerm(JsonArray j) throws ParseException {
//		System.out.println("Parsing array "+j.toString()+" to Term");
		ListTerm list = new ListTermImpl();
		
		for (Iterator<JsonElement> iterator = j.iterator(); iterator.hasNext();) {
			JsonElement e = (JsonElement) iterator.next();
			Term t = null;
			if (e.isJsonPrimitive())
				t = JsonPrimitiveToTerm(e.getAsJsonPrimitive());
			else if (e.isJsonObject())
				list.addAll((List<Literal>) entryToLiteral(e.getAsJsonObject().entrySet()));
				
			if (t != null)
				list.add(t);
		}
		
		return list;
	}
	public static List<Literal> JsonArrayToLiterals(String functor, JsonArray j) throws ParseException {
//		System.out.println("Parsing array "+j.toString()+" to Literals");
		List<Literal> list = new ArrayList<>();
		
		for (Iterator<JsonElement> iterator = j.iterator(); iterator.hasNext();) {
			JsonElement e = (JsonElement) iterator.next();
			
			if (e.isJsonObject()) {
//				list.addAll(entryToLiteral(e.getAsJsonObject().entrySet()));

				list.add(JsonObjectToStructure(functor, e.getAsJsonObject()));
			} else {
				list.add(JsonArrayToLiteralTerms(functor, e.getAsJsonArray()));
			}
		}
		return list;
	}
	
	private static Term decideTerm(String functor, JsonElement j) throws ParseException {
//		System.out.println("deciding what "+j.toString()+" is");
		if (j.isJsonPrimitive()) {
			return JsonPrimitiveToTerm(j.getAsJsonPrimitive());
		} else if (j.isJsonArray()) {
			return JsonArrayToTerm(j.getAsJsonArray());
		} else if (j.isJsonObject()) {
			return JsonObjectToStructure(functor, j.getAsJsonObject());
		} else if (j.isJsonNull()) {
			return ASSyntax.createVar();
		}
		return null;
	}

	public static Literal perceptToLiteral(Percept per) throws JasonException {
		Literal l = ASSyntax.createLiteral(per.getName());
		for (Parameter par : per.getParameters())
			l.addTerm(parameterToTerm(per, par));
		return l;
	}
	public static Term parameterToTerm(Percept per, Parameter par) throws JasonException {
		if (par instanceof Numeral) {
			return ASSyntax.createNumber(((Numeral) par).getValue().doubleValue());
		} else if (par instanceof Identifier) {
			try {
				Identifier i = (Identifier) par;
				String a = i.getValue();
				if (!Character.isUpperCase(a.charAt(0)))
					return ASSyntax.parseTerm(a);
			} catch (Exception e) {
				System.out.println("Error while parsing to atom, going to be a string: "+e.getMessage());
			}
			return ASSyntax.createString(((Identifier) par).getValue());
		} else if (par instanceof ParameterList) {
			ListTerm list = new ListTermImpl();
			ListTerm tail = list;
			for (Parameter p : (ParameterList) par)
				tail = tail.append(parameterToTerm(per, p));
			return list;
		} else if (par instanceof Function) {
			return filter(per, par);
		}
		
		throw new JasonException("The type of parameter " + par + " is unknown!");
	}
	public static Structure filter(Percept per, Parameter par) throws JasonException{
		Function f = (Function) par;
		String name = f.getName();
		Structure l = ASSyntax.createStructure(name);
		if(name.equals("availableItem")){
			l = ASSyntax.createStructure("item");
		}
		for (Parameter p : f.getParameters())
			l.addTerm(parameterToTerm(per, p));
		if(per.getName().equals("shop") && name.equals("item")){
			l.addTerm(ASSyntax.createNumber(0));
			l.addTerm(ASSyntax.createNumber(0));
			l.addTerm(ASSyntax.createNumber(0));
		}
		return l;		
	}
	
	public static Percept JsonPrimitiveToPercept(String functor, JsonPrimitive j) {
		return new Percept(functor, JsonPrimitiveToParameter(j));
	}
	public static Parameter JsonPrimitiveToParameter(JsonPrimitive j) {
		if (j.isNumber()) {
			String num = j.getAsString();
		    boolean isFloat = num.matches("[-+]?[0-9]*\\.[0-9]+");
		    if (isFloat)
		    	return new Numeral(j.getAsDouble());
		    else
		    	return new Numeral(j.getAsInt());
		}else if (j.isString()){
			return new Identifier(j.getAsString());
		} 
		return null;
	}
	public static ParameterList JsonObjectToFunction(JsonObject j) {
		List<Parameter> list = new ArrayList<Parameter>();
		
		j.entrySet().forEach(e -> list.add(new Function(e.getKey(), decideParameter(e.getKey(), e.getValue()))));

		return new ParameterList(list);
	}
	public static Function JsonObjectToFunction(String functor, JsonObject j) {
		List<Parameter> list = new ArrayList<Parameter>();
		
		j.entrySet().forEach(e -> list.add(decideParameter(e.getKey(), e.getValue())));

		return new Function(functor,list);
	}
	public static Percept JsonObjectToPercept(String functor, JsonObject j) {
		Percept p = new Percept(functor);
		
		j.entrySet().forEach(e -> {
			p.addParameter(decideParameter(e.getKey(), e.getValue()));
		});
		
		return p;
	}
	
	private static ParameterList JsonArrayToParameter(JsonArray j) {
		List<Parameter> list = new ArrayList<Parameter>();
		
		j.forEach(p -> {
			if (p.isJsonPrimitive())
				list.add(JsonPrimitiveToParameter(p.getAsJsonPrimitive()));
			else if (p.isJsonObject()) {
				JsonObjectToFunction(p.getAsJsonObject()).forEach(m -> list.add(m));
			}
		});
		
		return new ParameterList(list);
	}
	public static List<Percept> JsonArrayToPercepts(String functor, JsonArray j) {
		List<Percept> list = new ArrayList<>();
		
		j.forEach(m -> {
			if (m.isJsonObject()) {
				list.add(JsonObjectToPercept(functor, m.getAsJsonObject()));
			} else {
//				list.add(JsonArrayToLiteralTerms(functor, e.getAsJsonArray()));
			}
		});
		
		return list;
	}
	
	public static List<Percept> entryToPercept(Set<Entry<String,JsonElement>> entries) throws ParseException {
		ArrayList<Percept> list = new ArrayList<>();
		
		entries.forEach(e ->{
			if (e.getValue().isJsonPrimitive()) {
				JsonPrimitive jp = e.getValue().getAsJsonPrimitive();
				if (jp.isBoolean() & jp.getAsBoolean())
					list.add(new Percept(e.getKey()));
				else					
					list.add(JsonPrimitiveToPercept(e.getKey(), jp));
			} 
			else if (e.getValue().isJsonArray()) {
				JsonArray ja = e.getValue().getAsJsonArray();
				
				if (ja.iterator().hasNext() && ja.get(0).isJsonArray()) {
					list.addAll(JsonArrayToPercepts(e.getKey(), ja));
				}else if (ja.iterator().hasNext() && ja.get(0).isJsonObject()) {
					list.addAll(JsonArrayToPercepts(e.getKey(), ja));
				} else {
//					list.add(JsonPrimitiveToParameter(ja.get)); 
				}		
			} else if (e.getValue().isJsonObject()) {
				list.add(JsonObjectToPercept(e.getKey(), e.getValue().getAsJsonObject()));
			} else if (e.getValue().isJsonNull()) {
//				t = ASSyntax.createVar();
			}
		});	
		
		return list;
	}
	
	private static Parameter decideParameter(String functor, JsonElement j) {
		if (j.isJsonPrimitive()) {
			return JsonPrimitiveToParameter(j.getAsJsonPrimitive()); 
		}
		else if (j.isJsonArray()) {
			return JsonArrayToParameter(j.getAsJsonArray());
		} else if (j.isJsonObject()) {
			return JsonObjectToFunction(functor, j.getAsJsonObject());
		} else if (j.isJsonNull()) {
			return null;
		}
		return null;
	}
}