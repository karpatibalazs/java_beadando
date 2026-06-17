package agentic.workflow;
import agentic.workflow.llm.SchemaType;
import agentic.workflow.llm.StructuredOutput;

import static org.junit.jupiter.api.Assertions.*;
import module org.junit.jupiter;

public class WorkflowStepTest{
    @Test
    void testExpectsStructuredOutput(){
        StructuredOutput validStructuredOutput = new StructuredOutput(new SchemaType[]{SchemaType.STRING});

        WorkflowStep step = new WorkflowStep(
            "name", 
            "prompt", 
            "systemprompt", 
            validStructuredOutput
        );
        
        assertTrue(step.expectsStructuredOutput());
    }
}