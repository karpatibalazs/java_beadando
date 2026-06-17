package agentic.workflow.llm;

import static org.junit.jupiter.api.Assertions.*;
import module org.junit.jupiter;

public class StructuredOutputTest{

    @Test
    void testContainsExistingType(){
        SchemaType type1 = SchemaType.INT;
        SchemaType type2 = SchemaType.STRING;

        StructuredOutput structuredOutput = new StructuredOutput(new SchemaType[]{type1, type2});
        assertTrue(structuredOutput.contains(type1));
        assertTrue(structuredOutput.contains(type2));
    }

    @Test
    void testContainsMissingType(){
        SchemaType missingType = SchemaType.STRING;
        SchemaType type1 = SchemaType.INT;
        StructuredOutput structuredOutput = new StructuredOutput(new SchemaType[]{type1});
        assertFalse(structuredOutput.contains(missingType));
    }

    @Test
    void testSize(){
        SchemaType type1 = SchemaType.STRING;
        SchemaType type2 = SchemaType.INT;

        StructuredOutput structuredOutput = new StructuredOutput(new SchemaType[]{type1, type2});
        assertEquals(2, structuredOutput.size());
    }
}