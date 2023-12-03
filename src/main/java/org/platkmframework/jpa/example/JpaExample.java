package org.platkmframework.jpa.example;

import java.util.List;

import org.platkmframework.codegenerator.core.processor.GeneratorProcessorMapToDb;
import org.platkmframework.codegenerator.exception.CoddeGenerationException;
import org.platkmframework.common.domain.filter.criteria.SearchCriteria;
import org.platkmframework.content.ioc.ObjectContainer;
import org.platkmframework.content.ioc.exception.IoDCException;
import org.platkmframework.content.project.ProjectContent;
import org.platkmframework.jpa.persistence.CustomIoDprocess;
import org.platkmframework.jpa.persistence.PersistenceManager;
import org.platkmframework.jpa.persistence.PlatkmPersistenceFileParse;

public class JpaExample {
	
	public static void main(String[] arg) {
		
		try {

			initCongig();
			PersistenceManager.instance().begin();
			initDatabaseData();
			
			List list = PersistenceManager.instance().get("platkm_jpa_test").
						getQueryDao().
							select(new SearchCriteria().select("catalog"), null);
			 
			System.out.println("");
			System.out.println("");
			System.out.println("----------------------RESULT-------------------------");
			System.out.println("ID-Name");
			System.out.println("-----------------------------------------------------");
			for (Object row : list) {
				System.out.println( ((Object[])row)[0].toString() + "-" + ((Object[])row)[1].toString());
			}
			
			
			PersistenceManager.instance().commit();
			 
		} catch (Exception e) { 
			PersistenceManager.instance().rollback(); 			
			System.out.print(e);  
		}finally {
			PersistenceManager.instance().close();
		}
		
	}


	private static void initCongig() throws IoDCException {
		ObjectContainer.instance().process( System.getProperty("java.class.path"), new String[] {"org.platkmframework"}, 
				ProjectContent.instance().getAppProperties(),
				new CustomIoDprocess());
		
		PlatkmPersistenceFileParse.parse();
		
	}

	private static void initDatabaseData() throws CoddeGenerationException {
		GeneratorProcessorMapToDb generatorProcessorMapToDb = new GeneratorProcessorMapToDb();
		
		String script = "Create table catalog (ID int primary key, name varchar(50));"
							+ "Insert into catalog (ID, name) values (1, 'bicycle');"
							+ "Insert into catalog (ID, name) values (2, 'TV');";
		generatorProcessorMapToDb.exceute(script, PersistenceManager.instance().get("platkm_jpa_test").getDatabaseMapper().getDatabaseName(),
											PlatkmPersistenceFileParse.getPersistenceUnits().get(0));
		
		 
		
	}
}
