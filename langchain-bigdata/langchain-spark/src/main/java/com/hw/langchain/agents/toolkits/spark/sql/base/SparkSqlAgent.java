package com.hw.langchain.agents.toolkits.spark.sql.base;

import com.hw.langchain.agents.agent.Agent;
import com.hw.langchain.agents.agent.AgentExecutor;
import com.hw.langchain.agents.mrkl.base.ZeroShotAgent;
import com.hw.langchain.agents.toolkits.spark.sql.toolkit.SparkSqlToolkit;
import com.hw.langchain.base.language.BaseLanguageModel;
import com.hw.langchain.chains.llm.LLMChain;
import com.hw.langchain.prompts.prompt.PromptTemplate;
import com.hw.langchain.tools.base.BaseTool;

import java.util.List;
import java.util.Map;

import static com.hw.langchain.agents.mrkl.prompt.Prompt.FORMAT_INSTRUCTIONS;
import static com.hw.langchain.agents.toolkits.spark.sql.prompt.Prompt.SQL_PREFIX;
import static com.hw.langchain.agents.toolkits.spark.sql.prompt.Prompt.SQL_SUFFIX;
import static com.hw.langchain.prompts.utils.FormatUtils.formatTemplate;

/**
 * @author HamaWhite
 */
public class SparkSqlAgent {

    private SparkSqlAgent() {
        // private constructor to hide the implicit public one
        throw new IllegalStateException("Utility class");
    }

    /**
     * Construct a Spark SQL agent from an LLM and tools.
     */
    public static AgentExecutor createSparkSqlAgent(BaseLanguageModel llm, SparkSqlToolkit toolkit) {
        return createSparkSqlAgent(llm, toolkit, SQL_PREFIX, SQL_SUFFIX, FORMAT_INSTRUCTIONS, null, 10, 15, null, "force");
    }

    /**
     * Construct a Spark SQL agent from an LLM and tools.
     */
    @SuppressWarnings("all")
    public static AgentExecutor createSparkSqlAgent(
            BaseLanguageModel llm,
            SparkSqlToolkit toolkit,
            String prefix,
            String suffix,
            String formatInstructions,
            List<String> inputVariables,
            int topK,
            Integer maxIterations,
            Float maxExecutionTime,
            String earlyStoppingMethod
    ) {
        List<BaseTool> tools = toolkit.getTools();
        prefix = formatTemplate(prefix, Map.of("top_k", topK));

        PromptTemplate prompt = ZeroShotAgent.createPrompt(tools, prefix, suffix, formatInstructions, inputVariables);
        LLMChain llmChain = new LLMChain(llm, prompt);

        List<String> toolNames = tools.stream().map(BaseTool::getName).toList();
        Agent agent = new ZeroShotAgent(llmChain, toolNames);

        return AgentExecutor.builder()
                .agent(agent)
                .tools(tools)
                .maxIterations(maxIterations)
                .maxExecutionTime(maxExecutionTime)
                .earlyStoppingMethod(earlyStoppingMethod)
                .build();
    }
}
