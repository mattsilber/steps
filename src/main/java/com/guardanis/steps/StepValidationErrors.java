package com.guardanis.steps;

import java.util.ArrayList;
import java.util.List;

public class StepValidationErrors {

    private List<String> errors = new ArrayList<String>();

    public StepValidationErrors add(String error){
        errors.add(error);

        return this;
    }

    public List<String> getErrors(){
        return errors;
    }

    public boolean hasErrors(){
        return 0 < errors.size();
    }

    @Override
    public String toString() {
        String errorMessage = "";

        for(String s : errors)
            errorMessage += s + "\n";

        if(errorMessage.length() > 3)
            errorMessage = errorMessage.substring(0, errorMessage.length() - 1);

        return errorMessage;
    }

}
