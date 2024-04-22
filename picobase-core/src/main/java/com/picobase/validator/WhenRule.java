package com.picobase.validator;

/**
 * WhenRule is a validation rule that executes the given list of rules when the condition is true.
 */
public class WhenRule implements Rule {
    private boolean condition;
    private Rule[] rules;
    private Rule[] elseRules;


    public WhenRule(boolean condition, Rule[] rules, Rule[] elseRules) {
        this.condition = condition;
        this.rules = rules;
        this.elseRules = elseRules;
    }

    @Override
    public Error validate(Object value) {
        if (condition) {
            return Validation.validate(value, rules);
        } else {
            return Validation.validate(value, elseRules);
        }
    }

    /**
     * returns a validation rule that executes the given list of rules when the condition is false.
     */
    public WhenRule else_(Rule... rules) {
        this.elseRules = rules;
        return this;
    }
}