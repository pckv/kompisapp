package me.pckv.kompisapp.ui;

import androidx.annotation.IdRes;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class FormValidator {

    private HashMap<Integer, FormField> fields;

    @Getter
    private MediatorLiveData<Boolean> valid;

    @Getter
    private MutableLiveData<String> error;


    public FormValidator() {
        this.fields = new HashMap<>();
        this.valid = new MediatorLiveData<>();
        this.error = new MediatorLiveData<>();
    }

    public static Validator lengthValidator(int length) {
        return field -> field.trim().length() >= length;
    }

    public void addField(@IdRes Integer field, String errorMessage, Validator validator) {
        MutableLiveData<String> liveData = new MutableLiveData<>("");

        fields.put(field, new FormField(liveData, validator, errorMessage));
        valid.addSource(liveData, s -> valid.setValue(isValid()));
    }

    public MutableLiveData<String> getField(Integer field) {
        return fields.get(field).liveData;
    }

    private boolean isValid() {
        return fields.values().stream().allMatch(FormField::isValid);
    }

    public interface Validator {
        boolean isValid(String field);
    }

    @AllArgsConstructor
    private class FormField {

        protected MutableLiveData<String> liveData;
        protected Validator validator;
        private String errorMessage;

        protected boolean isValid() {
            String value = liveData.getValue();
            boolean valid = value != null && validator.isValid(value);

            // Update the error on valid check
            if (!valid && !value.isEmpty()) {
                error.setValue(errorMessage);
            } else {
                error.setValue(null);
            }

            return valid;
        }
    }
}
