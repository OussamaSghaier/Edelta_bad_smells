package com.example;

import edelta.lib.AbstractEdelta;
import edelta.resolver.lib.Resolver;

public class Main {

	public static void main(String[] args) throws Exception {
		experiment("CRM", "model/My.ecore");
		//experiment("Families", "model/Families/BS1/Families.ecore");
		
		//experiment("Persons", "model/Grafcet/BS1/Persons.ecore");
		//experiment("Grafcet", "model/Grafcet/BS5/Grafcet.ecore");
		//experiment("PNML", "model/PNML/BS5/PNML.ecore");
		//experiment("PathExp", "model/PathExp/BS1/PathExp.ecore");
		//experiment("PetriNet", "model/PetriNet/BS5/PetriNet.ecore");
		//experiment("typeB", "model/TypeB/BS5/TypeB.ecore");
		//experiment("SimpleClass", "model/SimpleClass/BS5/SimpleClass.ecore");
		//experiment("SimpleRDBMS", "model/SimpleRDBMS/BS4/SimpleRDBMS.ecore");
		
	}
	
	public static void experiment(String packageName,String ecorefile) throws Exception {
		// Create an instance of the generated Java class
		AbstractEdelta edelta = new Resolver() {
			@Override
			protected void doExecute() throws Exception {
				checkRefactoringsCatalog(getEPackage(packageName));
			};
		};
		// Make sure you load all the used Ecores
		edelta.loadEcoreFile(ecorefile);
		// Execute the actual transformations defined in the DSL
		edelta.execute();
		// Save the modified Ecore model into a new path
		edelta.saveModifiedEcores("modified");
		
	}

	
}
