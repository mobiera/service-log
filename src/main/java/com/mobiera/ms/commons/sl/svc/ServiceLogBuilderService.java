package com.mobiera.ms.commons.sl.svc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mobiera.commons.util.JsonUtil;
import com.mobiera.ms.commons.sl.api.ServiceLogMsg;
import com.mobiera.ms.commons.sl.model.ServiceLog;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;


@ApplicationScoped
public class ServiceLogBuilderService {

	@Inject
	EntityManager em;
	
	private static boolean startedService = false;
	
	private static Logger logger = Logger.getLogger(ServiceLogBuilderService.class);
	
	
	void onStart(@Observes StartupEvent ev) {
		createIndexes();
		startedService = true;
    }

    void onStop(@Observes ShutdownEvent ev) {
    	
    	startedService = false;
    }
 
    void createIndexes() {
    	// create required indexes
		
		
		InputStream inputStream = getClass().getResourceAsStream("/indexes.sql");
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		
		String line;
	    try {
			while( (line = br.readLine()) != null )
			{
				if (!line.strip().isEmpty()) {
					if (!line.startsWith("--")) {
						executeIndexQuery(line);
					}
				}
				
			}
		} catch (IOException e) {
			logger.error("", e);
		}
		
		
    	
    }

	@Transactional
	public void executeIndexQuery(String line) {
		logger.info("executeIndexQuery: query: " + line);
		em.createNativeQuery(line).executeUpdate();
	}

	@Transactional
	public void serviceLogMsg(ServiceLogMsg slm) {
		
		if (slm != null) {
			if (slm.getTs() != null) {
				ServiceLog sl = new ServiceLog();
				sl.setData1(slm.getData1());
				sl.setData2(slm.getData2());
				sl.setData3(slm.getData3());
				sl.setData4(slm.getData4());
				sl.setData5(slm.getData5());
				sl.setData6(slm.getData6());
				sl.setData7(slm.getData7());
				sl.setData8(slm.getData8());
				sl.setData9(slm.getData9());
				sl.setDataA(slm.getDataA());
				sl.setEntity(slm.getEntity());
				sl.setInstance(slm.getInstance());
				sl.setModule(slm.getModule());
				sl.setTs(slm.getTs());
				sl.setUser(slm.getUser());
				sl.setPersistedTs(Instant.now());
				em.persist(sl);
			} else {
				try {
					logger.warn("serviceLogMsg: ignoring slm with null ts: " + JsonUtil.serialize(slm, false));
				} catch (JsonProcessingException e) {
					
				}
			}
		}
		
	}


	public boolean isStartedService() {
		return startedService;
	}

}
