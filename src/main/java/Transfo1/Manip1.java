package Transfo1;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.*;
import java.nio.file.*;
import java.util.Map;
import java.util.regex.*;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import projectsortie.ProjectsortiePackage;

public class Manip1 {

    public static void main(String[] args) {
    	ExecManip1("C:/Users/zakar/eclipse-workspace/Ecore/model/InitConfig/InitConfig.model");
    }
    
    public static EObject ExecManip1(String ModelInitUri) {
        // Create an instance of ClassesToTestRetrieval
        ClassesToTestRetrieval classesRetrieval = new ClassesToTestRetrieval();

        // Get the list of file names from ClassesToTestRetrieval
      
        String classestotest =   String.join(", ", classesRetrieval.retrieveFileNames());
        
        //  EPackage.Registry.INSTANCE.put("http://ezdevops2.0com", ProjectsortiePackage.eINSTANCE);



        // Register the XMI resource factory for the EMF resource set
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

        // Create an EMF resource set
        ResourceSet resourceSet = new ResourceSetImpl();

        // Load the input metamodel (InitConfig.ecore)
        Resource MMResource = resourceSet.getResource(URI.createFileURI("C:/Users/zakar/eclipse-workspace/Ecore/model/InitConfig/InitConfig.ecore"), true);
        EObject MMracine = MMResource.getContents().get(0);
        EPackage MMePackage = (EPackage) MMracine;

        // Load the output metamodel (JenkinsMetamodel.ecore)
        Resource MMSResource = resourceSet.getResource(URI.createFileURI("C:/Users/zakar/eclipse-workspace/Ecore/model/Jenkins/JenkinsMetamodel.ecore"), true);
        EObject MMSracine = MMSResource.getContents().get(0);
        EPackage MMSePackage = (EPackage) MMSracine;

        // Load the input model (InitConfig.model)
        String nsUri = MMePackage.getNsURI();
        resourceSet.getPackageRegistry().put(nsUri, MMePackage);
        Resource load_resource = resourceSet.getResource(URI.createFileURI(ModelInitUri), true);
        Resource MResource = load_resource;
        EObject MRacine = MResource.getContents().get(0);

        // Create an instance of the root class from the output metamodele
        EClass outputRootClass = (EClass) MMSePackage.getEClassifier("config");
        EObject outputModelRoot = MMSePackage.getEFactoryInstance().create(outputRootClass);
       
        //  Stages
        EClass stagesClass = (EClass) MMSePackage.getEClassifier("Stages");
        EObject stagesObject = MMSePackage.getEFactoryInstance().create(stagesClass);

        ((EList<EObject>) outputModelRoot.eGet(((EClass) MMSePackage.getEClassifier("config")).getEStructuralFeature("stages"))).add(stagesObject);
        

        EAttribute nameFeature = (EAttribute) stagesClass.getEStructuralFeature("name");
        stagesObject.eSet(nameFeature, "Vos 4 stages...");
        ((EList<EObject>) outputModelRoot.eGet(((EClass) MMSePackage.getEClassifier("config")).getEStructuralFeature("stages"))).add(stagesObject);
        

        // Transformation rules
        
        //Creating the conform cloning child of stages 'first attribute'
        EClass cloningClass = (EClass) MMSePackage.getEClassifier("Cloning");
        EObject cloningObject = MMSePackage.getEFactoryInstance().create(cloningClass);

        EAttribute credentialIDFeature = (EAttribute) cloningClass.getEStructuralFeature("credentialID");
        cloningObject.eSet(credentialIDFeature, "personal-cloning-key");
        
        //transformation from the Projet branch to the cloning branch
        
        EAttribute branchFeature = (EAttribute) cloningClass.getEStructuralFeature("branch");
        cloningObject.eSet(branchFeature, MRacine.eGet(((EClass) MMePackage.getEClassifier("Projet")).getEStructuralFeature("branch")));
        EReference stagesCloningReference = findContainmentReference(stagesClass, "cloning");
        
        // Projet.url + '.git' to Cloning.url
        
        EAttribute urlFeature = (EAttribute) cloningClass.getEStructuralFeature("url");
        cloningObject.eSet(urlFeature, MRacine.eGet(((EClass) MMePackage.getEClassifier("Projet")).getEStructuralFeature("url"))+ ".git");
        
        
        // Initialize the "cloning" feature if it's null
        EList<EObject> cloningList = (EList<EObject>) stagesObject.eGet(stagesCloningReference);
        if (cloningList == null) {
            cloningList = new BasicEList<>();
        }

        // Add cloningObject to cloningList
        cloningList.add(cloningObject);

        // Set cloningList to the "cloning" feature
        stagesObject.eSet(stagesCloningReference, cloningObject);

       
        
//Testing Stage
                       
        
        
        
        // Testing.cmdtest to Tests.shell
        
        EObject rootObject = MRacine;
        // Iterate through all elements
        TreeIterator<Object> iterator = EcoreUtil.getAllContents(Collections.singletonList(rootObject));
        while (iterator.hasNext()) {
            Object obj = iterator.next();

            if (obj instanceof EObject) {
                EObject eObject = (EObject) obj;
                for (EStructuralFeature feature : eObject.eClass().getEAllStructuralFeatures()) {
                    Object value = eObject.eGet(feature);
                    if ("cmdtest".equals(feature.getName())) {
                    
                   	 String cmdTesting = value.toString();
                   	EClass testsClassShell = (EClass) MMSePackage.getEClassifier("Tests");
                    EObject testsObjectShell = MMSePackage.getEFactoryInstance().create(testsClassShell);
                    EAttribute shellFeature = (EAttribute) testsClassShell.getEStructuralFeature("shell");
                    EAttribute classFeature = (EAttribute) testsClassShell.getEStructuralFeature("classestotest");

                    // MRacine.eGet(((EClass) MMePackage.getEClassifier("Testing")).getEStructuralFeature("Cmdtest"))
                    testsObjectShell.eSet(shellFeature, cmdTesting);
                    testsObjectShell.eSet(classFeature,classestotest );

                
                    // Initialize the "testing" feature if it's null
                    EReference stagesTestingReference = findContainmentReference(stagesClass, "tests");

                    EList<EObject> testingList = (EList<EObject>) stagesObject.eGet(stagesTestingReference);
                    if (testingList == null) {
                    	testingList = new BasicEList<>();
                    }

                    // Add cloningObject to cloningList
                    testingList.add(testsObjectShell);

                    // Set cloningList to the "cloning" feature
                    stagesObject.eSet(stagesTestingReference, testsObjectShell);
                    }
                }
            }
        }
        
        
 //Build Stage
        EClass buildClassCmd = (EClass) MMSePackage.getEClassifier("Build");
        EObject buildObjectCmd = MMSePackage.getEFactoryInstance().create(buildClassCmd);
        EAttribute cmdFeature = (EAttribute) buildClassCmd.getEStructuralFeature("cmd");
        buildObjectCmd.eSet(cmdFeature, "sh \"docker build -t ${dockerImageTag} .\"");
        EReference stagesbuildReference = findContainmentReference(stagesClass, "build");
        
        
        // Initialize the "build" feature if it's null
        EList<EObject> buildList = (EList<EObject>) stagesObject.eGet(stagesbuildReference);
        if (buildList == null) {
            buildList = new BasicEList<>();
        }

        // Add cloningObject to cloningList
        buildList.add(buildObjectCmd);

        // Set cloningList to the "cloning" feature
        stagesObject.eSet(stagesbuildReference, buildObjectCmd);
        
   
        
//Deploy stage
        
        EClass deployClassCmd = (EClass) MMSePackage.getEClassifier("Deploy");
        EObject deployObjectCmd = MMSePackage.getEFactoryInstance().create(deployClassCmd);
        EAttribute cmdDeployFeature = (EAttribute) deployClassCmd.getEStructuralFeature("cmd");
        deployObjectCmd.eSet(cmdDeployFeature, "sh \"docker push ${doockerImageTag}\"");
        
EReference stagesdeployReference = findContainmentReference(stagesClass, "deploy");
        
        
        // Initialize the "deploy" feature if it's null
        EList<EObject> deployList = (EList<EObject>) stagesObject.eGet(stagesdeployReference);
        if (deployList == null) {
        	deployList = new BasicEList<>();
        }

        // Add cloningObject to cloningList
        deployList.add(deployObjectCmd);

        // Set cloningList to the "cloning" feature
        stagesObject.eSet(stagesdeployReference, deployObjectCmd);
        
        
        
        
        
        // Save Output Model:
        Resource outputModelResource = resourceSet.createResource(URI.createFileURI("C:/Users/zakar/eclipse-workspace/Ecore/model/Output.model"));
        outputModelResource.getContents().add(outputModelRoot);
        try {
            outputModelResource.save(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  printEObject(outputModelRoot,0);
        Map<String, Object> featureMap = getEObjectMap(outputModelRoot, 0);
        Map<String, Object> stages = (Map<String, Object>) featureMap.get("stages");
        Map<String, Object> cloning = (Map<String, Object>) stages.get("tests");
        System.out.println("Classes to test: " + cloning.get("classestotest"));
        
        printFeatureMapAsYaml(featureMap);

        try {
            replaceVariablesInTemplate("JenkinsFileTemplate", featureMap, "JenkinsFile");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputModelRoot;
    }

    public static void replaceVariablesInTemplate(String templateFilePath, Map<String, Object> featureMap, String outputFilePath) throws IOException {
        // ecrire un fichier jenkinsfile conforme aux specifications 
        Path templatePath = Paths.get(templateFilePath);
        Path outputPath = Paths.get(outputFilePath);

        try (BufferedReader reader = Files.newBufferedReader(templatePath);
             BufferedWriter writer = Files.newBufferedWriter(outputPath)) {

            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = Pattern.compile("\\[(.*?)\\/\\]").matcher(line);
                while (matcher.find()) {
                    String variable = matcher.group(1);
                    String[] keys = variable.split("\\.");
                    Map<String, Object> currentMap = featureMap;
                    for (int i = 0; i < keys.length - 1; i++) {
                        currentMap = (Map<String, Object>) currentMap.get(keys[i]);
                    }
                    String value = (String) currentMap.get(keys[keys.length - 1]);
                    if (value == null) {
                        line = line.replace("[" + variable + "/]", "default");
                    } else if(variable.equals("stages.tests.classestotest")) {
                        // Split and remove spaces after splitting
                        String[] classes = value.split(",");
                        line = line.replace("[" + variable + "/]", "");
                        for (String classe : classes) {
                            writer.write("\t\t\t\tsh('./mvnw test -Dspring.profiles.active=prod -Dspring.datasource.url=$TEST_DATABASE_URL -Dspring.datasource.username=$TEST_DATABASE_USERNAME -Dspring.datasource.password=$TEST_DATABASE_PASSWORD -Dtest="+classe.trim()+"')");
                            writer.newLine();
                        }
                    }
                    else line = line.replace("[" + variable + "/]", value);
                }
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private static void setFeatureValue(EObject targetObject, EPackage targetPackage, String targetClassName, String targetFeatureName,
        EObject sourceObject, EPackage sourcePackage, String sourceClassName, String sourceFeatureName) {
        EClass targetClass = (EClass) targetPackage.getEClassifier(targetClassName);
        EStructuralFeature targetFeature = targetClass.getEStructuralFeature(targetFeatureName);

        EClass sourceClass = (EClass) sourcePackage.getEClassifier(sourceClassName);
        EStructuralFeature sourceFeature = sourceClass.getEStructuralFeature(sourceFeatureName);

        if (targetFeature.isChangeable()) {
            // Check if sourceFeature is an attribute
            if (sourceFeature instanceof EAttribute) {
                targetObject.eSet(targetFeature, sourceObject.eGet(sourceFeature));
            } else {
                // If sourceFeature is a reference, copy its value to the target feature
                EObject referencedObject = (EObject) sourceObject.eGet(sourceFeature);
                if (referencedObject != null) {
                    targetObject.eSet(targetFeature, referencedObject);
                }
            }
        } else {
            System.err.println("Cannot set the value for non-changeable feature: " + targetFeatureName);
        }
    }


    private static void setFeatureValue(EObject targetObject, EPackage targetPackage, String targetClassName, String targetFeatureName, Object value) {
        EClass targetClass = (EClass) targetPackage.getEClassifier(targetClassName);
        EAttribute targetFeature = (EAttribute) targetClass.getEStructuralFeature(targetFeatureName);

        targetObject.eSet(targetFeature, value);
    }

    public static Map<String, Object> getEObjectMap(EObject eObject, int indent) {
        // Create a HashMap to store feature names and values
        Map<String, Object> featureMap = new HashMap<>();

        for (EStructuralFeature feature : eObject.eClass().getEStructuralFeatures()) {
            Object value = eObject.eGet(feature);

            if (feature.isMany()) {
                Map<String, Object> subFeatureMap = new HashMap<>();
                for (Object element : (Iterable<?>) value) {
                    if (element instanceof EObject) {
                        subFeatureMap.putAll(getEObjectMap((EObject) element, indent + 1));
                    } else {
                        subFeatureMap.put(feature.getName(), element);
                    }
                }
                featureMap.put(feature.getName(), subFeatureMap);
            } else {
                if (value instanceof EObject) {
                    featureMap.put(feature.getName(), getEObjectMap((EObject) value, indent + 1));
                } else {
                    featureMap.put(feature.getName(), value);
                }
            }
        }

        return featureMap;
    }

    public static void printEObject(EObject eObject, int indent) {
        // Create a HashMap to store feature names and values
        // Map<String, Object> featureMap = new HashMap<>();

        for (EStructuralFeature feature : eObject.eClass().getEStructuralFeatures()) {
            Object value = eObject.eGet(feature);

            // featureMap.put(feature.getName(), value);

            // Print feature name and value
            System.out.print(getIndent(indent) + feature.getName() + ": *****");

            if (feature.isMany()) {
                System.out.println();
                for (Object element : (Iterable<?>) value) {
                    if (element instanceof EObject) {
                        printEObject((EObject) element, indent + 1);        
                    } else {
                    System.out.println(getIndent(indent + 1) + element + "----");
                    }
                }
            } else {
                if (value instanceof EObject) {
                    System.out.println();
                    printEObject((EObject) value, indent + 1);
                } else {
                    System.out.println(value+ "°°°°°");
                }
            }
        }
        // for (Object i : featureMap.values()) {
        //     if(i==null){
        //         System.out.println("null");
        //     }else{
        //         //System.out.println(i.getClass().getEStructuralFeatures());

        //     }
            
        //   }
        // Print only the feature names and their values
        //System.out.println(getIndent(indent) + "Feature Map:");
        // printFeatureMap(featureMap, indent + 1);
        // return featureMap;

    }

public static void printFeatureMap(Map<String, Object> featureMap, int indent) {
    for (Map.Entry<String, Object> entry : featureMap.entrySet()) {
        System.out.print("HACHM " + getIndent(indent) + entry.getKey() + ": ");

        if (entry.getValue() instanceof Map && !(entry.getValue() instanceof EObject)) {
            System.out.println();
            printFeatureMap((Map<String, Object>) entry.getValue(), indent + 1);
        } else if (!(entry.getValue() instanceof EObject)) {
            System.out.println(entry.getValue()+"hachm");
        }
    }
}

public static void printFeatureMapAsYaml(Map<String, Object> featureMap) {
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setPrettyFlow(true);
    options.setIndent(2);

    Yaml yaml = new Yaml(options);
    String output = yaml.dump(featureMap);
    System.out.println(output);
}


private static String getIndent(int count) {
    StringBuilder indent = new StringBuilder();
    for (int i = 0; i < count; i++) {
        indent.append("  "); // Adjust the number of spaces as needed
    }
    return indent.toString();
}
private static EReference findContainmentReference(EClass eClass, String featureName) {
    for (EReference reference : eClass.getEAllReferences()) {
        if (reference.isContainment() && reference.getName().equals(featureName)) {
            return reference;
        }
    }
    throw new IllegalArgumentException("Containment reference not found: " + featureName);
}

    
}
