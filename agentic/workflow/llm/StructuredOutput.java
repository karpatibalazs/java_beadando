package agentic.workflow.llm;

import java.util.*;

public class StructuredOutput{
    private final SchemaType[] schemaTypes;

    public StructuredOutput(SchemaType[] schemaTypes){
        if(schemaTypes == null || schemaTypes.length == 0){
            throw new IllegalArgumentException("legalább egy sématípust meg kell adni.");
        }
        for(SchemaType type : schemaTypes){
            if(type == null){
                throw new NullPointerException("a megadott sématípusok között nem lehet null.");
            }
        }

        this.schemaTypes = schemaTypes.clone();
    }

    public SchemaType[] getSchemaTypes(){
        return schemaTypes.clone();
    }

    public boolean contains(SchemaType schemaType){
        for(SchemaType type : this.schemaTypes){
            if(type == schemaType){
                return true;
            }
        }
        return false;
    }

    public int size(){
        return schemaTypes.length;
    }
}