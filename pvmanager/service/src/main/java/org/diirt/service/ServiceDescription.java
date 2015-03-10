/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import static org.diirt.service.Service.namePattern;

/**
 * A utility class to gather all the elements that define the service.
 * <p>
 * This class is not thread-safe and is meant to be used right before the
 * creation of Service objects.
 *
 * @author carcassi
 */
public class ServiceDescription {

    // Access is package private so we don't even bother creating accessors for
    // these
    String name;
    String description;
    List<ServiceMethodDescription> serviceMethodDescriptions = new ArrayList<>(); //NEW
    // Remove following
    Map<String, ServiceMethod> serviceMethods = new HashMap<>();
    ExecutorService executorService; //NEW

    /**
     * Creates a new service description with the given name and description,
     * both of which are mandatory attributes of the service.
     *
     * @param name the service name, can't be null
     * @param description the service description, can't be null
     */
    public ServiceDescription(String name, String description) {
        // TODO: check they are not null or empty strings
        this.name = name;
        this.description = description;
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }
    }

    /**
     * Adds the given method to this service description.
     *
     * @param serviceMethod a service method, can't be null
     * @return this description
     */
    public ServiceDescription addServiceMethod(ServiceMethodDescription serviceMethod) {
        // TODO check for null
        // TODO Check for reused name of service method
        serviceMethodDescriptions.add(serviceMethod);
        //serviceMethods.put(serviceMethod.getName(), serviceMethod);
        return this;
    }

    //NEW
    public ServiceDescription executorService(ExecutorService executor) {
        // TODO Check for null argument
        if (this.executorService != null) {
            throw new IllegalArgumentException("ExecutorService was already set");
        }
        this.executorService = executor;
        return this;
    }
    
    // TODO  Map<String, ServiceMethod> createServiceMethods()
    // Map should be already immutable

    //NEW
    public Service createService() {
        //All method descriptions
        for (ServiceMethodDescription serviceMethodDescription : serviceMethodDescriptions) {
            //Creates the method and updates
            ServiceMethod method = serviceMethodDescription.createServiceMethod(this);
            serviceMethods.put(method.getName(), method);
        }

        return new Service(this);
    }
}
