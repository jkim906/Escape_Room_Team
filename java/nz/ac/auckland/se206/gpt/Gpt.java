package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Utility class for generating GPT prompt engineering strings. */
public class Gpt {

  private ChatCompletionRequest chatCompletionRequest;

  /**
   * Creates a new GPT instance with the given temperature, topP, and maxTokens.
   *
   * @param temp the temperature to use
   * @param topP the topP to use
   * @param maxTokens the maxTokens to use
   */
  public Gpt(double temp, double topP, int maxTokens) {
    this.chatCompletionRequest =
        new ChatCompletionRequest()
            .setN(1)
            .setTemperature(temp)
            .setTopP(topP)
            .setMaxTokens(maxTokens);
  }

  /**
   * Generates a GPT prompt engineering string for the given item.
   *
   * @param msg the message to generate a prompt for
   * @return the generated prompt engineering string
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  public ChatMessage run(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
    Choice result = chatCompletionResult.getChoices().iterator().next();
    chatCompletionRequest.addMessage(result.getChatMessage());
    return result.getChatMessage();
  }
}
