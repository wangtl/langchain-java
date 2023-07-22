package com.hw.langchain.tools.spark.sql.prompt;


/**
 * @author HamaWhite
 */
public class Prompt {

    private Prompt() {
        // private constructor to hide the implicit public one
        throw new IllegalStateException("Utility class");
    }

    public static final String QUERY_CHECKER = """
            {query}
            Double check the Spark SQL query above for common mistakes, including:
            - Using NOT IN with NULL values
            - Using UNION when UNION ALL should have been used
            - Using BETWEEN for exclusive ranges
            - Data type mismatch in predicates
            - Properly quoting identifiers
            - Using the correct number of arguments for functions
            - Casting to the correct data type
            - Using the proper columns for joins

            If there are any of the above mistakes, rewrite the query. If there are no mistakes, just reproduce the original query.""";

}
