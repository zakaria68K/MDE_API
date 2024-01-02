package com.MDE_API;

import java.util.Map;

import org.eclipse.acceleo.module.sample.Main;
import org.eclipse.emf.ecore.EObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import Transfo1.Manip1;

@SpringBootApplication
public class MdeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdeApiApplication.class, args);
		Manip1 manip1 = new Manip1();
	    manip1.ExecManip1("C:/Users/zakar/eclipse-workspace/Ecore/model/InitConfig/InitConfig.model");
		
	}

}
