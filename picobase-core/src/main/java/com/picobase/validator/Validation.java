package com.picobase.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Validation {

    /**
     * Nil is a validation rule that checks if a value is nil.
     * It is the opposite of NotNil rule
     */
    public static final AbsentRule Nil = new AbsentRule(true, false);

    /**
     * Empty checks if a not nil value is empty.
     */
    public static final AbsentRule Empty = new AbsentRule(true, true);

    /**
     * // Required is a validation rule that checks if a value is not empty.
     * // A value is considered not empty if
     * // - integer, float: not zero
     * // - bool: true
     * // - string, array, slice, map: len() > 0
     * // - interface, pointer: not nil and the referenced value is not empty
     * // - any other types
     */
    public static final RequiredRule required = new RequiredRule(false, true);

    /**
     * // NilOrNotEmpty checks if a value is a nil pointer or a value that is not empty.
     * // NilOrNotEmpty differs from Required in that it treats a nil pointer as valid.
     */
    public static final RequiredRule NilOrNotEmpty = new RequiredRule(true, true);

    /**
     * When returns a validation rule that executes the given list of rules when the condition is true.
     */
    public static WhenRule when(boolean condition, Rule... rules) {
        return new WhenRule(condition, rules, new Rule[0]);
    }

    /**
     * Each returns a validation rule that loops through an iterable (map, slice or array)
     * and validates each value inside with the provided rules.
     * An empty iterable is considered valid. Use the Required rule to make sure the iterable is not empty.
     */
    public static EachRule each(Rule... rules) {
        return new EachRule(rules);
    }

    /**
     * // Min returns a validation rule that checks if a value is greater or equal than the specified value.
     * // By calling Exclusive, the rule will check if the value is strictly greater than the specified value.
     * // Note that the value being checked and the threshold value must be of the same type.
     * // Only int, uint, float and time.Time types are supported.
     * // An empty value is considered valid. Please use the Required rule to make sure a value is not empty.
     */
    public static ThresholdRule min(Object value) {
        return ThresholdRule.min(value);
    }

    /**
     * // Max returns a validation rule that checks if a value is less or equal than the specified value.
     * // By calling Exclusive, the rule will check if the value is strictly less than the specified value.
     * // Note that the value being checked and the threshold value must be of the same type.
     * // Only int, uint, float and time.Time types are supported.
     * // An empty value is considered valid. Please use the Required rule to make sure a value is not empty.
     */
    public static ThresholdRule max(Object value) {
        return ThresholdRule.max(value);
    }

    /**
     * // Date returns a validation rule that checks if a string value is in a format that can be parsed into a date.
     * // The format of the date should be specified as the layout parameter which accepts the same value as that for time.Parse.
     * // For example,
     * //    validation.Date(time.ANSIC)
     * //    validation.Date("02 Jan 06 15:04 MST")
     * //    validation.Date("2006-01-02")
     * //
     * // By calling Min() and/or Max(), you can let the Date rule to check if a parsed date value is within
     * // the specified date range.
     * //
     * // An empty value is considered valid. Use the Required rule to make sure a value is not empty.
     */
    public static DateRule date(String layout) {
        return new DateRule(layout);
    }

    /**
     * // NotIn returns a validation rule that checks if a value is absent from the given list of values.
     * // Note that the value being checked and the possible range of values must be of the same type.
     * // An empty value is considered valid. Use the Required rule to make sure a value is not empty.
     */
    public static NotInRule notIn(Object... values) {
        return new NotInRule(values);
    }

    public static Error validate(Object value, Rule... rules) {
        for (Rule rule : rules) {
            if (rule instanceof SkipRule skipRule) {
                if (skipRule.isSkip()) {
                    return null;
                }
            }

            Error error = rule.validate(value);
            if (error instanceof ErrorObject errorObject) {
                return errorObject;
            }
            if (error instanceof Errors errors && errors.size() > 0) {
                return errors.filter();
            }
        }
        return null;
    }

    public static LengthRule length(int min, int max) {
        return new LengthRule(min, max);
    }

    public static MatchRule match(Pattern p) {
        return new MatchRule(p);
    }

    public static InlineRule by(RuleFunc ruleFunc) {
        return new InlineRule(ruleFunc);
    }

    public static InRule in(Object... values) {
        return new InRule(values);
    }

    /**
     * 对象结构的校验
     *
     * @param obj        待校验对象
     * @param fieldRules 校验规则
     * @return 校验结果
     */
    public static Errors validateObject(Object obj, FieldRules... fieldRules) {
        if (obj == null || fieldRules == null) { //不定参中 1个null 为整体null ， 多个 null 为 null数组
            return null;
        }

        var errors = new Errors();
        List<FieldRules> rules = Arrays.stream(fieldRules).filter(Objects::nonNull).toList();
        for (int i = 0; i < rules.size(); i++) {
            FieldRules fr = rules.get(i);
            fr.setCheckObj(obj);
            var err = validate(fr.checkValue(), fr.rules);
            if (err != null) {
                errors.put(fr.fieldName, err);
            }
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return null;
    }

    public static FieldRules field(String fieldName, Object val, Rule... rules) {
        return new FieldRules(fieldName, val, rules);
    }

    public static <T, R> FieldRules field(FieldFns.FieldFn<T, R> fn, Rule... rules) {
        return new FieldRules<>(fn, rules);
    }


    /**
     * NewStringRule creates a new validation rule using a function that takes a string value and returns a bool.
     * The rule returned will use the function to check if a given string or byte slice is valid or not.
     * An empty value is considered to be valid. Please use the Required rule to make sure a value is not empty.
     */
    public static StringRule newStringRule(StringRule.StringValidator<String, Boolean> stringValidator, String message) {
        return new StringRule(stringValidator, message);
    }

    public static StringRule newStringRuleWithError(StringRule.StringValidator<String, Boolean> stringValidator, Error error) {
        return new StringRule(stringValidator, error);
    }


}

