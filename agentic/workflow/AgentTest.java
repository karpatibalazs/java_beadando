package agentic.workflow;

import static org.junit.jupiter.api.Assertions.*;
import module org.junit.jupiter;
import agentic.workflow.WorkflowStep;
import agentic.workflow.llm.SchemaType;
import agentic.workflow.llm.StructuredOutput;
import agentic.workflow.Agent;
import java.nio.file.Files;
import java.nio.file.Path;

public class AgentTest{
    @Test
    void testStepCount(){
        Agent agent = new Agent("testagent");
        StructuredOutput structuredOutput = new StructuredOutput(new SchemaType[]{SchemaType.STRING});
        WorkflowStep step1 = new WorkflowStep("step1", "prompt1", "systemprompt1", structuredOutput);
        WorkflowStep step2 = new WorkflowStep("step2", "prompt1", "systemprompt2", structuredOutput);

        assertEquals(0, agent.getStepCount());
        
        agent.addStep(step1);
        assertEquals(1, agent.getStepCount());
        
        agent.addStep(step2);
        assertEquals(2, agent.getStepCount());
    }

    @Test
    void testAddDuplicateStepRejected(){
        Agent agent = new Agent("testagent");
        StructuredOutput structuredOutput = new StructuredOutput(new SchemaType[]{SchemaType.STRING});
        WorkflowStep step1 = new WorkflowStep("step1", "prompt1", "systemprompt1", structuredOutput);
        WorkflowStep step2 = new WorkflowStep("step2", "prompt1", "systemprompt2", structuredOutput);
        agent.addStep(step1);
        WorkflowStep duplicateStep = new WorkflowStep("step1", "diffprompt", "diffsystemprompt", step1.getStructuredOutput());
        assertThrows(IllegalArgumentException.class, () ->{
            agent.addStep(duplicateStep);
        });
    }

    @Test
    void findStepByName() {
        Agent agent = new Agent("testagent");
        StructuredOutput structuredOutput = new StructuredOutput(new SchemaType[]{SchemaType.STRING});
        WorkflowStep step1 = new WorkflowStep("step1", "prompt1", "systemprompt1", structuredOutput);
        agent.addStep(step1);
        
        WorkflowStep found = agent.findStepByName("  step1   ");
        
        assertEquals("step1", found.getName());
    }


    @Test
    void findStepByNameMissing() {
        Agent agent = new Agent("testagent");
        StructuredOutput structuredOutput = new StructuredOutput(new SchemaType[]{SchemaType.STRING});
        WorkflowStep step1 = new WorkflowStep("step1", "prompt1", "systemprompt1", structuredOutput);
        agent.addStep(step1);
        WorkflowStep found = agent.findStepByName("NemLetezoLepes");
        assertEquals(null, found);
    }

    @TempDir
    Path tempDir;

    @Test
    void testLoadAgentSuccess() throws Exception {
        Path file = tempDir.resolve("valid.txt");
        Files.writeString(file, """
            AGENT: testagent
            STEP
            name=name
            prompt=prompt
            systemPrompt=sysprompt
            output=STRING
            ENDSTEP
            """);
            
        Agent loadedAgent = Agent.loadAgent(file.toString());
        
        assertEquals("testagent", loadedAgent.getName());
        assertEquals(1, loadedAgent.getStepCount());
    }

    @Test
    void testLoadAgentRejectsMissingHeader() throws Exception {
        Path file = tempDir.resolve("missing.txt");
        Files.writeString(file, """
            STEP
            name=name
            prompt=prompt
            systemPrompt=sysprompt
            output=STRING
            ENDSTEP
            """);
            
        assertThrows(WorkflowFormatException.class, () -> {
            Agent.loadAgent(file.toString());
        });
    }

    @Test
    void testLoadAgentRejectsDuplicateStepNames() throws Exception {
        Path file = tempDir.resolve("duplicate.txt");
        Files.writeString(file, """
            AGENT: badagent
            STEP
            name=name
            prompt=prompt1
            systemPrompt=sysprompt1
            output=STRING
            ENDSTEP
            STEP
            name=name
            prompt=prompt2
            systemPrompt=sysprompt2
            output=INT
            ENDSTEP
            """);
            
        assertThrows(IllegalArgumentException.class, () -> {
            Agent.loadAgent(file.toString());
        });
    }
}