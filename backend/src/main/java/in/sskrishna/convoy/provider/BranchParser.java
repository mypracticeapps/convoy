package in.sskrishna.convoy.provider;

import kong.unirest.JsonNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BranchParser {
    public static void parse(JsonNode node) {
      log.info("Found {} branchs", node.getArray().length());
      for(Object obj: node.getArray()){
          System.out.println(obj);
      }
    }
}
