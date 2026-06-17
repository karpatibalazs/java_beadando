package agentic.workflow;

import agentic.workflow.llm.SchemaType;
import agentic.workflow.llm.StructuredOutput;

public class WorkflowStep{
    private String name;
    private String prompt;
    private String systemPrompt;
    private StructuredOutput structuredOutput;

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPrompt(){
        return this.prompt;
    }

    public void setPrompt(String prompt){
        this.prompt = prompt;
    }

    public String getSystemPrompt(){
        return this.systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt){
        this.systemPrompt = systemPrompt;
    }

    public StructuredOutput getStructuredOutput(){
        return this.structuredOutput;
    }

    public void setStructuredOutput(StructuredOutput structuredOutput){
        this.structuredOutput = structuredOutput;
    }

    public WorkflowStep(String name, String prompt, String systemPrompt, StructuredOutput structuredOutput){
        if(name.length() == 0 || prompt.length() == 0 || systemPrompt.length() == 0 || structuredOutput == null){
            throw new IllegalArgumentException("a `name`, `prompt` és `systemPrompt` nem lehet üres, a `structuredOutput` pedig nem lehet `null`.");
        }
        this.name = name;
        this.prompt = prompt;
        this.systemPrompt = systemPrompt;
        this.structuredOutput = structuredOutput;
    }


    public boolean expectsStructuredOutput(){
        return this.structuredOutput != null;
    }

    public String simulateResponse(){
        SchemaType primaryType = this.structuredOutput.getSchemaTypes()[0];
        return switch(primaryType){
            case INT -> "0";
            case STRING -> "sample";
            case BOOLEAN -> "true";
            case LIST_INT -> "[1,2,3]";
            case LIST_STRING -> "[\"a\",\"b\"]";
            case MAP_STRING_STRING -> "{\"kulcs\":\"érték\"}";
        };
    }
}