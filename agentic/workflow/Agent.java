package agentic.workflow;
import java.io.*;
import java.util.*;
import agentic.workflow.llm.SchemaType;
import agentic.workflow.llm.StructuredOutput;

public class Agent{

    private String name;

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    private final List<WorkflowStep> steps;

    public List<WorkflowStep> getSteps(){
        return new ArrayList<WorkflowStep>(this.steps);
    }

    public Agent(String name){
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("a név nem lehet `null`, üres vagy csak szóközökből álló.");
        }
        this.name = name;
        this.steps = new ArrayList<>();
    }


    public void addStep(WorkflowStep step){
        if(step == null){
            throw new IllegalArgumentException("nem lehet null");
        }
        for(WorkflowStep existingStep : this.steps){
            if(existingStep.getName().equals(step.getName())){
                throw new IllegalArgumentException("nem lehet ugyanaz a step neve");
            }
        }
        this.steps.add(step);
    }


    public int getStepCount(){
        return steps.size();
    }

    public WorkflowStep findStepByName(String stepName){
        if(stepName == null || stepName.isBlank()){
            throw new IllegalArgumentException("a lépés neve nem lehet `null`, üres vagy csak szóközökből álló.");
        }
        String cleanedName = stepName.trim();

        for(WorkflowStep wantedStep : this.steps){
            if(wantedStep.getName().equals(cleanedName)){
                return wantedStep;
            }
        }
        return null;
    }

    public void run(){
        for(WorkflowStep step : this.steps){
            String name = step.getName();
            String response = step.simulateResponse();
            System.out.println(name + " -> " + response);
        }
    }

    public static Agent loadAgent(String filename) throws IOException, WorkflowFormatException {
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("a fájlnév nem lehet `null`, üres vagy csak szóközökből álló.");
        }

        try (BufferedReader reader = new java.io.BufferedReader(new FileReader(filename))) {
            Agent agent = null;
            String line;

            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();

                if (trimmedLine.isEmpty()) {
                    continue;
                }

                if (agent == null) {
                    if (trimmedLine.startsWith("AGENT: ")) {
                        String agentName = trimmedLine.substring(7).trim();
                        agent = new Agent(agentName);
                        continue;
                    } else {
                        throw new WorkflowFormatException("A fájlnak 'AGENT: ...' sorral kell kezdődnie.");
                    }
                }

                if (trimmedLine.equals("STEP")) {
                    WorkflowStep step = parseStep(reader);
                    agent.addStep(step);
                } else {
                    throw new WorkflowFormatException("Váratlan sor a fájlban: " + trimmedLine);
                }
            }

            if (agent == null) {
                throw new WorkflowFormatException("Hiányzó AGENT fejléc a fájlban.");
            }

            return agent;
        }
    }


    private static WorkflowStep parseStep(BufferedReader reader) throws IOException, WorkflowFormatException {
        String name = null;
        String prompt = null;
        String systemPrompt = null;
        String outputTypeStr = null;

        String line;
        boolean endStepFound = false;

        while ((line = reader.readLine()) != null) {
            String trimmedLine = line.trim();

            if (trimmedLine.isEmpty()) {
                continue;
            }

            if (trimmedLine.equals("ENDSTEP")) {
                endStepFound = true;
                break;
            }

            int separatorIndex = trimmedLine.indexOf(':');
            if (separatorIndex == -1) {
                separatorIndex = trimmedLine.indexOf('=');
                if (separatorIndex == -1) {
                    throw new WorkflowFormatException("Hibás tulajdonságsor (nincs elválasztó): " + trimmedLine);
                }
            }

            String key = trimmedLine.substring(0, separatorIndex).trim();
            String value = trimmedLine.substring(separatorIndex + 1).trim();

            switch (key) {
                case "name":
                    name = value;
                    break;
                case "prompt":
                    prompt = value;
                    break;
                case "systemPrompt":
                    systemPrompt = value;
                    break;
                case "output":
                    outputTypeStr = value;
                    break;
                default:
                    throw new WorkflowFormatException("Ismeretlen tulajdonság a lépésben: " + key);
            }
        }

        if (!endStepFound) {
            throw new WorkflowFormatException("A fájl váratlanul véget ért, hiányzik az ENDSTEP.");
        }

        if (name == null || prompt == null || systemPrompt == null || outputTypeStr == null) {
            throw new WorkflowFormatException("Hiányzó kötelező tulajdonság a lépésben! (name, prompt, systemPrompt, output kötelező)");
        }

        SchemaType schemaType;
        try {
            schemaType = SchemaType.valueOf(outputTypeStr);
        } catch (IllegalArgumentException e) {
            throw new WorkflowFormatException("Érvénytelen output típus (nem található a SchemaType enumban): " + outputTypeStr);
        }

        StructuredOutput structuredOutput = new StructuredOutput(new SchemaType[]{schemaType});
        
        return new WorkflowStep(name, prompt, systemPrompt, structuredOutput);
    }
}
