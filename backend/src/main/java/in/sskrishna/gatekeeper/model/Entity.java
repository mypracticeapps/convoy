package in.sskrishna.gatekeeper.model;

import lombok.Data;

@Data
public class Entity {
    private String id;

    public static String idFrom(Object... str) {
        String tmp = "";
        for (Object o : str) {
            tmp += o.toString();
        }
        return tmp;
    }
}
